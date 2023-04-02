package com.example.moodle.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodle.MyApplication;
import com.example.moodle.SyllabusDetailsActivity;
import com.example.moodle.databinding.SyllabusListBinding;
import com.example.moodle.filters.FilterSyllabus;
import com.example.moodle.models.ModelSyllabus;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterSyllabus extends RecyclerView.Adapter<AdapterSyllabus.HolderClass> implements Filterable {
    private SyllabusListBinding binding;
    private Context context;
    View itemview;
    FilterSyllabus filter;
    public ArrayList<ModelSyllabus> syllabusArrayList,filterList;

    public AdapterSyllabus(Context context, ArrayList<ModelSyllabus> syllabusArrayList) {
        this.context = context;
        this.syllabusArrayList = syllabusArrayList;

    }

    @NonNull
    @Override
    public AdapterSyllabus.HolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=SyllabusListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderClass(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSyllabus.HolderClass holder, int position) {
        ModelSyllabus model=syllabusArrayList.get(position);
        String pdfId=model.getTimestamp();
        String title=model.getCourses();
        String pdfUrl=model.getUrl();
        String timestamp=model.getTimestamp();

        String formattedDate= MyApplication.formatTimestamp(Long.parseLong(timestamp));
        holder.titleTv.setText(title);
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
                Intent intent=new Intent(context, SyllabusDetailsActivity.class);
                intent.putExtra("syllabusId",pdfId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return syllabusArrayList.size();
    }
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new FilterSyllabus(filterList,this);
        }
        return filter;
    }

  class HolderClass extends RecyclerView.ViewHolder {

      PDFView pdfView;
      ProgressBar progressBar;
      TextView titleTv,sizeTv,dateTv;

      public HolderClass(@NonNull View itemView) {
          super(itemView);
          pdfView=binding.pdfView;
          progressBar=binding.progressBar;
          titleTv=binding.titleTv;
          sizeTv=binding.sizeTv;
          dateTv=binding.dateTv;
      }

    }
}
