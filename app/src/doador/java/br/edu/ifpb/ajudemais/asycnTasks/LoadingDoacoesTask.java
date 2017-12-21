package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link LoadingDoacoesTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn para carregar donativos de um doador.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LoadingDoacoesTask extends AsyncTask<Void, Void, List<DoacaoAdapterDto>> {

    public AsyncResponse<List<DoacaoAdapterDto>> delegate = null;
    private String message = null;
    private Context context;
    private String username;
    private DonativoRemoteService donativoRemoteService;
    private DoadorRemoteService doadorRemoteService;
    private ImagemStorageRemoteService imagemStorageRemoteService;
    private List<DoacaoAdapterDto> doacaoAdapterDtos;

    public LoadingDoacoesTask(Context context, String nameDoador) {
        this.context = context;
        this.username = nameDoador;
        this.donativoRemoteService = new DonativoRemoteService(context);
        this.doadorRemoteService = new DoadorRemoteService(context);
        this.imagemStorageRemoteService = new ImagemStorageRemoteService(context);
        this.doacaoAdapterDtos = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<DoacaoAdapterDto> doInBackground(Void... params) {
        try {

            Doador doador = doadorRemoteService.getDoador(username);

            List<Donativo> donativos = donativoRemoteService.findByDonativosToDoadorId(doador.getId());

            for (Donativo donativo : donativos) {
                DoacaoAdapterDto doacaoAdapterDto = new DoacaoAdapterDto();
                doacaoAdapterDto.setDonativo(donativo);

                if (donativo.getFotosDonativo() != null && donativo.getFotosDonativo().size() > 0) {
                    byte[] photo = imagemStorageRemoteService.getImage(donativo.getFotosDonativo().get(0).getNome());
                    doacaoAdapterDto.setPhoto(photo);
                }
                doacaoAdapterDtos.add(doacaoAdapterDto);
            }

            return doacaoAdapterDtos;
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<DoacaoAdapterDto> donativos) {
        if (donativos != null) {
            delegate.processFinish(donativos);
        }

        if (message != null) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }
}
