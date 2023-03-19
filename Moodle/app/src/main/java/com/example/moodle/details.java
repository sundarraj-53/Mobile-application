package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class details extends AppCompatActivity {

    private ListView list;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        back=findViewById(R.id.back);
        list=findViewById(R.id.list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(details.this, "Back to Main Menu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(details.this,admin.class));
            }
        });
        ArrayList<String> qwe=new ArrayList<String>();
        ArrayAdapter<String> listView=new ArrayAdapter<>(details.this,R.layout.activity_details2,qwe);
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

    }
}