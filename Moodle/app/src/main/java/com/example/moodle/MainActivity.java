package com.example.moodle;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodle.adapters.AdapterClass;
import com.example.moodle.adapters.AdapterPdfUser;
import com.example.moodle.databinding.ActivityDashboardBinding;
import com.example.moodle.databinding.ActivityMainBinding;
import com.example.moodle.models.ModelClass;
import com.example.moodle.models.ModelPdf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private ListView list;


    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home1,Dashboard1,User1,About1,Logout1;
    private RecyclerView recyclerView;
     TextView name;
     Button syllabus;
     DatabaseReference databaseReference;
     private ActivityMainBinding binding;
     private AdapterClass adapterClass;
        private ArrayList<ModelClass> qwe;
     public String getName="";
     public String getPass="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);
//        list = findViewById(R.id.listView);
//        recyclerView = findViewById(R.id.label);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home1 = findViewById(R.id.home);
        name=findViewById(R.id.mail);
        Dashboard1 = findViewById(R.id.dashboard);
        User1 = findViewById(R.id.userSettings);
        About1 = findViewById(R.id.About);
        Logout1 = findViewById(R.id.Logout);
        syllabus=findViewById(R.id.syllabus);
//        Intent intent = getIntent();
//        String getName = intent.getStringExtra("name");
        getName=getIntent().getStringExtra("name");
        getPass=getIntent().getStringExtra("password");
        System.out.println("getName"+getName);
        //Set Text
        name.setText(getName);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        String id = user.getUid();
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
                        reference.child(""+id)
                                .child("Token")
                                .setValue(token)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        // Log and toast
                    }
                });
        loadPdfList();
        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,innerCourse.class));
            }
        });
//        intent.putExtra("name", getName);
//        Intent intent=new Intent(MainActivity.this,user.class);
//        intent.putExtra("name", "getName");
//        startActivity(intent);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        Dashboard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Dashboard.class));
            }
        });
        User1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,user.class);
                intent.putExtra("name", getName);
                intent.putExtra("pass", getPass);
                startActivity(intent);

//                redirectActivity(MainActivity.this,user.class);
            }
        });
        About1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(MainActivity.this,About.class);
            }
        });
        Logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(MainActivity.this,Login.class);
            }
        });
//        ArrayList<ModelClass> qwe = new ArrayList<>();
//        DatabaseReference refernce = FirebaseDatabase.getInstance().getReference().child("Courses");
//        refernce.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                qwe.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    ModelClass model=snapshot1.getValue(ModelClass.class);
//                    qwe.add(model);
//                }
////                ArrayAdapter<String> listView = new ArrayAdapter<>(MainActivity.this, R.layout.activity_details2, qwe);
////                list.setAdapter(listView);
//                AdapterClass adapterClass=new AdapterClass(com.example.moodle.MainActivity.this,qwe);
//                binding.bookRv.setAdapter(adapterClass);
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "New course", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, innerCourse.class));
//            }
//        });

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
////                ArrayAdapter<String> listView = new ArrayAdapter<>(MainActivity.this, R.layout.activity_details2, qwe);
////                list.setAdapter(listView);
                adapterClass=new AdapterClass(com.example.moodle.MainActivity.this,qwe);
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
        public static void redirectActivity(Activity activity,Class secondActivity) {
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


//        tv1=findViewById(R.id.home);
//        tv2=findViewById(R.id.dashboard);
//        tv3=findViewById(R.id.user);
//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,MainActivity.class));
//                Toast.makeText(MainActivity.this, "Home page", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        tv2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,Dashboard.class));
//                Toast.makeText(MainActivity.this, "Dashboard page", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        tv3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,user.class));
//                Toast.makeText(MainActivity.this, "user page", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }