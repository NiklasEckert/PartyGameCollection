package fes.profession.partygamecompanion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class WifiDirectBroadcastReceiverAlt extends BroadcastReceiver {

    private static final String TAG = "WifiDirectBR";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BottleCreateGroupActivity mActivity;

    public WifiDirectBroadcastReceiverAlt(WifiP2pManager manager, Channel channel, BottleCreateGroupActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //mActivity.setIsWifiP2pEnabled(true);
            } else {
                //mActivity.setIsWifiP2pEnabled(false);
                //mActivity.resetData();
            }
            //Log.d(mActivity.TAG, "P2P peers changed - "+state);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (mManager != null) {
                mManager.requestPeers(mChannel, (PeerListListener) mActivity.getFragmentManager().findFragmentById(R.id.frag_list));
            }
            //Log.d(BottleCreateGroupActivity.TAG, "P2P peers changed");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (mManager == null) {
                return;
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                DeviceListFragment deviceListFragment = (DeviceListFragment) mActivity.getFragmentManager().findFragmentById(R.id.frag_list);
                mManager.requestConnectionInfo(mChannel, deviceListFragment);
            } else {
                //mActivity.resetData();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            DeviceListFragment fragment = (DeviceListFragment) mActivity.getFragmentManager().findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }
    }
}
