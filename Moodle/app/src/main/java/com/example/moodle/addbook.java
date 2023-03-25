package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class addbook extends AppCompatActivity {

    private FirebaseAuth auth;
    DrawerLayout drawerLayout;
    ImageView menu;
    Button attach;
    private Uri pdfuri=null;
    private Button upload;
    private ArrayList<String> categoryArrayList;
    LinearLayout home,About,Dashboard,Logout;
    DatabaseReference databaseReference;
    private static final String tag="ADD_PDF_TAG";
    private static final int pickcode=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        Dashboard = findViewById(R.id.dashboard);
        Logout = findViewById(R.id.Logout);
        About = findViewById(R.id.About);
        attach=findViewById(R.id.pin);
        auth=FirebaseAuth.getInstance();
        upload=findViewById(R.id.Upload);

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdfPickIntent();
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
                startActivity(new Intent(addbook.this,admin.class));
            }
        });
        Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(addbook.this,details.class);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(addbook.this,addbook.class);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(addbook.this, "Signed Out", Toast.LENGTH_SHORT).show();
                redirectActivity(addbook.this,Login.class);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    private String title="",description="";
    private void validateData() {
        EditText title1=(EditText) findViewById(R.id.EditText);
        EditText description1=findViewById(R.id.bgDescription);
        title=title1.getText().toString().trim();
        description=description1.getText().toString().trim();
        if(title.isEmpty()){
            Toast.makeText(this, "Enter the title", Toast.LENGTH_SHORT).show();
        }
        else if(description.isEmpty()){
            Toast.makeText(this, "Enter the description", Toast.LENGTH_SHORT).show();
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
        String filePathName="Moodle/" +Timsestamp;
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
                        Toast.makeText(addbook.this, "pdf is failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
        });
    }

    private void uploadPDFToDB(String uploadpdf, long timestamp) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("title", "" + title);
        hashMap.put("Description", "" + description);
        hashMap.put("url", "" + uploadpdf);
        hashMap.put("timestamp", "" + timestamp);

        databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child("" + timestamp)

                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addbook.this, "Successfully pdf is uploaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(addbook.this, "Failed to upload the pdf"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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