package com.acevedo.rutaexperienciauc.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Beneficio;

import java.util.List;

public class BeneficioAdapter extends RecyclerView.Adapter<BeneficioAdapter.BeneficioHolder> implements View.OnClickListener{

    Context context;
    List<Beneficio> beneficioList;

    View.OnClickListener listener;

    int progress;

    public BeneficioAdapter(Context context, List<Beneficio> beneficioList, int porcentajeBeneficio){
        this.context = context;
        this.beneficioList = beneficioList;
        this.progress = porcentajeBeneficio;
    }

    @NonNull
    @Override
    public BeneficioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.beneficio_item, parent, false);
        mView.setOnClickListener(this);
        return new BeneficioHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficioAdapter.BeneficioHolder holder, int position) {
        Beneficio beneficio = beneficioList.get(position);
        holder.setText(beneficio.getBeDescripcion());
        holder.setProgress(progress);//esto deberia de ser de acuerdo al ciclo que rrecorre
    }

    @Override
    public int getItemCount() {
        return beneficioList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }
    public class BeneficioHolder extends RecyclerView.ViewHolder {

        ProgressBar pbBeneficio;
        TextView tvBeneficioTitle;
        View view;

        public BeneficioHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setText(String title){
            tvBeneficioTitle = view.findViewById(R.id.tvBeneficioTitle);
            tvBeneficioTitle.setText(title);
        }

        public void setProgress(int progress){
            pbBeneficio = view.findViewById(R.id.pbBeneficio);
            //pbBeneficio.setProgress(progress);

            //animacion al progressbarBeneficio
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(pbBeneficio, "progress",0,progress);//el ultimo 100 es el progresso
            //progressAnimator.setDuration(2000);
            //progressAnimator.setInterpolator(new LinearInterpolator());
            progressAnimator.start();
        }
    }
}
