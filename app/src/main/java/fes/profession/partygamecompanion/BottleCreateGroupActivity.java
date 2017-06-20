package fes.profession.partygamecompanion;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class BottleCreateGroupActivity extends Activity {

    private Random r;
    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_spinbottle);

        button = (Button) findViewById(R.id.btn_rotate_single);
        imageView = (ImageView) findViewById(R.id.iv_bottle_single);
        r = new Random();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int angle = r.nextInt(360);
                RotateAnimation rotateAnimation = new RotateAnimation(0, 3600+angle,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(3000);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                imageView.startAnimation(rotateAnimation);
            }
        });
    }
}
