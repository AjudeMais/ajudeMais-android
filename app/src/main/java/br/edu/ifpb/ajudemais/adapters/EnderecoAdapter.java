package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Endereco;

/**
 * Created by amsv on 21/05/17.
 */

public class EnderecoAdapter extends  RecyclerView.Adapter<EnderecoAdapter.ViewHolder> {

    private List<Endereco> enderecos;
    private Context context;

    public EnderecoAdapter(List<Endereco> enderecos, Context context) {
        this.enderecos = enderecos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_endereco, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.logradouroName.setText(enderecos.get(position).getLogradouro());
        holder.ufName.setText(enderecos.get(position).getUf());
        holder.numberName.setText(enderecos.get(position).getNumero()+",");
        holder.cityName.setText(enderecos.get(position).getLocalidade()+",");
        holder.bairroName.setText(enderecos.get(position).getBairro()+",");
        holder.cepName.setText(enderecos.get(position).getCep()+",");

    }

    @Override
    public int getItemCount() {
        if (enderecos == null) {
            enderecos = new ArrayList<>();
        }
        return enderecos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView logradouroName;
        TextView ufName;
        TextView cityName;
        TextView bairroName;
        TextView numberName;
        TextView cepName;

        public ViewHolder(View itemView) {
            super(itemView);
            logradouroName = (TextView) itemView.findViewById(R.id.tv_logradouro_name);
            ufName = (TextView) itemView.findViewById(R.id.tv_uf_name);
            cityName = (TextView) itemView.findViewById(R.id.tv_city);
            bairroName = (TextView) itemView.findViewById(R.id.tv_bairro);
            numberName = (TextView) itemView.findViewById(R.id.tv_number);
            cepName = (TextView) itemView.findViewById(R.id.tv_cep_name);


        }
    }

}
