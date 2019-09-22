package edu.bigredhack.recyclecontamination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class result extends AppCompatActivity {
TextView heading,instrunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent= getIntent();
        String title= intent.getStringExtra("TITLE");
        String steps= intent.getStringExtra("STEPS");
        heading= findViewById(R.id.title);
        instrunctions= findViewById(R.id.steps);
        heading.setText(title);
        instrunctions.setText(steps);
    }
}
