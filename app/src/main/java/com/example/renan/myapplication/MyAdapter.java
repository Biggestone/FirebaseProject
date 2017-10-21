package com.example.renan.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by renan on 6/17/17.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public int index;
    private List<String> lista;
    private List<String> listaDescriptions;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public TextView textViewDescription;
        public ImageView imageView;

        public ViewHolder(View view, int index) {
            super(view);
            textView = (TextView)view.findViewById(R.id.tv_mytext);
            textViewDescription=(TextView)view.findViewById(R.id.tv_description);
            if(index==2){
                imageView = (ImageView)view.findViewById(R.id.imv_profile);
                imageView.setVisibility(View.INVISIBLE);
            }

        }
    }



    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text,parent,false);


        ViewHolder vh = new ViewHolder(v,index);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(lista.get(position));
        holder.textViewDescription.setText(listaDescriptions.get(position));

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }



    public MyAdapter(List<String> lista,List<String> listaDescriptions, int index) {
        this.index = index;
        this.lista = lista;
        this.listaDescriptions = listaDescriptions;
    }


}
