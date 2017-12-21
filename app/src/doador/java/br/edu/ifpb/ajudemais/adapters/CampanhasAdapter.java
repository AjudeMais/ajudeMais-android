package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;

/**
 * <p>
 * <b>CampanhasAdapter</b>
 * </p>
 * <p>
 * Adapter para lista de campanhas.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class CampanhasAdapter extends RecyclerView.Adapter<CampanhasAdapter.ViewHolder> {

    private List<Campanha> campanhas;
    private Context context;

    /**
     * @param campanhas
     * @param context
     */
    public CampanhasAdapter(List<Campanha> campanhas, Context context) {
        this.campanhas = campanhas;
        this.context = context;

    }

    @Override
    public CampanhasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_search_campanha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CampanhasAdapter.ViewHolder holder, int position) {
        holder.nomeCampanha.setText(campanhas.get(position).getNome());
        holder.instituicao.setText(campanhas.get(position).getInstituicaoCaridade().getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(campanhas.get(position).getDataFim());
        holder.termino.setText(date);
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        if (campanhas == null) {
            campanhas = new ArrayList<>();
        }
        return campanhas.size();
    }

    /**
     * @param campanhas
     */
    public void setFilter(List<Campanha> campanhas) {
        this.campanhas = campanhas;
        notifyDataSetChanged();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCampanha;
        TextView instituicao;
        TextView termino;

        /**
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            nomeCampanha = (TextView) itemView.findViewById(R.id.tv_row_campanha_name);
            instituicao = (TextView) itemView.findViewById(R.id.tv_row_campanha_inst);
            termino = (TextView) itemView.findViewById(R.id.tv_row_campanha_termino);
        }

        /**
         *
         * @param campanha
         */
        public void bind(Campanha campanha) {
            nomeCampanha.setText(campanha.getNome());
            instituicao.setText(campanha.getInstituicaoCaridade().getNome());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date = sdf.format(campanha.getDataFim());
            termino.setText(date);
        }
    }
}
