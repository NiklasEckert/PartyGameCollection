package fes.profession.partygamecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BottleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);
    }

    public void startBottleCreateGroup(View view) {
        Intent intent = new Intent(this, BottleCreateGroupActivity.class);
        startActivity(intent);
    }

    public void startBottleJoinGroup(View view) {
        Intent intent = new Intent(this, BottleJoinGroupActivity.class);
        startActivity(intent);
    }
}
