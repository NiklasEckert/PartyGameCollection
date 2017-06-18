package fes.profession.partygamecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class DiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        TextView textView = (TextView) findViewById(R.id.tv_show_dice);
        textView.setText("6"+"   |   "+"6");
    }

    public void rollTheDice(View view) {
        Random r = new Random();
        TextView textView = (TextView) findViewById(R.id.tv_show_dice);
        int number1 = r.nextInt(6)+1;
        int number2 = r.nextInt(6)+1;
        textView.setText(number1+"   |   "+number2);
    }
}
