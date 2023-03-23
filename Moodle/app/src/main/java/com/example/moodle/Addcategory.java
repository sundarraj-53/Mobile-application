package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Addcategory extends AppCompatActivity {
    private FirebaseAuth auth;
    DrawerLayout drawerLayout;
    ImageView menu;
    Button categorySubmit,categoryView;
    LinearLayout home,About,Dashboard,Logout;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcategory);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        Logout = findViewById(R.id.Logout);
        About = findViewById(R.id.About);
        auth=FirebaseAuth.getInstance();
        categoryView=findViewById(R.id.categoryView);
        categorySubmit=findViewById(R.id.categoryButton);
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Addcategory.this,courseView.class));
            }
        });
        categorySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Addcategory.this,admin.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Addcategory.this,details.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Addcategory.this,addbook.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Addcategory.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(Addcategory.this,Login.class);
            }
        });
    }

    private String Category="";

    private void validateData() {
        EditText categoryTitle=(EditText) findViewById(R.id.categoryTitle);
         Category=categoryTitle.getText().toString().trim();
        if(Category.isEmpty()){
            Toast.makeText(this, "Please enter the category", Toast.LENGTH_SHORT).show();
        }
        else{
             addCategoryFirebase();
        }

    }

    private void addCategoryFirebase()
    {
        long Timestamp=System.currentTimeMillis();
        HashMap<String,Object> hashmap=new HashMap<>();
        hashmap.put("id",""+Timestamp);
        hashmap.put("Category",""+Category);
        hashmap.put("timestamp", Timestamp);
        hashmap.put("Uid",""+auth.getUid());

        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference1.child(""+Timestamp)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Addcategory.this, "Category added Successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Addcategory.this, "Failed to add"+e.getMessage(), Toast.LENGTH_SHORT).show();
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