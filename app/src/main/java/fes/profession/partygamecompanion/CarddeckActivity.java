package fes.profession.partygamecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class CarddeckActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    private TextView up, down, middle;
    private Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carddeck);

        r = new Random();

        imageView = (ImageView) findViewById(R.id.iv_carddeck);
        up = (TextView) findViewById(R.id.tv_up);
        up.setText("");
        down = (TextView) findViewById(R.id.tv_down);
        down.setText("");
        middle = (TextView) findViewById(R.id.tv_middle);
        middle.setText("");

        button = (Button) findViewById(R.id.btn_draw_card);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard();
            }
        });
    }

    private void drawCard() {
        int cardType = r.nextInt(4);
        switch (cardType) {
            case 0: imageView.setImageResource(R.drawable.herz); break;
            case 1: imageView.setImageResource(R.drawable.karo); break;
            case 2: imageView.setImageResource(R.drawable.pik); break;
            case 3: imageView.setImageResource(R.drawable.kreuz); break;
        }

        int cardValue = r.nextInt(13);
        switch (cardValue) {
            case 0: up.setText("2"); down.setText("2"); middle.setText("2"); break;
            case 1: up.setText("3"); down.setText("3"); middle.setText("3"); break;
            case 2: up.setText("4"); down.setText("4"); middle.setText("4"); break;
            case 3: up.setText("5"); down.setText("5"); middle.setText("5"); break;
            case 4: up.setText("6"); down.setText("6"); middle.setText("6"); break;
            case 5: up.setText("7"); down.setText("7"); middle.setText("7"); break;
            case 6: up.setText("8"); down.setText("8"); middle.setText("8"); break;
            case 7: up.setText("9"); down.setText("9"); middle.setText("9"); break;
            case 8: up.setText("10"); down.setText("10"); middle.setText("10"); break;
            case 9: up.setText("BUBE"); down.setText("BUBE"); middle.setText("BUBE"); break;
            case 10: up.setText("DAME"); down.setText("DAME"); middle.setText("DAME"); break;
            case 11: up.setText("KÖNIG"); down.setText("KÖNIG"); middle.setText("KÖNIG"); break;
            case 12: up.setText("ASS"); down.setText("ASS"); middle.setText("ASS"); break;
        }


    }
}
