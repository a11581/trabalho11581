package com.example.trabalho_v2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.trabalho_v2.Model.Class;


import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder>{

    List<Class> dataset;
    private static ClickListener clickListener;

    public ClassAdapter(List<Class> ds) {
        this.dataset = ds;
    }

    public List<Class> getDataset() {
        return dataset;
    }

    @NonNull
    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.class_entry,viewGroup,false);
        ClassViewHolder evh = new ClassViewHolder(v, this);
        return evh;
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int i) {
        classViewHolder.classDate.setText(dataset.get(i).getDate());
        classViewHolder.classStrHour.setText("Starting at: "+dataset.get(i).getStartingHour().toString()+" Hour");
        classViewHolder.classDuration.setText("Duration: "+dataset.get(i).getDuration().toString()+" Minutes");
        classViewHolder.classRoom.setText("Room: " + dataset.get(i).getRoom());
        classViewHolder.classSpecial.setText(dataset.get(i).getSpecial().toString());
        classViewHolder.classDisciplineName.setText("Discipline: " + dataset.get(i).getDisciplineName());
    }
    public void setOnItemClickListener(ClassAdapter.ClickListener clickListener) {
        ClassAdapter.clickListener = clickListener;
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView classDate;
        public TextView classStrHour;
        public TextView classDuration;
        public TextView classRoom;
        public TextView classSpecial;
        public TextView classDisciplineName;
        public ClassAdapter adapterRef;


        public ClassViewHolder(View v, ClassAdapter ref) {
            super(v);
            classDate = v.findViewById(R.id.classDateId);
            classStrHour = v.findViewById(R.id.classStartingHourId);
            classDuration = v.findViewById(R.id.classDurationId);
            classRoom = v.findViewById(R.id.classRoomId);
            classDisciplineName = v.findViewById(R.id.classDisciplineNameId);
            classSpecial = v.findViewById(R.id.classSpecialId);
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
        void onItemClick(ClassAdapter adapter, View v, int position);
        void onItemLongClick(ClassAdapter adapter, View v, int position);
    }
}
