package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
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
 * <b>{@link br.edu.ifpb.ajudemais.domain.DisponibilidadeHorario}</b>
 * </p>
 * <p>
 * <p>
 * Adapter para lidar com listas de datas para coletas.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DisponibilidadeHorarioAdapter extends RecyclerView.Adapter<DisponibilidadeHorarioAdapter.ViewHolder>{

    private List<DisponibilidadeHorario> disponibilidadeHorarios;
    private Context context;

    /**
     * Construtor default
     * @param disponibilidadeHorarios
     * @param context
     */
    public DisponibilidadeHorarioAdapter(List<DisponibilidadeHorario> disponibilidadeHorarios, Context context) {
        this.disponibilidadeHorarios = disponibilidadeHorarios;
        this.context = context;

    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public DisponibilidadeHorarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_disponibildade_coleta, parent, false);
        return new DisponibilidadeHorarioAdapter.ViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(DisponibilidadeHorarioAdapter.ViewHolder holder, int position) {

        holder.dataDisponibilidade.setText(ConvertsDate.getInstance().
                convertDateToStringFormat(disponibilidadeHorarios.get(position).getHoraInicio()));

        holder.faixaDeHorarios.setText("disponibilidade das "+
                ConvertsDate.getInstance().convertHourToString(disponibilidadeHorarios.get(position).getHoraInicio())+
                "h às "+ConvertsDate.getInstance().convertHourToString(disponibilidadeHorarios.get(position).getHoraFim())+"h");
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

        public ViewHolder(View itemView) {
            super(itemView);
            dataDisponibilidade = (TextView) itemView.findViewById(R.id.tv_row_data);
            faixaDeHorarios = (TextView) itemView.findViewById(R.id.tv_row_campanha_termino_label);
        }

    }
}
