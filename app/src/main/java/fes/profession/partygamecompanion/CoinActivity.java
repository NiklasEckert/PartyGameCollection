package fes.profession.partygamecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class CoinActivity extends AppCompatActivity {

    private Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        Button button = (Button) findViewById(R.id.btn_coin);
        final ImageView imageView = (ImageView) findViewById(R.id.iv_coin);
        r = new Random();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int coinSide = r.nextInt(2);

                if (coinSide == 0) {
                    imageView.setImageResource(R.drawable.coin_up);
                } else {
                    imageView.setImageResource(R.drawable.coin_down);
                }

                RotateAnimation rotateAnimation = new RotateAnimation(0, 3600,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(3000);
                imageView.startAnimation(rotateAnimation);
            }
        });
    }
}
