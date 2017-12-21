package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.asycnTasks</b>
 * </p>
 * <p>
 * <p>
 * AsycnTask carrega donativo por mensageiro.
 * </p>
 *
 */


public class LoadingDonativoByMensageiroTask extends AsyncTask<Void, Void, List<DoacaoAdapterDto>>{

    public AsyncResponse<List<DoacaoAdapterDto>> delegate = null;
    private DonativoRemoteService donativoRemoteService;
    private String message;
    private List<Donativo> donativos;
    private Context context;
    private String userName;
    private ImagemStorageRemoteService imagemStorageRemoteService;
    private List<DoacaoAdapterDto> doacaoAdapterDtos;

    public LoadingDonativoByMensageiroTask(Context context, String userName){
        this.context = context;
        this.userName = userName;
        this.imagemStorageRemoteService = new ImagemStorageRemoteService(context);
        this.doacaoAdapterDtos = new ArrayList<>();
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        donativoRemoteService = new DonativoRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected List<DoacaoAdapterDto> doInBackground(Void... params) {
        try {
            donativos = donativoRemoteService.findByMensageiro(userName);

            for (Donativo donativo : donativos) {
                DoacaoAdapterDto doacaoAdapterDto = new DoacaoAdapterDto();
                doacaoAdapterDto.setDonativo(donativo);

                if (donativo.getFotosDonativo() != null && donativo.getFotosDonativo().size() > 0) {
                    byte[] photo = imagemStorageRemoteService.getImage(donativo.getFotosDonativo().get(0).getNome());
                    doacaoAdapterDto.setPhoto(photo);
                }
                doacaoAdapterDtos.add(doacaoAdapterDto);
            }

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacaoAdapterDtos;
    }

    /**
     *
     */
    @Override
    protected void onPostExecute(List<DoacaoAdapterDto> donativos) {
        if (donativos != null && message == null) {
            delegate.processFinish(donativos);
        }else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
