package fes.profession.partygamecompanion;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Niklas on 05.05.2017.
 */

public class WifiDirectPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    private List<WifiP2pDevice> items;
    private DeviceListFragment deviceListFragment;

    public WifiDirectPeerListAdapter(Context context, int textViewResourceId, List<WifiP2pDevice> objects, DeviceListFragment deviceListFragment) {
        super(context, textViewResourceId, objects);
        items = objects;
        this.deviceListFragment = deviceListFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) deviceListFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_device, null);
        }
        WifiP2pDevice device = items.get(position);
        if (device != null) {
            TextView top = (TextView) v.findViewById(R.id.device_name);
            TextView bot = (TextView) v.findViewById(R.id.device_detail);
            if (top != null) {
                top.setText(device.deviceName);
            }
            if (bot != null) {
                bot.setText(deviceListFragment.getDeviceStatus(device.status));
            }
        }
        return  v;
    }

    public void setDeviceListFragment(DeviceListFragment deviceListFragment) {
        this.deviceListFragment = deviceListFragment;
    }
}
