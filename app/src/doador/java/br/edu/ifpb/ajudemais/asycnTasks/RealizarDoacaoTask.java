package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Date;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.enumarations.Estado;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.asycnTasks</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para realizar uma doação avulsa
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class RealizarDoacaoTask extends AsyncTask<Void, Void, Donativo> {

    private DoadorRemoteService doadorRemoteService;
    public AsyncResponse<Donativo> delegate = null;
    private String message = null;
    private Doador doador;
    private Donativo donativo;
    private Context context;
    private String username;
    private ProgressDialog progressDialog;
    private DonativoRemoteService donativoRemoteService;

    public RealizarDoacaoTask(Context context, String usernameDoador, Donativo donativo) {
        this.username = usernameDoador;
        this.context = context;
        this.donativo = donativo;
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
    protected Donativo doInBackground(Void... params) {
        try {

            doador = doadorRemoteService.getDoador(username);
            donativo.setDoador(doador);
            donativo.setEstadosDaDoacao(new ArrayList<EstadoDoacao>());
            EstadoDoacao estadoDoacao = new EstadoDoacao();
            estadoDoacao.setData(new Date());
            estadoDoacao.setAtivo(true);
            estadoDoacao.setEstadoDoacao(Estado.DISPONIBILIZADO);
            donativo.getEstadosDaDoacao().add(estadoDoacao);
            donativo = donativoRemoteService.saveDonativo(donativo);

            return  donativo;
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Donativo donativo) {
        super.onPostExecute(donativo);

        progressDialog.dismissProgressDialog();

        if (donativo != null){
            delegate.processFinish(donativo);
        }

        if (message != null){
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }
}
