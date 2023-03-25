package com.example.moodle.filters;

import android.widget.Filter;

import com.example.moodle.adapters.AdapterPdfUser;
import com.example.moodle.adapters.adapterCategory;
import com.example.moodle.models.ModelCategory;
import com.example.moodle.models.ModelPdf;

import java.util.ArrayList;

public class FilterPdfuser extends Filter {
    ArrayList<ModelPdf> filterList;
    AdapterPdfUser adapterPdfUser;

    public FilterPdfuser(ArrayList<ModelPdf> filterList, com.example.moodle.adapters.AdapterPdfUser adapterPdfUser) {
        this.filterList = filterList;
        this.adapterPdfUser = adapterPdfUser;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null && constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<ModelPdf> filteredModels=new ArrayList<>();
            for(int i=0;i<filterList.size();i++){
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint)){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count=filteredModels.size();
            results.values=filteredModels;
        }
        else{
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterPdfUser.pdfArrayList=(ArrayList<ModelPdf>) results.values;

        adapterPdfUser.notifyDataSetChanged();
    }
}
