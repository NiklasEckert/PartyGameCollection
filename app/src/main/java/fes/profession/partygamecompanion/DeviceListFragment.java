package fes.profession.partygamecompanion;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niklas on 05.05.2017.
 */

public class DeviceListFragment extends ListFragment implements PeerListListener, WifiP2pManager.ConnectionInfoListener {

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    ProgressDialog progressDialog = null;
    private View mContentView = null;
    private WifiP2pDevice myDevice;
    private WifiP2pDevice device;
    private WifiP2pInfo info;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setListAdapter(new WifiDirectPeerListAdapter(getActivity(), R.layout.row_device, peers, this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.device_list, null);

/*        mContentView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
                        "Connecting to: "+device.deviceAddress, true, true);
                ((DeviceActionListener) getActivity()).connect(config);
            }
        });*/

        return  mContentView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        device = (WifiP2pDevice) getListAdapter().getItem(position);
        if (mContentView.findViewById(R.id.btn_connect).getVisibility() == View.VISIBLE) {
            mContentView.findViewById(R.id.btn_connect).setVisibility(View.INVISIBLE);
        } else {
            mContentView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    config.wps.setup = WpsInfo.PBC;
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
                            "Connecting to: "+device.deviceAddress, true, true);
                    ((DeviceActionListener) getActivity()).connect(config);
                }
            });
            mContentView.findViewById(R.id.btn_connect).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((WifiDirectPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        if (peers.size() == 0) {
            //Log.d(BottleCreateGroupActivity.TAG, "No devices found");
            return;
        }
    }

    public void clearPeers() {
        peers.clear();
        ((WifiDirectPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public void onInitiateDiscorvery() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "finding peers", true,
                true, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
    }

    public static String getDeviceStatus(int deviceStatus) {
        //Log.d(BottleCreateGroupActivity.TAG, "Peer status: "+deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.UNAVAILABLE:
                return  "Unavailable";
            case WifiP2pDevice.FAILED:
                return  "Failed";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            default:
                return "Unknown";
        }
    }

    public void updateThisDevice(WifiP2pDevice myDevice) {
        this.myDevice = myDevice;
        TextView view = (TextView) mContentView.findViewById(R.id.my_name);
        view.setText(myDevice.deviceName);
        view = (TextView) mContentView.findViewById(R.id.my_status);
        view.setText(getDeviceStatus(myDevice.status));
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        this.info = info;
        this.getView().setVisibility(View.VISIBLE);

        TextView view = (TextView) mContentView.findViewById(R.id.txt_iAmOwner);
        view.setText((info.isGroupOwner == true) ? "yes":"no");

        view = (TextView) mContentView.findViewById(R.id.txt_ownerIp);
        view.setText("Host IP: "+info.groupOwnerAddress.getHostAddress());

        if (info.groupFormed && info.isGroupOwner) {

        } else if (info.groupFormed) {

        }
    }

    public WifiP2pDevice getDevice() {
        return device;
    }

    public WifiP2pDevice getMyDevice() {
        return myDevice;
    }

    public interface DeviceActionListener {
        void showDetails(WifiP2pDevice device);
        void cancelDisconnect();
        void connect(WifiP2pConfig config);
        void disconnect();
    }
}
