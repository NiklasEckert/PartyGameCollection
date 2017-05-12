package fes.profession.partygamecompanion;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Niklas on 12.05.2017.
 */

public class BottleGameFragment extends Fragment {

    private View view;
    private GameBottle gameBottle;
    private TextView textView;
    private String tick = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.flaschendrehen, container, false);
        textView = (TextView) view.findViewById(R.id.flaschendrehen_status);

        view.findViewById(R.id.btn_startBottleGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBottle != null) {
                    gameBottle.sendTick(tick.getBytes());
                }
            }
        });

        return view;
    }

    public interface TickTarget {
        public Handler getHandler();
    }

}
