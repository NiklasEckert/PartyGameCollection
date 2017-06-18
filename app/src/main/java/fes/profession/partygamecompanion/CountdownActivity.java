package fes.profession.partygamecompanion;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CountdownActivity extends AppCompatActivity {

    private Button btnStartStop;
    private TextView textView;
    private EditText editText;
    private int timer;
    private long timeCountInMilliseconds;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        editText = (EditText) findViewById(R.id.et_countdown);
        editText.setText("");

        textView = (TextView) findViewById(R.id.tv_coutdown);

        btnStartStop = (Button) findViewById(R.id.btn_startStop_Countdown);
        btnStartStop.setText("Start");
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStartStop.getText().toString() == "Start") {
                    startCountdown();
                } else {
                    stopCountdown();
                }
            }
        });
    }

    public void startCountdown() {
        if (editText.getText().toString().isEmpty()) {
            timer = 1;
        } else {
            timer = Integer.valueOf(editText.getText().toString());
        }
        btnStartStop.setText("Stop");
        countDownTimer = new CountDownTimer(timer * 1000 , 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secs=(int) (millisUntilFinished/1000);
                int mins=secs/60;
                secs%=60;
                int milliseconds=(int) (millisUntilFinished%1000);
                textView.setText(""+mins+":"+String.format("%2d",secs)+":"+String.format("%3d",milliseconds));
            }

            @Override
            public void onFinish() {
                btnStartStop.setText("Start");
                textView.setText("Done!");
            }
        }.start();
    }

    public void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            btnStartStop.setText("Start");
        }
    }
}
