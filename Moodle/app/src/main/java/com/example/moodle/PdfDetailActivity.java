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

import com.example.moodle.databinding.ActivityPdfDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PdfDetailActivity extends AppCompatActivity {

    private ActivityPdfDetailBinding binding;
    String bookId,bookTitle,bookUrl;
    private static final String TAG_DOWNLOAD="DOWNLOAD_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        bookId=intent.getStringExtra("bookId");
        binding.downloadBookBtn.setVisibility(View.GONE);
        loadBookDetails();
        MyApplication.incrementBookViewCount(bookId);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PdfDetailActivity.this,PdfViewActivity.class);
                intent1.putExtra("bookId",bookId);
                startActivity(intent1);
            }
        });
        binding.downloadBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG_DOWNLOAD,"onCLick: Checking permission");
                if(ContextCompat.checkSelfPermission(PdfDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG_DOWNLOAD,"onClick:Permission already granted,can download Book");
                            MyApplication.downloadBook(PdfDetailActivity.this,""+bookId,""+bookTitle,""+bookUrl);
                }
                else{
                    Log.d(TAG_DOWNLOAD,"onClick:Permission was not granted request permission...");
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
               if(isGranted){
                   Log.d(TAG_DOWNLOAD,"Permission Granted");
                   MyApplication.downloadBook(this,""+bookId,""+bookTitle,""+bookUrl);

               }
               else{
                   Log.d(TAG_DOWNLOAD,"Permission Denied...");
                   Toast.makeText(this, "Permission are denied..", Toast.LENGTH_SHORT).show();
               }
            });

    private void loadBookDetails() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("SNAPSHOT:"+snapshot);

                bookTitle=""+snapshot.child("title").getValue();
                System.out.println("PDFDETAILACTIVITYTITLE"+bookTitle);
                String description=""+snapshot.child("Description").getValue();
                System.out.println("PDFDETAILACTIVITYDECRIPTION"+description);
                String viewsCount=""+snapshot.child("viewsCount").getValue();
                System.out.println("PDFDETAILACTIVITYVIEWSCOUNT"+viewsCount);
                String downloadsCount=""+snapshot.child("downloadsCount").getValue();
                System.out.println("PDFDETAILACTIVITYDOWNLOADSCOUNT"+downloadsCount);
                String  timestamp =""+snapshot.child("timestamp").getValue();
                System.out.println("PDFDETAILACTIVITYTIMESTAMP"+timestamp);
                bookUrl=""+snapshot.child("url").getValue();
                System.out.println("PDFDETAILACTIVITYURL"+bookUrl);
                binding.downloadBookBtn.setVisibility(View.VISIBLE);
//                String url=""+snapshot.child("url").getValue();
//                String timestamp =""+snapshot.child("timestamp").getValue();
//                long timestamp= Long.parseLong(""+snapshot.child("timestamp").getValue());
//                System.out.println("PDFDETAILACTIVITY"+timestamp);
//                System.out.println("PDFDETAILACTIVITYURL"+url);
                String date=MyApplication.formatTimestamp(Long.parseLong(timestamp));
                MyApplication.PdfFromUrlSinglePage(
                        ""+bookUrl,
                        ""+bookTitle,
                        binding.pdfView,
                        binding.progressBar
                );
                MyApplication.PdfSize(
                        ""+bookUrl,
                        ""+bookTitle,
                        binding.sizeTv
                );

                binding.titleTv.setText(bookTitle);
                binding.descriptionTv.setText(description);
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