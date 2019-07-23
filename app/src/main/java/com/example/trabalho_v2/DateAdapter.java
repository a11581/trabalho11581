package com.example.trabalho_v2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho_v2.Model.Date;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder>{

    List<Date> dataset;
    private static ClickListener clickListener;

    public DateAdapter(List<Date> ds) {
        this.dataset = ds;
    }

    public List<Date> getDataset() {
        return dataset;
    }

    @NonNull
    @Override
    public DateAdapter.DateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.date_entry,viewGroup,false);
        DateViewHolder evh = new DateViewHolder(v,this);
        return evh;
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder classViewHolder, int i) {
        classViewHolder.dateDate.setText(dataset.get(i).getDate());
        classViewHolder.dateDescription.setText(dataset.get(i).getDescription());

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DateAdapter.clickListener = clickListener;
    }


    public static class DateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView dateDate;
        public TextView dateDescription;
        public DateAdapter adapterRef;


        public DateViewHolder(View v,  DateAdapter ref) {
            super(v);
            dateDate = v.findViewById(R.id.dateDateId);
            dateDescription = v.findViewById(R.id.dateDescriptionId);
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
        void onItemClick(DateAdapter adapter, View v, int position);
        void onItemLongClick(DateAdapter adapter, View v, int position);
    }

}
