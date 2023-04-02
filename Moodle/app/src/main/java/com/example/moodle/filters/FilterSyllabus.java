package com.example.moodle.filters;

import android.util.Log;
import android.widget.Filter;

import com.example.moodle.adapters.AdapterSyllabus;
import com.example.moodle.models.ModelSyllabus;

import java.util.ArrayList;

public class FilterSyllabus extends Filter {
    ArrayList<ModelSyllabus> filterList;
    AdapterSyllabus adapterSyllabus;
    public static final String TAG="FILTER_SYLLABUS";

    public FilterSyllabus(ArrayList<ModelSyllabus> filterList,com.example.moodle.adapters.AdapterSyllabus adapterSyllabus) {
        this.filterList = filterList;
        this.adapterSyllabus = adapterSyllabus;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null && constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<ModelSyllabus> filteredModels=new ArrayList<>();
            for(int i=0;i<filterList.size();i++){
                if(filterList.get(i).getCourses().toUpperCase().contains(constraint)){
                    Log.d(TAG,"on Filter Syllabus perform Filtering-If statement");
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count=filteredModels.size();
            results.values=filteredModels;
        }
        else{
            Log.d(TAG,"on Filter Syllabus perform Filtering-else statement");
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        Log.d(TAG,"on Filter Syllabus publish Results");
        adapterSyllabus.filterList=(ArrayList<ModelSyllabus>) results.values;

        adapterSyllabus.notifyDataSetChanged();

    }
}
