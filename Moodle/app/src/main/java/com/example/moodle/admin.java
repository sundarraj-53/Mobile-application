package com.example.moodle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private Button Submit;
    private EditText Newcourse;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,Dashboard,About,Logout;
    ListView list;
    DatabaseReference databaseReference;
//    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Newcourse=findViewById(R.id.newcourse);
//        list=findViewById(R.id.list);
        Submit=findViewById(R.id.submit);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        Logout = findViewById(R.id.Logout);
        About = findViewById(R.id.About);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin.this,admin.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin.this,details.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(admin.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(admin.this,Login.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin.this,addbook.class);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                int c=1;
                String course=Newcourse.getText().toString();
                if(course.isEmpty()){
                    Toast.makeText(admin.this, "No name is entered", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Courses").child(course).setValue(course);
                    FirebaseDatabase.getInstance().getReference().child("CourseBooks").child(course).setValue(course);
                    Toast.makeText(admin.this, "Course is Added", Toast.LENGTH_SHORT).show();
//                    FirebaseDatabase.getInstance().getReference().child("Courses").child("Course"+i).setValue(course);
//                    FirebaseDatabase.getInstance().getReference().child("CourseBooks").child("Books"+i).setValue(course);
//                    Toast.makeText(admin.this, "Course is Added", Toast.LENGTH_SHORT).show();
//                    i=i+1;
                }
            }
        });
    }
    public static void openDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent=new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }

    }