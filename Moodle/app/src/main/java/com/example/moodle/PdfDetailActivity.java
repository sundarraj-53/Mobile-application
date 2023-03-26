package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moodle.databinding.ActivityPdfDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PdfDetailActivity extends AppCompatActivity {

    private ActivityPdfDetailBinding binding;
    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        bookId=intent.getStringExtra("bookId");
        loadBookDetails();
        MyApplication.incrementBookViewCount(bookId);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadBookDetails() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("SNAPSHOT:"+snapshot);

                String title=""+snapshot.child("title").getValue();
                System.out.println("PDFDETAILACTIVITYTITLE"+title);
                String description=""+snapshot.child("Description").getValue();
                System.out.println("PDFDETAILACTIVITYDECRIPTION"+description);
                String viewsCount=""+snapshot.child("viewsCount").getValue();
                System.out.println("PDFDETAILACTIVITYVIEWSCOUNT"+viewsCount);
                String downloadsCount=""+snapshot.child("downloadsCount").getValue();
                System.out.println("PDFDETAILACTIVITYDOWNLOADSCOUNT"+downloadsCount);
                String  timestamp =""+snapshot.child("timestamp").getValue();
                System.out.println("PDFDETAILACTIVITYTIMESTAMP"+timestamp);
                String url=""+snapshot.child("url").getValue();
                System.out.println("PDFDETAILACTIVITYURL"+url);
//                String url=""+snapshot.child("url").getValue();
//                String timestamp =""+snapshot.child("timestamp").getValue();
//                long timestamp= Long.parseLong(""+snapshot.child("timestamp").getValue());
//                System.out.println("PDFDETAILACTIVITY"+timestamp);
//                System.out.println("PDFDETAILACTIVITYURL"+url);
                String date=MyApplication.formatTimestamp(Long.parseLong(timestamp));
                MyApplication.loadPdfFromUrlSinglePage(
                        ""+url,
                        ""+title,
                        binding.pdfView,
                        binding.progressBar
                );
                MyApplication.loadPdfSize(
                        ""+url,
                        ""+title,
                        binding.sizeTv
                );

                binding.titleTv.setText(title);
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