package com.example.moodle.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodle.MainActivity;
import com.example.moodle.innerCourse;
import com.example.moodle.databinding.ActivityDetails2Binding;
import com.example.moodle.models.ModelClass;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.HolderClass> {
    private ActivityDetails2Binding binding;
    private Context context;
    View itemView;
    public ArrayList<ModelClass> qwe;

    public AdapterClass(Context context,ArrayList<ModelClass> qwe) {
//        this.binding = binding;
        this.context = context;
        this.qwe = qwe;
    }

    @NonNull
    @Override
    public HolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ActivityDetails2Binding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderClass(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.HolderClass holder, int position) {
        ModelClass model=qwe.get(position);
        String courses=model.getCourses();
        System.out.println("Courses"+courses);

        holder.label.setText(courses);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, innerCourse.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return qwe.size();
    }

    class HolderClass extends RecyclerView.ViewHolder{
        TextView label;

        public HolderClass(@NonNull View itemView) {
            super(itemView);
            label=binding.label;
        }
    }
}
