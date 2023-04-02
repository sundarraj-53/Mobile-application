package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class admin extends AppCompatActivity {

    private Button Submit;
    private EditText Newcourse;
    DrawerLayout drawerLayout;
    ImageView menu;
    Button AddNewCategory;
    private Uri pdfuri=null;
    LinearLayout home,Dashboard,About,Logout;
    ListView list;
    DatabaseReference databaseReference;
    Button attach;
//    private ListView list;
    private static final int pickcode=1000;
     long Timestamp=0;

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
        attach=findViewById(R.id.pin);
        AddNewCategory=findViewById(R.id.NewCategory);

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdfPickIntent();
            }
        });

        AddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin.this,Addcategory.class));
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
                validateData();
//                int c=1;
//                String course=Newcourse.getText().toString();
//                if(course.isEmpty()){
//                    Toast.makeText(admin.this, "No name is entered", Toast.LENGTH_SHORT).show();
//                }
//                else{
////                    Timestamp=System.currentTimeMillis();
////                    HashMap<String, Object> hashMap = new HashMap<>();
////                    hashMap.put("id", "" + Timestamp);
////                    hashMap.put("Course",""+course);
////                    FirebaseDatabase.getInstance().getReference().child("Courses")
////                            .child(""+Timestamp)
////                                    .setValue(hashMap);
//
//                    FirebaseDatabase.getInstance().getReference().child("Courses").child(course).setValue(course);
////                    FirebaseDatabase.getInstance().getReference().child("CourseBooks").child(course).setValue(course);
//                    Toast.makeText(admin.this, "Course is Added", Toast.LENGTH_SHORT).show();
////                    FirebaseDatabase.getInstance().getReference().child("Courses").child("Course"+i).setValue(course);
////                    FirebaseDatabase.getInstance().getReference().child("CourseBooks").child("Books"+i).setValue(course);
////                    Toast.makeText(admin.this, "Course is Added", Toast.LENGTH_SHORT).show();
////                    i=i+1;
//                }
            }
        });
    }
    public String NewCourse="";
    private void validateData() {
        EditText NewCourse1=(EditText) findViewById(R.id.newcourse);
        NewCourse=NewCourse1.getText().toString().trim();
        if(NewCourse.isEmpty()){
            Toast.makeText(this, "Enter the title", Toast.LENGTH_SHORT).show();
        }
        else if(pdfuri==null){
            Toast.makeText(this, "pick the pdf", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadPDF();
        }
    }
    private void uploadPDF() {
        long Timsestamp=System.currentTimeMillis();
        String filePathName="Syllabus/" +Timsestamp;
        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathName);
        storageReference.putFile(pdfuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String Uploadpdf=""+uriTask.getResult();

                        uploadPDFToDB(Uploadpdf,Timsestamp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(admin.this, "pdf is failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void uploadPDFToDB(String uploadpdf, long timestamp) {

//        FirebaseDatabase.getInstance().getReference("Courses").setValue(timestamp);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Courses",""+NewCourse);
        hashMap.put("url", "" +uploadpdf);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("viewsCount", 0);
        hashMap.put("downloadsCount", 0);

//        databaseReference.child("" + timestamp)
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        databaseReference.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(admin.this, "Successfully pdf is uploaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin.this, "Failed to upload the pdf" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(admin.this, "Failed to upload the pdf" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
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
    private void pdfPickIntent() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent,"Choose pdf"),pickcode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==pickcode){
                pdfuri=data.getData();
                Toast.makeText(this, "Pdf is picked", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "cancelled picking pdf", Toast.LENGTH_SHORT).show();

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