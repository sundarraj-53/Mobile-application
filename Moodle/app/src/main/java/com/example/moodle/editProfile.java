package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class editProfile extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    Button submit;
    EditText name,regno,address,phoneno,department;
    LinearLayout home,Dashboard,User,About,Logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        name = findViewById(R.id.profileName);
        regno = findViewById(R.id.regEt);
        address = findViewById(R.id.addr);
        phoneno = findViewById(R.id.phoneNoEt);
        department = findViewById(R.id.DepartmentEt);
        submit = findViewById(R.id.editProfile);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        User = findViewById(R.id.userSettings);
        About = findViewById(R.id.About);
        Logout = findViewById(R.id.Logout);
        String Name=getIntent().getStringExtra("Name");
        String registerno=getIntent().getStringExtra("Regno");
        String Addressui=getIntent().getStringExtra("Address");
        String departmentui=getIntent().getStringExtra("Department");
        String PhoneNumber=getIntent().getStringExtra("Phoneno");
        name.setText(Name);
        regno.setText(registerno);
        address.setText(Addressui);
        phoneno.setText(PhoneNumber);
        department.setText(departmentui);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(editProfile.this, MainActivity.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(editProfile.this, Dashboard.class);
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(editProfile.this, user.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(editProfile.this, About.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(editProfile.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(editProfile.this, Login.class);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String user = name.getText().toString().trim();
                    String b = regno.getText().toString().trim();
                    long Regno = Long.parseLong(b);
                    String Address = address.getText().toString();
                    String c = phoneno.getText().toString().trim();
                    long PhoneNo = Long.parseLong(c);
                    String Department = department.getText().toString().trim();
                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("Name", "" + user);
                    hashmap.put("Register No", Regno);
                    hashmap.put("Address", "" + Address);
                    hashmap.put("Phone Number", PhoneNo);
                    hashmap.put("Department", Department);
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    String id = user1.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child("" + id)
                            .setValue(hashmap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(editProfile.this, "User Profile is Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(editProfile.this, "Failed to add" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
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