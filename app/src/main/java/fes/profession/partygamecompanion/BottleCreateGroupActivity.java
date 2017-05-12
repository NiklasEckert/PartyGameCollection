package fes.profession.partygamecompanion;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BottleCreateGroupActivity extends Activity implements ChannelListener, PeerListListener, DeviceListFragment.DeviceActionListener {

    public static final String TAG = "BottleCrGroupActivity";

    public static final String SERVICE_INSTANCE = "_PartyGroupCompanion";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    static final int SERVER_PORT = 4545;

    WifiP2pManager mManager;
    Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    Context context;

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {

        }
    };
    private boolean isWifiP2pEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle_create_group);

        String message = "onCreate";
        Toast myToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        myToast.show();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WifiDirectBroadcastReceiverAlt(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void resetData() {
        DeviceListFragment deviceListFragment = (DeviceListFragment) getFragmentManager().findFragmentById(R.id.frag_list);
        if (deviceListFragment != null) {
            deviceListFragment.clearPeers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atn_direct_discover:
                if (!isWifiP2pEnabled) {
                    Toast.makeText(BottleCreateGroupActivity.this, "Enable P2P", Toast.LENGTH_SHORT).show();
                    return true;
                }
                final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager().findFragmentById(R.id.frag_list);
                fragment.onInitiateDiscorvery();
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(BottleCreateGroupActivity.this, "Discover", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(BottleCreateGroupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showDetails(WifiP2pDevice device) {

    }

    @Override
    public void cancelDisconnect() {
        if (mManager != null) {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager().findFragmentById(R.id.frag_list);
            if (fragment.getMyDevice() == null || fragment.getMyDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getMyDevice().status == WifiP2pDevice.AVAILABLE ||
                        fragment.getMyDevice().status == WifiP2pDevice.INVITED) {
                mManager.cancelConnect(mChannel, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(BottleCreateGroupActivity.this, "Aborting connection", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(BottleCreateGroupActivity.this, "Connect abort request failed. Reason Code: "+reason, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void connect(WifiP2pConfig config) {
        mManager.connect(mChannel, config, new ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(BottleCreateGroupActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(BottleCreateGroupActivity.this, "Connect failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager().findFragmentById(R.id.frag_list);
        mManager.removeGroup(mChannel, new ActionListener() {
            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Disconnect failed" + reason);
            }
        });
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public boolean getIsWifiP2pEnabled() {
        return this.isWifiP2pEnabled;
    }

    @Override
    public void onChannelDisconnected() {

    }

    public WifiP2pManager.PeerListListener getPeerListListener() {
        return this.peerListListener;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }
}
