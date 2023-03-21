package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Dashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,Dashboard,User,About,Logout;
    ListView list;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        list=findViewById(R.id.list);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        User = findViewById(R.id.userSettings);
        About = findViewById(R.id.About);
        Logout = findViewById(R.id.Logout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,MainActivity.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Dashboard.this,Dashboard.class);
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Dashboard.this,user.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Dashboard.this,About.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Dashboard.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(Dashboard.this,Login.class);
            }
        });
        ArrayList<String> qwe=new ArrayList<String>();
        ArrayAdapter<String> listView=new ArrayAdapter<>(Dashboard.this,R.layout.activity_details2,qwe);
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