package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodle.databinding.ActivityUserBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;

    TextView getName,getPass,profileEmail;
    ActivityUserBinding binding;
    FirebaseUser firebaseEmail;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Button edit;
    TextView DbName,regno,address,Department,phoneno;
    LinearLayout home,Dashboard,User,About,Logout;
    String Name1,Regno1,Address1,department1,PhoneNo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        edit=findViewById(R.id.editProfile);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        User = findViewById(R.id.userSettings);
        About = findViewById(R.id.About);
        Logout = findViewById(R.id.Logout);
//        getName=findViewById(R.id.titleUsername);
        getPass=findViewById(R.id.passEdt);
        profileEmail=findViewById(R.id.profileEmail);
        DbName=findViewById(R.id.profileName);
        regno=findViewById(R.id.regNoTv);
        address=findViewById(R.id.AddTv);
        Department=findViewById(R.id.departmentTv);
        phoneno=findViewById(R.id.phnoTv);
        String name = getIntent().getStringExtra("name");
        String pass=getIntent().getStringExtra("pass");
        System.out.println("UserActivity"+name);
        System.out.println("UserActivityPass"+pass);
//        getName.setText(name);
        getPass.setText(pass);
        profileEmail.setText(name);
        FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
//        String id=user1.getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(user1.getUid());
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 System.out.println("Snapshot Value"+snapshot);
                 Toast.makeText(user.this, ""+snapshot, Toast.LENGTH_SHORT).show();
                  Name1=snapshot.child("Name").getValue().toString();
                 Regno1=snapshot.child("Register No").getValue().toString();
                Address1=snapshot.child("Address").getValue().toString();
                 department1=snapshot.child("Department").getValue().toString();
                 PhoneNo1=snapshot.child("Phone Number").getValue().toString();
                 DbName.setText(Name1);
                 regno.setText(Regno1);
                 address.setText(Address1);
                 Department.setText(department1);
                 phoneno.setText(PhoneNo1);

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(user.this,editProfile.class);
                intent.putExtra("name",name);
                intent.putExtra("pass",pass);
                startActivity(intent);

            }
        });

        binding.accountStatusTv.setText("N/A");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseEmail=FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        loadUserInfo();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user.this,MainActivity.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(user.this,Dashboard.class);
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(user.this,user.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(user.this,About.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(user.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(user.this,Login.class);
            }
        });
        binding.accountStatusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseEmail.isEmailVerified()){
                    Toast.makeText(user.this, "Already verified...", Toast.LENGTH_SHORT).show();

                }
                else{
                    emailVerificationDialog();
                }
            }
        });
    }

    private void loadUserInfo() {
        if(firebaseEmail.isEmailVerified()){
                    binding.accountStatusTv.setText("Verified");
        }
        else{
            binding.accountStatusTv.setText("Not Verified");
        }
    }

    private void emailVerificationDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("verify Email")
                .setMessage("Are you sure!you want to send email verification instructions to your email "+firebaseEmail.getEmail())
        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        sendEmailVerification();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                    }
                })
                .show();
    }

    private void sendEmailVerification() {

        progressDialog.setMessage("Sending email verification instructions to your email "+firebaseEmail.getEmail());
        progressDialog.show();
        firebaseEmail.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(user.this, "Instruction send check your email "+firebaseEmail.getEmail(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(user.this, "Failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
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