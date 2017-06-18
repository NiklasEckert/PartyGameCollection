package fes.profession.partygamecompanion;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {

    Button btnStart;
    Button btnStop;
    TextView textView;
    Handler customHandler = new Handler();

    long startTime=0L;
    long timeInMilliseconds=0L;
    long timeSwapBuff=0L;
    long updateTime=0L;

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMilliseconds;
            int secs=(int) (updateTime/1000);
            int mins=secs/60;
            secs%=60;
            int milliseconds=(int) (updateTime%1000);
            textView.setText(""+mins+":"+String.format("%2d",secs)+":"+String.format("%3d",milliseconds));
            customHandler.postDelayed(this,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        btnStart = (Button) findViewById(R.id.btn_start_stopwatch);
        btnStop = (Button) findViewById(R.id.btn_stop_stopwatch);
        textView = (TextView) findViewById(R.id.tv_stopwatch);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread,0);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff+=timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
            }
        });
    }
}
