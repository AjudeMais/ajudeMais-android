package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Categoria;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.adapters</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um foto.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder>{

    private List<Categoria> categories;
    private Context context;

    public CategoriasAdapter(List<Categoria> categories, Context context){
        this.context = context;
        this.categories = categories;

    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CategoriasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_inst_itens_doaveis, parent, false);
        return new CategoriasAdapter.ViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.categoryName.setText(categories.get(position).getNome());
        holder.categoryDescription.setText(categories.get(position).getDescricao());
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {

        if(categories == null){
            categories = new ArrayList<>();
        }
        return categories.size();
    }



    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        TextView categoryDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
            categoryDescription = (TextView) itemView.findViewById(R.id.tv_category_description);
        }
    }


}
