package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.DisponibilidadeHorario;
import br.edu.ifpb.ajudemais.utils.ConvertsDate;

/**
 * <p>
 * <b>{@link SelectHorarioColetaAdapter}</b>
 * </p>
 * <p>
 * <p>
 * Adapter para select Lista de horários
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class SelectHorarioColetaAdapter extends RecyclerView.Adapter<SelectHorarioColetaAdapter.ViewHolder>{

    private List<DisponibilidadeHorario> disponibilidadeHorarios;
    private Context context;

    public interface OnItemCheckListener {
        void onItemCheck(DisponibilidadeHorario disponibilidadeHorario);
        void onItemUncheck(DisponibilidadeHorario DisponibilidadeHorario);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    /**
     * Construtor default
     * @param disponibilidadeHorarios
     * @param context
     */
    public SelectHorarioColetaAdapter(List<DisponibilidadeHorario> disponibilidadeHorarios, Context context,@NonNull OnItemCheckListener onItemCheckListener) {
        this.disponibilidadeHorarios = disponibilidadeHorarios;
        this.context = context;
        this.onItemCheckListener = onItemCheckListener;

    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SelectHorarioColetaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horario_coleta, parent, false);
        return new SelectHorarioColetaAdapter.ViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final SelectHorarioColetaAdapter.ViewHolder holder, int position) {
        final DisponibilidadeHorario currentDisponibilidadeHorario = disponibilidadeHorarios.get(position);

        holder.dataDisponibilidade.setText(ConvertsDate.getInstance().
                convertDateToStringFormat(disponibilidadeHorarios.get(position).getHoraInicio()));

        holder.faixaDeHorarios.setText("disponibilidade das "+
                ConvertsDate.getInstance().convertHourToString(disponibilidadeHorarios.get(position).getHoraInicio())+
                "h às "+ConvertsDate.getInstance().convertHourToString(disponibilidadeHorarios.get(position).getHoraFim())+"h");



        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(
                        !holder.checkBox.isChecked());
                if (holder.checkBox.isChecked()) {
                    onItemCheckListener.onItemCheck(currentDisponibilidadeHorario);
                } else {
                    onItemCheckListener.onItemUncheck(currentDisponibilidadeHorario);
                }
            }
        });
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        if (disponibilidadeHorarios == null) {
            disponibilidadeHorarios = new ArrayList<>();
        }
        return disponibilidadeHorarios.size();
    }

    public void setFilter(List<DisponibilidadeHorario> disponibilidadeHorarios) {
        this.disponibilidadeHorarios = disponibilidadeHorarios;
        notifyDataSetChanged();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dataDisponibilidade;
        TextView faixaDeHorarios;
        CheckBox checkBox;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dataDisponibilidade = (TextView) itemView.findViewById(R.id.tv_row_data);
            faixaDeHorarios = (TextView) itemView.findViewById(R.id.tv_row_campanha_termino_label);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkHorario);
            checkBox.setClickable(false);

        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }

    }


}

