package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class courseView extends AppCompatActivity {
    private FirebaseAuth auth;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,About,Dashboard,Logout;
    DatabaseReference databaseReference;
    EditText search;
    View itemview;
    private ArrayList<ModelCategory> categoryArrayList;
    private adapterCategory adaptercategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        search=findViewById(R.id.searchbtn);
        Logout = findViewById(R.id.Logout);
        About = findViewById(R.id.About);
        auth=FirebaseAuth.getInstance();
        loadCategories();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(courseView.this,admin.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(courseView.this,details.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(courseView.this,addbook.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(courseView.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(courseView.this,Login.class);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adaptercategory.getFilter().filter(s);
                }
                catch (Exception e){

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadCategories() {
        categoryArrayList=new ArrayList<>();
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    categoryArrayList.clear();
//                    for(DataSnapshot snapshot1:snapshot.getChildren()){
//                        ModelCategory model=snapshot1.getValue(ModelCategory.class);
//                        categoryArrayList.add(model);
//                    }
//                categoryArrayList.clear();
//                for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                    categoryArrayList.add(snapshot1.getValue(ModelCategory.class));
//                }
                if(snapshot.exists()){
                    categoryArrayList.clear();
                    ModelCategory model=snapshot.getValue(ModelCategory.class);
                    categoryArrayList.add(model);
                }
                    adaptercategory=new adapterCategory(courseView.this,categoryArrayList);
                    RecyclerView recyclerView =findViewById(R.id.categoryRv);
                    recyclerView.setAdapter(adaptercategory);
                

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