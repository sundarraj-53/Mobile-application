package com.example.moodle;

import static com.example.moodle.Constants.MAX_BYTES_PDF;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MyApplication extends Application{

    private static final String TAG_DOWNLOAD="DOWNLOAD_TAG";
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
    public static  void PdfSize(String pdfUrl, String pdfTitle,TextView sizeTv) {
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
    public static void PdfFromUrlSinglePage(String pdfUrl, String pdfTitle, PDFView pdfView, ProgressBar progressBar) {
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
    public static void incrementSyllabusViewCount(String bookId)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Courses");
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
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Courses");
                        reference.child(bookId)
                                .updateChildren(hashMap);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public static  void downloadSyllabus(Context context, String bookId, String bookTitle, String bookUrl){
        Log.d(TAG_DOWNLOAD,"DownloadBook:downloading book....");
        String nameWithExtension=bookTitle+".pdf";
        Log.d(TAG_DOWNLOAD,"downloadBook:Name: "+nameWithExtension);
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Downloading "+nameWithExtension +"...");
        progressDialog.setCanceledOnTouchOutside(false);
        StorageReference storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG_DOWNLOAD,"OnSuccess: Book Downloaded");
                        saveDownloadedSyllabus(context,progressDialog,bytes,nameWithExtension,bookId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_DOWNLOAD,"onFailure:Failed to download due to "+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed to download due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private static void saveDownloadedSyllabus(Context context, ProgressDialog progressDialog, byte[] bytes, String nameWithExtension, String bookId) {
        Log.d(TAG_DOWNLOAD,"saveDownloadedBook: Saving downloaded book...");
        try{
            File downloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            downloadFolder.mkdir();
            String filePath=downloadFolder.getPath() +"/" +nameWithExtension;
            FileOutputStream out=new FileOutputStream(filePath);
            out.write(bytes);
            out.close();
            Toast.makeText(context, "Saved to Download Folder", Toast.LENGTH_SHORT).show();
            Log.d(TAG_DOWNLOAD,"saveDownloadBook: Saved to Download Folder");
            progressDialog.dismiss();

            incrementSyllabusDownloadCount(bookId);
        }
        catch(Exception e){
            Log.d(TAG_DOWNLOAD,"savedDownloadBook: Failed saving to Download Folder due to "+e.getMessage());
            Toast.makeText(context, "Failed saving to Download Folder due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private static void incrementSyllabusDownloadCount(String bookId) {
        Log.d(TAG_DOWNLOAD,"incrementBookDownloadedCount: Increment Book Download Count");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Courses");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String downloadsCount=""+snapshot.child("downloadsCount").getValue();
                        Log.d(TAG_DOWNLOAD,"onDataChange: Downloads Count: "+downloadsCount);
                        if(downloadsCount.equals(" ")||downloadsCount.equals("null")){
                            downloadsCount="0";
                        }
                        long newDownloadsCount=Long.parseLong(downloadsCount) + 1;
                        Log.d(TAG_DOWNLOAD,"onDataChange: New Download Count: "+newDownloadsCount);
                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("downloadsCount",newDownloadsCount);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Courses");
                        reference.child(bookId).updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG_DOWNLOAD,"onSuccess:Downloads Count Updated...");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG_DOWNLOAD,"onFailure: Failed to update Downloads Count due to "+e.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
    public static  void downloadBook(Context context, String bookId, String bookTitle, String bookUrl){
        Log.d(TAG_DOWNLOAD,"DownloadBook:downloading book....");
        String nameWithExtension=bookTitle+".pdf";
        Log.d(TAG_DOWNLOAD,"downloadBook:Name: "+nameWithExtension);
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Downloading "+nameWithExtension +"...");
        progressDialog.setCanceledOnTouchOutside(false);
        StorageReference storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG_DOWNLOAD,"OnSuccess: Book Downloaded");
                        saveDownloadedBook(context,progressDialog,bytes,nameWithExtension,bookId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_DOWNLOAD,"onFailure:Failed to download due to "+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed to download due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private static void saveDownloadedBook(Context context, ProgressDialog progressDialog, byte[] bytes, String nameWithExtension, String bookId) {
        Log.d(TAG_DOWNLOAD,"saveDownloadedBook: Saving downloaded book...");
        try{
            File downloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            downloadFolder.mkdir();
            String filePath=downloadFolder.getPath() +"/" +nameWithExtension;
            FileOutputStream out=new FileOutputStream(filePath);
            out.write(bytes);
            out.close();
            Toast.makeText(context, "Saved to Download Folder", Toast.LENGTH_SHORT).show();
            Log.d(TAG_DOWNLOAD,"saveDownloadBook: Saved to Download Folder");
            progressDialog.dismiss();
            
            incrementBookDownloadCount(bookId);
        }
        catch(Exception e){
            Log.d(TAG_DOWNLOAD,"savedDownloadBook: Failed saving to Download Folder due to "+e.getMessage());
            Toast.makeText(context, "Failed saving to Download Folder due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private static void incrementBookDownloadCount(String bookId) {
            Log.d(TAG_DOWNLOAD,"incrementBookDownloadedCount: Increment Book Download Count");
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Books");
            ref.child(bookId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String downloadsCount=""+snapshot.child("downloadsCount").getValue();
                            Log.d(TAG_DOWNLOAD,"onDataChange: Downloads Count: "+downloadsCount);
                            if(downloadsCount.equals(" ")||downloadsCount.equals("null")){
                                downloadsCount="0";
                            }
                            long newDownloadsCount=Long.parseLong(downloadsCount) + 1;
                            Log.d(TAG_DOWNLOAD,"onDataChange: New Download Count: "+newDownloadsCount);
                            HashMap<String, Object> hashMap=new HashMap<>();
                            hashMap.put("downloadsCount",newDownloadsCount);
                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Books");
                            reference.child(bookId).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                                Log.d(TAG_DOWNLOAD,"onSuccess:Downloads Count Updated...");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG_DOWNLOAD,"onFailure: Failed to update Downloads Count due to "+e.getMessage());
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }

}
