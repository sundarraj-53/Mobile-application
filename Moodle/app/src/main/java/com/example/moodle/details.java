package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moodle.adapters.AdapterClass;
import com.example.moodle.databinding.ActivityDetailsBinding;
import com.example.moodle.models.ModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class details extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    private ActivityDetailsBinding binding;
    private AdapterClass adapterClass;
    private ArrayList<ModelClass> qwe;
    LinearLayout home,About,Dashboard,Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        Logout = findViewById(R.id.Logout);
        About = findViewById(R.id.About);
        loadPdfList();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(details.this,admin.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectActivity(details.this,details.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectActivity(details.this,addbook.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(details.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(details.this,Login.class);
            }
        });
    }
    private void loadPdfList() {
        qwe=new ArrayList<>();
        DatabaseReference refernce = FirebaseDatabase.getInstance().getReference().child("Courses");
        refernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qwe.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    System.out.println("snapshot"+snapshot);
                    ModelClass model=snapshot1.getValue(ModelClass.class);
                    System.out.println("snapshot1"+snapshot1);
                    System.out.println("model"+model);
                    qwe.add(model);
                }
                adapterClass=new AdapterClass(com.example.moodle.details.this,qwe);
                System.out.println(adapterClass);
                binding.label.setAdapter(adapterClass);

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