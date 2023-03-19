package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private ListView list;


     ListView list;
     DatabaseReference databaseReference;
    private TextView tv1,tv2,tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=findViewById(R.id.listView);
        ArrayList<String> qwe=new ArrayList<String>();
        ArrayAdapter<String> listView=new ArrayAdapter<>(MainActivity.this,R.layout.activity_details2,qwe);
        list.setAdapter(listView);
        DatabaseReference refernce= FirebaseDatabase.getInstance().getReference().child("Courses");
        refernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qwe.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    qwe.add(snapshot1.getValue().toString());
                }
                listView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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