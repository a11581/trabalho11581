package com.example.trabalho_v2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho_v2.Model.Discipline;

import java.util.List;

public class DisciplineAdapter extends RecyclerView.Adapter<DisciplineAdapter.DisciplineViewHolder>{

    List<Discipline> dataset;
    private static ClickListener clickListener;

    public DisciplineAdapter(List<Discipline> ds) {
        this.dataset = ds;
    }

    public List<Discipline> getDataset() {
        return dataset;
    }

    @NonNull
    @Override
    public DisciplineAdapter.DisciplineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.discipline_entry,viewGroup,false);
        DisciplineViewHolder evh = new DisciplineViewHolder(v,this);
        return evh;
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DisciplineViewHolder classViewHolder, int i) {
        classViewHolder.disciplineName.setText("Name: " + dataset.get(i).getName());
        classViewHolder.disciplineAcronym.setText("Acronym: " + dataset.get(i).getAcronym());
        classViewHolder.disciplinePeriod.setText("Period: " + dataset.get(i).getPeriod());
        if(dataset.get(i).getYear()==null){
            classViewHolder.disciplineYear.setText("Empty");
        }
        else{
            classViewHolder.disciplineYear.setText("Year: " + dataset.get(i).getYear().getYearDate().toString());
        }
        if(dataset.get(i).getCourse()==null){
            classViewHolder.disciplineCourse.setText("Empty");
        }
        else{
            classViewHolder.disciplineCourse.setText("Course: " + dataset.get(i).getCourse().getName());
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DisciplineAdapter.clickListener = clickListener;
    }


    public static class DisciplineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView disciplineName;
        public TextView disciplineAcronym;
        public TextView disciplineYear;
        public TextView disciplineCourse;
        public TextView disciplinePeriod;
        public DisciplineAdapter adapterRef;


        public DisciplineViewHolder(View v,  DisciplineAdapter ref) {
            super(v);
            disciplinePeriod = v.findViewById(R.id.disciplinePeriodId);
            disciplineName = v.findViewById(R.id.disciplineNameId);
            disciplineAcronym = v.findViewById(R.id.disciplineAcronymId);
            disciplineYear = v.findViewById(R.id.disciplineYearId);
            disciplineCourse = v.findViewById(R.id.disciplineCourseId);
            adapterRef = ref;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(adapterRef,v,getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(adapterRef,v,getAdapterPosition());
            return false;
        }
    }

    public interface ClickListener {
        void onItemClick(DisciplineAdapter adapter, View v, int position);
        void onItemLongClick(DisciplineAdapter adapter, View v, int position);
    }

}
