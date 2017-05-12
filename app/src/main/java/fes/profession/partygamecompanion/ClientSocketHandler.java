package fes.profession.partygamecompanion;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Niklas on 12.05.2017.
 */

public class ClientSocketHandler extends Thread {
    private static final String TAG = "ClientSocketHandler";
    private Handler handler;
    private GameBottle gameBottle;
    private InetAddress mAddress;

    public ClientSocketHandler(Handler handler, InetAddress groupOwnerAddress) {
        this.handler = handler;
        this.mAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try  {
            socket.bind(null);
            socket.connect(new InetSocketAddress(mAddress.getHostAddress(), BottleJoinGroupActivity.SERVER_PORT), 5000);
            Log.d(TAG, "Launching the I/O handler");
            gameBottle = new GameBottle(socket, handler);
            new Thread(gameBottle).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException el) {
                el.printStackTrace();
            }
            return;
        }
    }
}
