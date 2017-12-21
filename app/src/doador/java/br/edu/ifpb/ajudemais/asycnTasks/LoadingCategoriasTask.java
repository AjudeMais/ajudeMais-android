package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.adapters.CategoriasAdapter;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Categoria;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.fragments.InstituicaoDetailFragment;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.remoteServices.CategoriaRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link LoadingDoadorTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para carregar lista de categoria de itens doáveis
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LoadingCategoriasTask extends AsyncTask<Void, Void, List<Categoria>> {

    private CategoriaRemoteService categoriaRemoteService;
    private List<Categoria> categorias;
    private String message;
    private Long idInstituicao;
    private Context context;
    public AsyncResponse<List<Categoria>> delegate;

    public LoadingCategoriasTask(Context context, Long idInstituicao){
        this.context = context;
        this.idInstituicao = idInstituicao;
        categoriaRemoteService = new CategoriaRemoteService(context);
    }


    /**
     * @param params
     * @return
     */
    @Override
    protected List<Categoria> doInBackground(Void... params) {
        try {
            categorias = categoriaRemoteService.listCategoriasAtivasToInstituicao(idInstituicao);
            return categorias;

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }

    /**
     * @param categorias
     */
    @Override
    protected void onPostExecute(List<Categoria> categorias) {

        if (message != null){
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
        if (categorias != null) {
            delegate.processFinish(categorias);
        }


    }

}
