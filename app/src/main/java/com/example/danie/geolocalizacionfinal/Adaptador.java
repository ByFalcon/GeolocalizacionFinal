package com.example.danie.geolocalizacionfinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>
        implements View.OnClickListener {

    List<Lugar> lugares;
    private View.OnClickListener listener;
    private RecyclerView rv;

    public Adaptador(List<Lugar> lugares) {
        this.lugares = lugares;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvNombre.setText(lugares.get(i).getNombre());
        viewHolder.tvLocalidad.setText(lugares.get(i).getLocalidad());
        viewHolder.tvPais.setText(lugares.get(i).getPais());
    }

    @Override
    public int getItemCount() {
        if (lugares != null)
            return lugares.size();
        return 0;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void swap(List<Lugar> lugares) {
        this.lugares = lugares;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvLocalidad, tvPais;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvLocalidad = itemView.findViewById(R.id.tvLocalidad);
            tvPais = itemView.findViewById(R.id.tvPais);
        }
    }
}
