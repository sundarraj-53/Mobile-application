package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin extends AppCompatActivity {

    private Button Logout,View,Submit;
    private EditText Newcourse;
//    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Logout=findViewById(R.id.Logout);
        View=findViewById(R.id.view);
        Newcourse=findViewById(R.id.newcourse);
//        list=findViewById(R.id.list);
        Submit=findViewById(R.id.submit);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(admin.this, "Signing Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(admin.this,Login.class));
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                int i=6;
                String course=Newcourse.getText().toString();
                if(course.isEmpty()){
                    Toast.makeText(admin.this, "No name is entered", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Courses").child("Courses"+i).setValue(course);
                    i=i+1;
                }
            }
        });
        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(admin.this, "Detail of the course", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(admin.this,details.class));
//
            }
        });
    }
}