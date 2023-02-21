package com.example.moodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv1,tv2,tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1=findViewById(R.id.home);
        tv2=findViewById(R.id.dashboard);
        tv3=findViewById(R.id.user);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                Toast.makeText(MainActivity.this, "Home page", Toast.LENGTH_SHORT).show();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Dashboard.class));
                Toast.makeText(MainActivity.this, "Dashboard page", Toast.LENGTH_SHORT).show();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,user.class));
                Toast.makeText(MainActivity.this, "user page", Toast.LENGTH_SHORT).show();
            }
        });
    }
}