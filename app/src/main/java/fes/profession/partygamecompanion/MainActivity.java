package fes.profession.partygamecompanion;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "fes.proffesion.partygamecompanion.MESSAGE";

    private final IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBottle(View view) {
        Intent intent = new Intent(this, BottleActivity.class);
        startActivity(intent);
    }

    public void startDice(View view) {
        Intent intent = new Intent(this, DiceActivity.class);
        startActivity(intent);
    }

    public void startStopwatch(View view) {
        Intent intent = new Intent(this, StopwatchActivity.class);
        startActivity(intent);
    }

    public void startCountdown(View view) {
        Intent intent = new Intent(this, CountdownActivity.class);
        startActivity(intent);
    }

    public void startCoin(View view) {
        Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);
    }

    public void startCarddeck(View view) {
        Intent intent = new Intent(this, CarddeckActivity.class);
        startActivity(intent);
    }
}
