package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.DisponibilidadeHorario;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.ConvertsDate;
import br.edu.ifpb.ajudemais.utils.EstadosDonativoUtil;

public class DonativosAdapter extends RecyclerView.Adapter<DonativosAdapter.ViewHolder> {

    private List<DoacaoAdapterDto> donativos;
    private Context context;
    private AndroidUtil androidUtil;
    private EstadosDonativoUtil estadosDonativoUtil;

    /**
     * @param donativos
     * @param context
     */
    public DonativosAdapter(List<DoacaoAdapterDto> donativos, Context context) {
        this.donativos = donativos;
        this.context = context;
        this.androidUtil = new AndroidUtil(context);
        this.estadosDonativoUtil = new EstadosDonativoUtil();

    }

    @Override
    public DonativosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_search_my_coletas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonativosAdapter.ViewHolder holder, int position) {
        holder.donativeName.setText(donativos.get(position).getDonativo().getNome());

        String dataColeta = donativos.get(position).getDonativo().getCategoria().getInstituicaoCaridade().getNome();

        for (DisponibilidadeHorario dh : donativos.get(position).getDonativo().getHorariosDisponiveis()) {

            if ((dh.getAtivo() != null && dh.getAtivo()) && donativos.get(position).getDonativo().getMensageiro() != null) {
                dataColeta = ConvertsDate.getInstance().
                        convertDateToStringFormat(dh.getHoraInicio()) + " das " +
                        ConvertsDate.getInstance().convertHourToString(dh.getHoraInicio()) + "h às " +
                        ConvertsDate.getInstance().convertHourToString(dh.getHoraFim());
            }
        }

        holder.dataColeta.setText(dataColeta);

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

    /**
     * @param donativos
     */
    public void setFilter(List<DoacaoAdapterDto> donativos) {
        this.donativos = donativos;
        notifyDataSetChanged();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView donativeName;
        TextView dataColeta;
        TextView estadoDoacao;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imagem_donativo_coleta);
            donativeName = (TextView) itemView.findViewById(R.id.tv_donativo_nome_coleta);
            dataColeta = (TextView) itemView.findViewById(R.id.tv_data_coleta);
            estadoDoacao = (TextView) itemView.findViewById(R.id.tv_donative_estado_coleta);

        }

        public void bind(Donativo donativo) {
            donativeName.setText(donativo.getNome());
        }
    }
}
