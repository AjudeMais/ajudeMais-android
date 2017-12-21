package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link LoadingMensageiroTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn task para carregar lista de donativos disponibilizados próximo do mensageiro com id passado.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LoadingNewSolicitacoesTask extends AsyncTask<Void, Void, List<DoacaoAdapterDto>> {

    public AsyncResponse<List<DoacaoAdapterDto>> delegate = null;
    private DonativoRemoteService donativoRemoteService;
    private String message;
    private Context context;
    private Long idMensageiro;
    private ImagemStorageRemoteService imagemStorageRemoteService;


    public LoadingNewSolicitacoesTask(Context context, Long idMensageiro) {
        this.idMensageiro = idMensageiro;
        this.context = context;
        this.donativoRemoteService = new DonativoRemoteService(context);
        this.imagemStorageRemoteService = new ImagemStorageRemoteService(context);

    }

    @Override
    protected List<DoacaoAdapterDto> doInBackground(Void... params) {
        try {
            List<Donativo> donativos = donativoRemoteService.filterByDonativosCloserMensageiroId(idMensageiro);
            List<DoacaoAdapterDto> donativosWithPhoto = new ArrayList<>();
            if (donativos.size()>0) {
                for (Donativo donativo : donativos) {
                    DoacaoAdapterDto doacaoAdapterDto = new DoacaoAdapterDto();
                    doacaoAdapterDto.setDonativo(donativo);

                    if (donativo.getFotosDonativo() != null && donativo.getFotosDonativo().size() > 0) {
                        byte[] photo = imagemStorageRemoteService.getImage(donativo.getFotosDonativo().get(0).getNome());
                        doacaoAdapterDto.setPhoto(photo);
                    }
                    donativosWithPhoto.add(doacaoAdapterDto);
                }
            }
            return donativosWithPhoto;

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param donativos
     */
    @Override
    protected void onPostExecute(List<DoacaoAdapterDto> donativos) {
        if (message != null) {
            CustomToast.getInstance(context).createSimpleCustomSuperToastActivity(message);
        }
        if (donativos != null) {
            delegate.processFinish(donativos);

        }


    }
}
