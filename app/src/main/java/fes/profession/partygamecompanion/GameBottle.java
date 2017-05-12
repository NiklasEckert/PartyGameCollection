package fes.profession.partygamecompanion;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Niklas on 12.05.2017.
 */

public class GameBottle implements Runnable {

    private Socket socket = null;
    private Handler handler;

    public GameBottle(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = "GameBottle";

    @Override
    public void run() {
        try {
            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(BottleJoinGroupActivity.MY_HANDLE, this).sendToTarget();
            while (true) {
                try {
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    Log.d(TAG, "Rec: "+String.valueOf(buffer));
                    handler.obtainMessage(BottleJoinGroupActivity.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTick(byte[] buffer) {
        try {
            oStream.write(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Exception during wirte", e);
        }
    }
}
