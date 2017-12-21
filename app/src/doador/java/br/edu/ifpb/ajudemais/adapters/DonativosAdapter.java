package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.enumarations.Estado;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.EstadosDonativoUtil;

/**
 * <p>
 * <b>{@link DonativosAdapter}</b>
 * </p>
 * <p>
 * <p>
 * Adapter para gerenciar a exibição e operação de listas com donativos
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class DonativosAdapter extends RecyclerView.Adapter<DonativosAdapter.ViewHolder> {

    private List<DoacaoAdapterDto> donativos;
    private Context context;
    private AndroidUtil androidUtil;
    private EstadosDonativoUtil estadosDonativoUtil;

    public DonativosAdapter(List<DoacaoAdapterDto> donativos, Context context) {
        this.context = context;
        this.donativos = donativos;
        this.androidUtil = new AndroidUtil(context);
        this.estadosDonativoUtil = new EstadosDonativoUtil();

    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public DonativosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_doacoes, parent, false);
        return new DonativosAdapter.ViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(DonativosAdapter.ViewHolder holder, int position) {

        holder.donativeName.setText(donativos.get(position).getDonativo().getNome());

        holder.instituicaoName.setText(donativos.get(position).getDonativo().getCategoria().getInstituicaoCaridade().getNome());

        if (donativos.get(position).getPhoto() != null) {
            holder.imageView.setImageBitmap(androidUtil.convertBytesInBitmap(donativos.get(position).getPhoto()));
        }

        for (EstadoDoacao estadoDoacao : donativos.get(position).getDonativo().getEstadosDaDoacao()) {
            if (estadoDoacao.getAtivo() != null && estadoDoacao.getAtivo()) {
                estadosDonativoUtil.setCustomLabelEstadoDoacao(holder.estadoDoacao, estadoDoacao.getEstadoDoacao());
                holder.estadoDoacao.setText(estadoDoacao.getEstadoDoacao().name().equals("NAO_ACEITO") ? "NÃO ACEITO" :
                        estadoDoacao.getEstadoDoacao().name().equals("CANCELADO_POR_MENSAGEIRO") ? "CANCELADO"  : estadoDoacao.getEstadoDoacao().name());
            }
        }
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {

        if (donativos == null) {
            donativos = new ArrayList<>();
        }
        return donativos.size();
    }


    public void setFilter(List<DoacaoAdapterDto> donativos) {
        this.donativos = donativos;
        notifyDataSetChanged();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView donativeName;
        TextView instituicaoName;
        TextView estadoDoacao;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imagem_donativo);
            donativeName = (TextView) itemView.findViewById(R.id.tv_donative_name);
            instituicaoName = (TextView) itemView.findViewById(R.id.tv_name_instituicao);
            estadoDoacao = (TextView) itemView.findViewById(R.id.tv_donative_estado);

        }

        public void bind(Donativo donativo) {
            donativeName.setText(donativo.getNome());
            instituicaoName.setText(donativo.getCategoria().getInstituicaoCaridade().getNome());
        }
    }

}
