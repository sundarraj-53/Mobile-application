package com.example.moodle.adapters;

import static com.example.moodle.Constants.MAX_BYTES_PDF;

import android.content.Context;
import android.content.Intent;
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
import com.example.moodle.PdfDetailActivity;
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
        String pdfId=model.getId();
        String title=model.getTitle();
        String description=model.getDescription();
        String pdfUrl=model.getUrl();
        long timestamp=model.getTimestamp();

        String formattedDate= MyApplication.formatTimestamp(timestamp);
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);

        MyApplication.loadPdfFromUrlSinglePage(
                ""+pdfUrl,
                ""+title,
                holder.pdfView,
                holder.progressBar
        );
        MyApplication.loadPdfSize(
                ""+pdfUrl,
                ""+title,
                holder.sizeTv
        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PdfDetailActivity.class);
                intent.putExtra("bookId",pdfId);
                context.startActivity(intent);
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
