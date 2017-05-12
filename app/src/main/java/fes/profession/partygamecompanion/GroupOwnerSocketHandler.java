package fes.profession.partygamecompanion;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Niklas on 12.05.2017.
 */

public class GroupOwnerSocketHandler extends Thread {
    ServerSocket socket = null;
    private final int THREAD_COUNT = 10;
    private Handler handler;
    private static final String TAG = "GroupOwnerSocketHandler";

    private final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public GroupOwnerSocketHandler(Handler handler) throws IOException {
        try {
            socket = new ServerSocket(4545);
            this.handler = handler;
            Log.d(TAG, "Socket Started");
        } catch (IOException e) {
            e.printStackTrace();
            poolExecutor.shutdownNow();
            throw e;
        }
    }

    @Override
    public void run(){
        while (true) {
            try {
                poolExecutor.execute(new GameBottle(socket.accept(), handler));
                Log.d(TAG, "Launching the I/O handler");
            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                poolExecutor.shutdownNow();
                break;
            }
        }
    }
}
