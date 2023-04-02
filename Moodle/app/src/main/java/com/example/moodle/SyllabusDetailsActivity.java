package com.example.moodle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.moodle.databinding.ActivitySyllabusDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SyllabusDetailsActivity extends AppCompatActivity {

    private ActivitySyllabusDetailsBinding binding;
    String bookId,bookTitle,bookUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySyllabusDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        bookId=intent.getStringExtra("syllabusId");
        binding.downloadBookBtn.setVisibility(View.GONE);
        loadBookDetails();
        MyApplication.incrementSyllabusViewCount(bookId);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(SyllabusDetailsActivity.this,SyllabusViewActivity.class);
                intent1.putExtra("bookId",bookId);
                startActivity(intent1);
            }
        });
        binding.downloadBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(SyllabusDetailsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    MyApplication.downloadSyllabus(SyllabusDetailsActivity.this,""+bookId,""+bookTitle,""+bookUrl);
                }
                else{
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    MyApplication.downloadSyllabus(this,""+bookId,""+bookTitle,""+bookUrl);

                }
                else{
                    Toast.makeText(this, "Permission are denied..", Toast.LENGTH_SHORT).show();
                }
            });

    private void loadBookDetails() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Courses");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println("SNAPSHOT:"+snapshot);

                        bookTitle=""+snapshot.child("Courses").getValue();
                        System.out.println("PDFDETAILACTIVITYTITLE"+bookTitle);
                        String viewsCount=""+snapshot.child("viewsCount").getValue();
                        System.out.println("PDFDETAILACTIVITYVIEWSCOUNT"+viewsCount);
                        String downloadsCount=""+snapshot.child("downloadsCount").getValue();
                        System.out.println("PDFDETAILACTIVITYDOWNLOADSCOUNT"+downloadsCount);
                        String  timestamp =""+snapshot.child("timestamp").getValue();
                        System.out.println("PDFDETAILACTIVITYTIMESTAMP"+timestamp);
                        bookUrl=""+snapshot.child("url").getValue();
                        System.out.println("PDFDETAILACTIVITYURL"+bookUrl);
                        binding.downloadBookBtn.setVisibility(View.VISIBLE);
                        long a=Long.parseLong(timestamp);
                        String date=MyApplication.formatTimestamp(a);
                        MyApplication.loadPdfFromUrlSinglePage(
                                ""+bookUrl,
                                ""+bookTitle,
                                binding.pdfView,
                                binding.progressBar
                        );
                        MyApplication.loadPdfSize(
                                ""+bookUrl,
                                ""+bookTitle,
                                binding.sizeTv
                        );

                        binding.titleTv.setText(bookTitle);
                        binding.viewTv.setText(viewsCount.replace("null","N/A"));
                        binding.downloadsTv.setText(downloadsCount.replace("null","N/A"));
                        binding.dateTv.setText(date);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}