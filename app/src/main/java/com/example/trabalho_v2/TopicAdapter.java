package com.example.trabalho_v2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho_v2.Model.Topic;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder>{

    List<Topic> dataset;
    private static ClickListener clickListener;

    public TopicAdapter(List<Topic> ds) {
        this.dataset = ds;
    }

    public List<Topic> getDataset() {
        return dataset;
    }

    @NonNull
    @Override
    public TopicAdapter.TopicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.topic_entry,viewGroup,false);
        TopicViewHolder evh = new TopicViewHolder(v,this);
        return evh;
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder classViewHolder, int i) {
        classViewHolder.topicName.setText(dataset.get(i).getName());
        classViewHolder.topicContent.setText(dataset.get(i).getContent());
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TopicAdapter.clickListener = clickListener;
    }

    public void setOnItemClickListener(DateAdapter.ClickListener clickListener) {
    }


    public static class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView topicName;
        public TextView topicContent;
        public TopicAdapter adapterRef;


        public TopicViewHolder(View v,  TopicAdapter ref) {
            super(v);
            topicName = v.findViewById(R.id.topicNameId);
            topicContent = v.findViewById(R.id.topicContentId);
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
        void onItemClick(TopicAdapter adapter, View v, int position);
        void onItemLongClick(TopicAdapter adapter, View v, int position);
    }

}
