package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moodle.adapters.AdapterPdfUser;
import com.example.moodle.databinding.ActivityDashboardBinding;
import com.example.moodle.models.ModelPdf;
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
    DatabaseReference databaseReference;
    private ArrayList<ModelPdf> pdfArrayList;
    private ActivityDashboardBinding binding;
    private AdapterPdfUser adapterPdfUser;

    private static final String TAG="PDF_LIST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        User = findViewById(R.id.userSettings);
        About = findViewById(R.id.About);
        Logout = findViewById(R.id.Logout);

        loadPdfList();

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            adapterPdfUser.getFilter().filter(s);

                        }
                        catch (Exception e){
                            Log.d(TAG,"onTextChanged: "+e.getMessage());

                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
//        ArrayList<String> qwe=new ArrayList<String>();
//        ArrayAdapter<String> listView=new ArrayAdapter<>(Dashboard.this,R.layout.recycler_view,qwe);
//        list.setAdapter(listView);
//        DatabaseReference refernce= FirebaseDatabase.getInstance().getReference().child("Books");
//        refernce.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                qwe.clear();
//                for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                    qwe.add(snapshot1.getValue().toString());
//                }
//                listView.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void loadPdfList() {
        pdfArrayList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfArrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelPdf model=ds.getValue(ModelPdf.class);
                    pdfArrayList.add(model);
                    Log.d(TAG,"onDataChange: "+model.getId()+" "+model.getTitle());
                }
                adapterPdfUser=new AdapterPdfUser(com.example.moodle.Dashboard.this,pdfArrayList);
                binding.bookRv.setAdapter(adapterPdfUser);


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