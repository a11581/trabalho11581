package com.example.trabalho_v2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho_v2.Model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    List<Contact> dataset;
    private static ClickListener clickListener;

    public ContactAdapter(List<Contact> ds) {
        this.dataset = ds;
    }

    public List<Contact> getDataset() {
        return dataset;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_entry,viewGroup,false);
        ContactViewHolder evh = new ContactViewHolder(v,this);
        return evh;
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder classViewHolder, int i) {
        classViewHolder.contactStart.setText("Starting hour: " + dataset.get(i).getStartingHour().toString());
        classViewHolder.contactEnd.setText("Ending hour: " + dataset.get(i).getEndingHour().toString());
        classViewHolder.contactDay.setText("Day: " + dataset.get(i).getDay());
        classViewHolder.contactType.setText("Type: " + dataset.get(i).getType());
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ContactAdapter.clickListener = clickListener;
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView contactStart;
        public TextView contactEnd;
        public TextView contactDay;
        public TextView contactType;
        public ContactAdapter adapterRef;


        public ContactViewHolder(View v,  ContactAdapter ref) {
            super(v);
            contactStart = v.findViewById(R.id.contactStartingHourId);
            contactEnd = v.findViewById(R.id.contactEndingHourId);
            contactDay = v.findViewById(R.id.contactDayId);
            contactType = v.findViewById(R.id.contactTypeId);
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
        void onItemClick(ContactAdapter adapter, View v, int position);
        void onItemLongClick(ContactAdapter adapter, View v, int position);
    }

}
