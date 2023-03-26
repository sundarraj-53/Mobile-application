package com.example.moodle;

import static com.example.moodle.Constants.MAX_BYTES_PDF;

import android.app.Application;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moodle.adapters.AdapterPdfUser;
import com.example.moodle.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static final String formatTimestamp(long timestamp){
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date= DateFormat.format("dd/MM/yyyy",cal).toString();
        return date;
    }
    public static  void loadPdfSize(String pdfUrl, String pdfTitle,TextView sizeTv) {
        String TAG="PDF_SIZE_TAG";
        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata()
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        double bytes=storageMetadata.getSizeBytes();
                        Log.d(TAG,"onSuccess "+pdfTitle+""+bytes);
                        double kb=bytes/1024;
                        double mb=kb/1024;
                        if(mb>=1){
                            sizeTv.setText(String.format("%.2f", mb)+" MB");
                        } else if (kb >= 1) {
                            sizeTv.setText(String.format("%.2f", kb)+" KB");
                        }
                        else{
                            sizeTv.setText(String.format("%.2f", bytes)+" bytes");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MyApplicat, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"onFailure "+e.getMessage());
                    }
                });

    }
    public static void loadPdfFromUrlSinglePage(String pdfUrl, String pdfTitle, PDFView pdfView, ProgressBar progressBar) {
        final String TAG="PDF_LOAD_SINGLE_TAG";
        System.out.println(pdfUrl);
        StorageReference ref=FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        System.out.println("Stroage Refernce"+ref);
        ref.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG,"onSuccess:"+pdfTitle+"Successfully got the file");
                        pdfView.fromBytes(bytes)
                                .pages(0)
                                .spacing(0)
                                .swipeHorizontal(false)
                                .enableSwipe(false)
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                       progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG,"onError: "+t.getMessage());

                                    }
                                })
                                .onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG,"onPageError: "+t.getMessage());
                                    }
                                })
                                .onLoad(new OnLoadCompleteListener() {
                                    @Override
                                    public void loadComplete(int nbPages) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG,"LoadComplete: pdf loaded");

                                    }
                                })
                                .load();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.d(TAG,"OnFailure:failed to getting the url due to "+e.getMessage());
                    }
                });
    }
    public static void incrementBookViewCount(String bookId)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String viewsCount=""+snapshot.child("viewsCount").getValue();
                        if(viewsCount.equals("")||viewsCount.equals(null)){
                            viewsCount="0";
                        }
                        long newViewsCount= Long.parseLong(viewsCount)+1;
                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("viewsCount",newViewsCount);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Books");
                        reference.child(bookId)
                                .updateChildren(hashMap);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
