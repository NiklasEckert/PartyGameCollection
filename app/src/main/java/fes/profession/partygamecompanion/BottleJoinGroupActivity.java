package fes.profession.partygamecompanion;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BottleJoinGroupActivity extends Activity implements
        WiFiDirectServicesList.DeviceClickListener, Handler.Callback, BottleGameFragment.TickTarget,
        WifiP2pManager.ConnectionInfoListener {

    public static final String TAG = "BottleJoinGroupActivity";

    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "PartyGameCompanion";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    public static final int MESSAGE_READ = 0x400 + 1;
    public static final int MY_HANDLE = 0x400 + 2;
    private WifiP2pManager manager;

    static final int SERVER_PORT = 4545;

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;

    private Handler handler = new Handler(this);
    private BottleGameFragment bottleGameFragment;
    private WiFiDirectServicesList servicesList;

    private TextView statusTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle_join_group);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        startRegistrationAndDiscovery();

        statusTxtView = (TextView) findViewById(R.id.flaschendrehen_status);

        servicesList = new WiFiDirectServicesList();
        getFragmentManager().beginTransaction().add(R.id.container_root, servicesList, "services").commit();
    }

    @Override
    protected void onRestart() {
        Fragment frag = getFragmentManager().findFragmentByTag("services");
        if (frag != null) {
            getFragmentManager().beginTransaction().remove(frag).commit();
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (manager != null && channel != null) {
            manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "Disconnect failed. Reason: "+reason);
                }
            });
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    public void startRegistrationAndDiscovery() {
        Map<String, String> record = new HashMap<String, String>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");

        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance(SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
        manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus("Added Local Service");
            }

            @Override
            public void onFailure(int reason) {
                appendStatus("Failed to add a service");
            }
        });
        discoverService();
    }

    private void discoverService() {
        manager.setDnsSdResponseListeners(channel, new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
                if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {
                    WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager().findFragmentByTag("services");
                    if (fragment != null) {
                        WiFiDirectServicesList.WiFiDevicesAdapter adapter = ((WiFiDirectServicesList.WiFiDevicesAdapter) fragment.getListAdapter());
                        WiFiP2pService service = new WiFiP2pService();
                        service.device = srcDevice;
                        service.instanceName = instanceName;
                        service.serviceRegistrationType = registrationType;
                        adapter.add(service);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "onBonjourServiceAvailable "+instanceName);
                    }
                }

            }
        }, new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
            public void onDnsSdTxtRecordAvailable(
                    String fullDomainName, Map<String, String> record,
                    WifiP2pDevice device) {
                Log.d(TAG, device.deviceName + " is " + record.get(TXTRECORD_PROP_AVAILABLE));
            }
        });

        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus("Added service discovery request");
            }

            @Override
            public void onFailure(int reason) {
                appendStatus("Failed adding service discovery request");
            }
        });
        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus("Service discovery initiated");
            }

            @Override
            public void onFailure(int reason) {
                appendStatus("Service discovery failed");
            }
        });


    }

    public void appendStatus(String status) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Thread handler = null;
        if (info.isGroupOwner) {
            Log.d(TAG, "Connected as group owner");
            try {
                handler = new GroupOwnerSocketHandler(this.getHandler());
                handler.start();
            } catch (IOException e) {
                Log.d(TAG, "Failed to create a server thread - "+e.getMessage());
                return;
            }
        } else {
            Log.d(TAG, "Connected as peer");
            handler = new ClientSocketHandler(this.getHandler(), info.groupOwnerAddress);
            handler.start();
        }
        bottleGameFragment = new BottleGameFragment();
        getFragmentManager().beginTransaction().replace(R.id.container_root, bottleGameFragment).commit();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.d(TAG, readMessage);
                if (readMessage == "1") {
                    (bottleGameFragment).showGameResult(false);
                } else {
                    (bottleGameFragment).showGameResult(true);
                }
                break;
            case MY_HANDLE:
                Object obj = msg.obj;
                (bottleGameFragment).setGameBottle((GameBottle) obj);
        }
        return true;
    }

    @Override
    public void connectP2p(WiFiP2pService wiFiP2pService) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wiFiP2pService.device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        if (serviceRequest != null) {
            manager.removeServiceRequest(channel, serviceRequest, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reason) {

                }
            });
        }
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                appendStatus("Connecting to service");
            }

            @Override
            public void onFailure(int reason) {
                appendStatus("Failed connecting to service");
            }
        });
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

}
