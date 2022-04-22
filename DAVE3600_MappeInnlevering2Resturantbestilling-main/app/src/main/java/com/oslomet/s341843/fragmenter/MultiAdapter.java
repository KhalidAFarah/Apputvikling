package com.oslomet.s341843.fragmenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oslomet.s341843.R;
import com.oslomet.s341843.database.Kontakt;

import java.util.ArrayList;
import java.util.List;

public class MultiAdapter extends RecyclerView.Adapter<MultiAdapter.MultiViewHolder>{

    private List<Kontakt> list;
    private Context context;
    private List<Kontakt> selected;
    public MultiAdapter(Context context, List<Kontakt> list){
        this.list = list;
        this.context = context;
        this.selected = new ArrayList<>();
    }

    public MultiAdapter(Context context, List<Kontakt> list, List<Kontakt> deltakere){
        this.list = list;
        this.context = context;
        this.selected = new ArrayList<>();
        for(Kontakt k : deltakere) selected.add(k);
    }



    public Context getContext() {
        return context;
    }

    public List<Kontakt> getList() {
        return list;
    }

    public List<Kontakt> getSelected() {
        return selected;
    }

    class MultiViewHolder extends RecyclerView.ViewHolder{
        private TextView txt;
        private CheckBox checkBox;

        public MultiViewHolder(View v){
            super(v);
            txt = v.findViewById(R.id.txt_list);
            checkBox = v.findViewById(R.id.check_list);
        }

        public void bind(Kontakt kontakt){
            txt.setText(kontakt.getName());
            checkBox.setChecked(false);

            for(Kontakt k : selected){
                if(kontakt.getId() == k.getId()) checkBox.setChecked(true);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()){
                        for(int i = 0; i < selected.size(); i++){
                            if(selected.get(i).getId() == kontakt.getId()) selected.remove(i);
                        }

                    }else{
                        selected.add(kontakt);
                    }
                    checkBox.setChecked(!checkBox.isChecked());

                }
            });
        }
    }

    @NonNull
    @Override
    public MultiAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiAdapter.MultiViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void empty(){
        selected = new ArrayList<>();
    }
}
