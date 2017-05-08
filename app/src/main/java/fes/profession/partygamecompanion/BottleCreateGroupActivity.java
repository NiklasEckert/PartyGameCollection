package fes.profession.partygamecompanion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BottleCreateGroupActivity extends AppCompatActivity implements ChannelListener, PeerListListener {

    public static final String TAG = "BottleCrGroupActivity";

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
    private boolean isWifiP2pEnabeld = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle_create_group);

        String message = "onCreate";
        Toast myToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        myToast.show();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                String message = "onSuccess";
                Toast myToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                myToast.show();
            }

            @Override
            public void onFailure(int reason) {
                String message = "onFailure";
                Toast myToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void setIsWifiP2pEnabeld(boolean isWifiP2pEnabeld) {
        this.isWifiP2pEnabeld = isWifiP2pEnabeld;
    }

    public boolean getIsWifiP2pEnabeld() {
        return this.isWifiP2pEnabeld;
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
