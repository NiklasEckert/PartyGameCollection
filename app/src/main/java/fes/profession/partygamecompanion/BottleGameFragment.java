package fes.profession.partygamecompanion;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Niklas on 12.05.2017.
 */

public class BottleGameFragment extends Fragment {

    private View view;
    private GameBottle gameBottle;
    private ImageView imageView;
    private Button button;
    private String tick = "1";
    private TextView textView;
    private int angle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.spinbottle, container, false);

        imageView = (ImageView) view.findViewById(R.id.iv_bottle);
        button = (Button) view.findViewById(R.id.btn_rotate);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBottle != null) {
                    if (gameBottle.chooseLooser()) {
                        showGameResult(true);
                    } else {
                        showGameResult(false);
                    }
                }
            }
        });




        /* textView = (TextView) view.findViewById(R.id.flaschendrehen_status);

        view.findViewById(R.id.btn_startBottleGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBottle != null) {
                    gameBottle.sendTick(tick.getBytes());
                }
            }
        });*/

        return view;
    }

    public void showGameResult(boolean isChoosen) {
        if (isChoosen) {
            angle = 3780;
        } else {
            angle = 3600;
        }
        RotateAnimation rotate = new RotateAnimation(0, angle, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setFillAfter(true);
        rotate.setDuration(3600);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());
        imageView.startAnimation(rotate);
    }

    public interface TickTarget {
        Handler getHandler();
    }

    public void pushTick(String readMessage) {
        textView.setText(readMessage);
    }

    public void setGameBottle(GameBottle gameBottle) {
        this.gameBottle = gameBottle;
    }

}
