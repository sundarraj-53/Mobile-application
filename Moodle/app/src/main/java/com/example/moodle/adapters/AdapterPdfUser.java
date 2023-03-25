package com.example.moodle.adapters;

import static com.example.moodle.Constants.MAX_BYTES_PDF;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodle.MyApplication;
import com.example.moodle.databinding.RowPdfDashboardBinding;
import com.example.moodle.filters.FilterPdfuser;
import com.example.moodle.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterPdfUser extends RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser> implements Filterable {

    private RowPdfDashboardBinding binding;
    private Context context;
    private static final String TAG="PDF_ADAPTER_TAG";
    View itemview;

    private FilterPdfuser filter;
    public ArrayList<ModelPdf> pdfArrayList,filterList;

    public AdapterPdfUser(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList=pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=RowPdfDashboardBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfUser holder, int position) {
        ModelPdf model=pdfArrayList.get(position);
        String title=model.getTitle();
        String description=model.getDescription();
        long timestamp=model.getTimestamp();

        String formattedDate= MyApplication.formatTimestamp(timestamp);
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);

        loadPdfFromUrl(model,holder);
        loadPdfSize(model,holder);
    }

    private void loadPdfSize(ModelPdf model, HolderPdfUser holder) {
        String pdfUrl=model.getUrl();
        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata()
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        double bytes=storageMetadata.getSizeBytes();
                        Log.d(TAG,"onSuccess "+model.getTitle()+""+bytes);
                        double kb=bytes/1024;
                        double mb=kb/1024;
                        if(mb>=1){
                            holder.sizeTv.setText(String.format("%.2f", mb)+" MB");
                        } else if (kb >= 1) {
                            holder.sizeTv.setText(String.format("%.2f", kb)+" KB");
                        }
                        else{
                            holder.sizeTv.setText(String.format("%.2f", bytes)+" bytes");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"onFailure "+e.getMessage());
                    }
                });

    }

    private void loadPdfFromUrl(ModelPdf model, HolderPdfUser holder) {
        String pdfUrl=model.getUrl();
        StorageReference ref=FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG,"onSuccess:"+model.getTitle()+"Successfully got the file");
                        holder.pdfView.fromBytes(bytes)
                                .pages(0)
                                    .spacing(0)
                                        .swipeHorizontal(false)
                                            .enableSwipe(false)
                                                    .onError(new OnErrorListener() {
                                                        @Override
                                                        public void onError(Throwable t) {
                                                                    holder.progressBar.setVisibility(View.INVISIBLE);
                                                                Log.d(TAG,"onError: "+t.getMessage());

                                                        }
                                                    })
                                                    .onPageError(new OnPageErrorListener() {
                                                        @Override
                                                        public void onPageError(int page, Throwable t) {
                                                            holder.progressBar.setVisibility(View.INVISIBLE);
                                                                Log.d(TAG,"onPageError: "+t.getMessage());
                                                        }
                                                    })
                                                        .onLoad(new OnLoadCompleteListener() {
                                                            @Override
                                                            public void loadComplete(int nbPages) {
                                                                holder.progressBar.setVisibility(View.INVISIBLE);
                                                                Log.d(TAG,"LoadComplete: pdf loaded");

                                                            }
                                                        })
                                                        .load();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.progressBar.setVisibility(View.INVISIBLE);

                        Log.d(TAG,"OnFailure:failed to getting the url due to "+e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new FilterPdfuser(filterList,this);
        }
        return filter;
    }

    class HolderPdfUser extends RecyclerView.ViewHolder{


        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv,descriptionTv,sizeTv,dateTv;
        ImageButton morebtn;
        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);

            pdfView=binding.pdfView;
            progressBar=binding.progressBar;
            titleTv=binding.titleTv;
            descriptionTv=binding.descriptionTv;
            sizeTv=binding.sizeTv;
            dateTv=binding.dateTv;
            morebtn=binding.morebtn;
        }
    }
}
