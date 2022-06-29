package com.example.eatright;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Result extends AppCompatActivity {
    TextView d1;
    TextView d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        d1=(TextView) findViewById(R.id.showdata1);
        d2=(TextView) findViewById(R.id.showdata2);
        d1.setText(getIntent().getStringExtra("data1"));
        d2.setText(getIntent().getStringExtra("data2"));
    }
}
