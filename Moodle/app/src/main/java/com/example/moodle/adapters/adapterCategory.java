package com.example.moodle.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodle.filters.FilterCategory;
import com.example.moodle.models.ModelCategory;
import com.example.moodle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class adapterCategory extends RecyclerView.Adapter<adapterCategory.HolderCategory> implements Filterable {
    private Context context;
    public ArrayList<ModelCategory> categoryArrayList,filterList;
    private View itemView;

    private FilterCategory filterCategory;

    public adapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.filterList=categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView=LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
        return new HolderCategory(itemView.getRootView());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        ModelCategory modelCategory=categoryArrayList.get(position);
        String id=modelCategory.getId();
        String Category=modelCategory.getCategory();
        String Uid=modelCategory.getUid();
        String Timestamp=modelCategory.getTimestamp();
//        System.out.println("Categories",+category);
        System.out.println("Holder"+holder);



       holder.categoryTv.setText(Category);
//            holder.categoryTitle.setText(category);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are You sure you want to delete this category ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                deleteCategory(modelCategory, holder);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void deleteCategory(ModelCategory modelCategory, HolderCategory holder) {
        String id= modelCategory.getId();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {

        return categoryArrayList.size();
    }

    @Override
    public  Filter getFilter() {
        if(filterCategory==null){
            filterCategory=new FilterCategory(filterList,this);
        }
        return filterCategory;
    }

    class HolderCategory extends RecyclerView.ViewHolder{
        private TextView categoryTv;
        private ImageButton delete;


        public HolderCategory(@NonNull View itemView) {

            super(itemView);
            categoryTv=itemView.findViewById(R.id.Categories);
            delete=itemView.findViewById(R.id.deletebtn);

        }
    }
}
