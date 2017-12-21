package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Date;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.DonativoCampanha;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.enumarations.Estado;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;


/**
 * <p>
 * <b>{@link RealizarDoacaoInCampanhaTask}</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um foto.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class RealizarDoacaoInCampanhaTask extends AsyncTask<Void, Void, DonativoCampanha> {

    private DoadorRemoteService doadorRemoteService;
    public AsyncResponse<DonativoCampanha> delegate = null;
    private String message = null;
    private Doador doador;
    private DonativoCampanha donativoCampanha;
    private Context context;
    private String username;
    private ProgressDialog progressDialog;
    private DonativoRemoteService donativoRemoteService;

    public RealizarDoacaoInCampanhaTask(Context context, String usernameDoador, DonativoCampanha donativoCampanha) {
        this.username = usernameDoador;
        this.context = context;
        this.donativoCampanha = donativoCampanha;
        this.doadorRemoteService = new DoadorRemoteService(context);
        this.donativoRemoteService = new DonativoRemoteService(context);
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.showProgressDialog();
    }

    @Override
    protected DonativoCampanha doInBackground(Void... params) {
        try {
            EstadoDoacao estadoDoacao = new EstadoDoacao();
            estadoDoacao.setData(new Date());
            estadoDoacao.setAtivo(true);
            estadoDoacao.setEstadoDoacao(Estado.DISPONIBILIZADO);
            doador = doadorRemoteService.getDoador(username);
            donativoCampanha.getDonativo().setDoador(doador);
            donativoCampanha.getDonativo().setEstadosDaDoacao(new ArrayList<EstadoDoacao>());
            donativoCampanha.getDonativo().getEstadosDaDoacao().add(estadoDoacao);
            donativoCampanha = donativoRemoteService.saveDonativoCampanha(donativoCampanha);
            Log.e("AJUDEMAIS", donativoCampanha.toString());
            return donativoCampanha;
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(DonativoCampanha donativoCampanha) {

        progressDialog.dismissProgressDialog();

        if (donativoCampanha != null) {
            delegate.processFinish(donativoCampanha);
        }

        if (message != null) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }
}
