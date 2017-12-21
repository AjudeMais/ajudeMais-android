package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Meta;

/**
 * <p>
 * <b>{@link MetasAdapter}</b>
 * </p>
 * <p>
 * <p>
 * Adapter para mostrar as metas de uma campanha.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MetasAdapter  extends RecyclerView.Adapter<MetasAdapter.ViewHolder>{

    private List<Meta> metas;
    private Context context;

    public MetasAdapter(Context context, List<Meta> metas){
        this.context = context;
        this.metas = metas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_metas_campanha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetasAdapter.ViewHolder holder, int position) {
        holder.tvCategoria.setText(metas.get(position).getCategoria().getNome());
        holder.tvPercentual.setText(Float.toString(metas.get(position).getPercentualAtingido())+"%");
        holder.skMeta.setProgress(metas.get(position).getPercentualAtingido().intValue());
    }

    @Override
    public int getItemCount() {
        if (metas == null) {
            metas = new ArrayList<>();
        }
        return metas.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategoria;
        SeekBar skMeta;
        TextView tvPercentual;

        /**
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoria = (TextView) itemView.findViewById(R.id.tv_nome_categoria);
            skMeta = (SeekBar) itemView.findViewById(R.id.sbar_line_meta);
            tvPercentual = (TextView) itemView.findViewById(R.id.tv_percentual_atigido);
        }

        /**
         *
         * @param meta
         */
        public void bind(Meta meta) {
            tvCategoria.setText(meta.getCategoria().getNome());
            skMeta.setProgress(meta.getPercentualAtingido().intValue());
            tvPercentual.setText(Float.toString(meta.getPercentualAtingido()));
        }
    }
}
