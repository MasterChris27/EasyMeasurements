package com.example.intern12.easymeasurements.data;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.intern12.easymeasurements.R;

import java.util.ArrayList;

/**
 * Created by intern12 on 16.05.2017.
 */

public class AdapterMasurari  extends RecyclerView.Adapter<AdapterMasurari.MyViewHolder> {

    private ArrayList<Masurari> listaMasurari;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagine;
        public TextView nume,suprafata,cultura;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            imagine= (ImageView) view.findViewById(R.id.imagine_sugestiva);
            nume = (TextView) view.findViewById(R.id.nume_sugestiv);
            suprafata = (TextView) view.findViewById(R.id.suprafata);
            cultura = (TextView) view.findViewById(R.id.cultura);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox_list);
        }
    }


    public AdapterMasurari(ArrayList<Masurari> lista) {
        this.listaMasurari = lista;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.measurement_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        final int pos = position;
            Masurari masurari = listaMasurari.get(position);
            holder.imagine.setImageResource(R.mipmap.ic_launcher);
            holder.nume.setText(masurari.getName());
            holder.suprafata.setText(String.valueOf(masurari.getSize()) + " ha");
            holder.cultura.setText(masurari.getCultura());

            holder.checkBox.setChecked(listaMasurari.get(position).isSelected());
            holder.checkBox.setTag(listaMasurari.get(position));



            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Masurari masurare = (Masurari) cb.getTag();
                    masurare.setSelected(cb.isChecked());

                }


            });




    }

    @Override
    public int getItemCount() {
        return listaMasurari.size();
    }
}
