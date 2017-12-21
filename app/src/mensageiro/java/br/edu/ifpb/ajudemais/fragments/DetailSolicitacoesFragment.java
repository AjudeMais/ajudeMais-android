package br.edu.ifpb.ajudemais.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.MainActivity;
import br.edu.ifpb.ajudemais.activities.NotRecolhidoActivity;
import br.edu.ifpb.ajudemais.activities.SelectHorarioColetaActivity;
import br.edu.ifpb.ajudemais.adapters.DisponibilidadeHorarioAdapter;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.GetImageTask;
import br.edu.ifpb.ajudemais.asyncTasks.UpdateEstadoDonativoTask;
import br.edu.ifpb.ajudemais.asyncTasks.ValidateColetaTask;
import br.edu.ifpb.ajudemais.asyncTasks.ValidateRecolhimentoTask;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.enumarations.Estado;
import br.edu.ifpb.ajudemais.permissionsPolyce.CallPhoneDevicePermission;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.EstadosDonativoUtil;

import static br.edu.ifpb.ajudemais.permissionsPolyce.CallPhoneDevicePermission.MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION;


/**
 * <p>
 * <b>{@link DetailSolicitacoesFragment}</b>
 * </p>
 * <p>
 * <p>
 * Fragmento para exibir dados de notativo para solicitações de novas coletas por parte do mensageiros.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DetailSolicitacoesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Donativo donativo;
    private TextView descricaoDonativo;
    private TextView nomeInstituicao;
    private TextView informationAgenda;
    private TextView agendamentoColeta;
    private Button btnListDisp;
    private EstadosDonativoUtil estadosDonativoUtil;
    private CardView cardViewDoador;
    private TextView nomeDoador;
    private TextView telefoneDoador;
    private ImageView imageDoador;
    private GetImageTask getImageTask;
    private CallPhoneDevicePermission callPhoneDevicePermission;
    private Button btnAceito;
    private Button btnRejeitado;
    private Button btnRecolher;
    private Button btnNoRecolhido;
    private Button btnConfirmEntrega;
    private TextView tvQuantidade;
    private UpdateEstadoDonativoTask updateEstadoDonativoTask;
    private byte[] imagem;

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail_solicitacao, container, false);
        if (savedInstanceState != null) {
            imagem = savedInstanceState.getByteArray("ImagemDoador");
            donativo = (Donativo) savedInstanceState.getSerializable("Donativo");

        } else {
            Intent intentDonativo = getActivity().getIntent();
            donativo = (Donativo) intentDonativo.getSerializableExtra("Donativo");
            Log.i("AJUDEMAIS", donativo.getEstadosDaDoacao().toString());

        }
        setHasOptionsMenu(true);
        return view;
    }


    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        estadosDonativoUtil = new EstadosDonativoUtil();
        callPhoneDevicePermission = new CallPhoneDevicePermission(getContext());
        descricaoDonativo = (TextView) getView().findViewById(R.id.tv_description);
        nomeInstituicao = (TextView) getView().findViewById(R.id.tv_instituicao_name);
        cardViewDoador = (CardView) getView().findViewById(R.id.cardDoador);
        agendamentoColeta = (TextView) getView().findViewById(R.id.tvAgendamentoColeta);
        informationAgenda = (TextView) getView().findViewById(R.id.tvInformationAgenda);
        descricaoDonativo.setText(donativo.getDescricao());
        nomeDoador = (TextView) getView().findViewById(R.id.tvNomeMsg);
        telefoneDoador = (TextView) getView().findViewById(R.id.tvTellphone);

        imageDoador = (ImageView) getView().findViewById(R.id.photoProfile);

        if (donativo.getDescricao() != null && donativo.getDescricao().length() > 0) {
            descricaoDonativo.setVisibility(View.VISIBLE);
        }

        btnListDisp = (Button) view.findViewById(R.id.btn_lista_disponibilidade);
        btnAceito = (Button) view.findViewById(R.id.btn_aceitar);
        btnRejeitado = (Button) view.findViewById(R.id.btn_rejeitar);
        btnRecolher = (Button) view.findViewById(R.id.btn_recolher);
        btnNoRecolhido = (Button) view.findViewById(R.id.btn_n_recolher);
        btnConfirmEntrega = (Button) view.findViewById(R.id.btn_confirm_entrega);

        tvQuantidade = (TextView) view.findViewById(R.id.tv_quantidade);
        tvQuantidade.setText("Quantidade: " + Float.toString(donativo.getQuantidade()));

        btnListDisp.setOnClickListener(this);
        btnAceito.setOnClickListener(this);
        btnRejeitado.setOnClickListener(this);
        btnRecolher.setOnClickListener(this);
        btnNoRecolhido.setOnClickListener(this);
        btnConfirmEntrega.setOnClickListener(this);

        setAtrAddressIntoCard(donativo.getEndereco());
        setInformationDoador();
        setVisibleBtns(donativo);
    }


    private void setInformationDoador() {
        nomeDoador.setText(donativo.getDoador().getNome());
        telefoneDoador.setText(donativo.getDoador().getTelefone());

        if (donativo.getDoador().getFoto() != null) {
            if (imagem != null) {
                AndroidUtil androidUtil = new AndroidUtil(getContext());
                Bitmap bitmap = androidUtil.convertBytesInBitmap(imagem);
                imageDoador.setImageBitmap(bitmap);

            } else {
                getImageTask = new GetImageTask(getContext(), donativo.getDoador().getFoto().getNome());
                getImageTask.setProgressAtivo(false);
                getImageTask.delegate = new AsyncResponse<byte[]>() {
                    @Override
                    public void processFinish(byte[] output) {
                        imagem = output;
                        AndroidUtil androidUtil = new AndroidUtil(getContext());
                        Bitmap bitmap = androidUtil.convertBytesInBitmap(output);
                        imageDoador.setImageBitmap(bitmap);
                    }
                };
                getImageTask.execute();
            }
        }

        cardViewDoador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallDoador();
            }
        });


    }


    /**
     * Cria dialog para perguntar mensageiro se deseja ligar para doadors
     */
    private void dialogCallDoador() {
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setTitle(getString(R.string.call_doador))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (callPhoneDevicePermission.isCallPhonePermissionGranted()) {
                            callPhoneDevicePermission.callPhone(donativo.getDoador().getTelefone());
                        }
                    }
                })

                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    /**
     * Seta endereço no cardview
     */
    private void setAtrAddressIntoCard(Endereco endereco) {
        CardView cardView = (CardView) getView().findViewById(R.id.componentAddress);
        ((TextView) cardView.findViewById(R.id.tv_logradouro_name)).setText(endereco.getLogradouro());
        ((TextView) cardView.findViewById(R.id.tv_bairro)).setText(endereco.getBairro());
        ((TextView) cardView.findViewById(R.id.tv_number)).setText(endereco.getNumero());
        ((TextView) cardView.findViewById(R.id.tv_cep_name)).setText(endereco.getCep());
        ((TextView) cardView.findViewById(R.id.tv_city)).setText(endereco.getLocalidade());
        ((TextView) cardView.findViewById(R.id.tv_uf_name)).setText(endereco.getUf());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_aceitar) {
            executeValidateColetaDonativoTask();
        } else if (v.getId() == R.id.btn_rejeitar) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_lista_disponibilidade) {
            showDialogListDisponibilidade();
        } else if (v.getId() == R.id.btn_recolher) {
            showDialogBtns(1, getString(R.string.confirm_recolhimento));
        } else if (v.getId() == R.id.btn_n_recolher) {
            showDialogBtns(2, getString(R.string.confirm_n_recolhimento_dialog));
        } else if (v.getId() == R.id.btn_confirm_entrega) {
            showDialogBtns(3, getString(R.string.confirm_entrega_dialog));
        }
    }

    /**
     * Verifica se donativo ainda está disponível para ser coletado
     */
    public void executeValidateColetaDonativoTask() {

        ValidateColetaTask validateColetaTask = new ValidateColetaTask(getContext(), donativo.getId());
        validateColetaTask.delegate = new AsyncResponse<Boolean>() {
            @Override
            public void processFinish(Boolean output) {
                if (output) {
                    Intent intent = new Intent(getContext(), SelectHorarioColetaActivity.class);
                    intent.putExtra("Donativo", donativo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    CustomToast.getInstance(getContext()).createSuperToastSimpleCustomSuperToast(getString(R.string.coleta_not_avaible));
                }
            }
        };
        validateColetaTask.execute();

    }

    /**
     * Verifica se donativo ainda está disponível para ser recolhifo
     */
    public void executeValidateRecolhimentoDonativoTask() {
        ValidateRecolhimentoTask validateRecolhimentoTask = new ValidateRecolhimentoTask(getContext(), donativo.getId());
        validateRecolhimentoTask.delegate = new AsyncResponse<Boolean>() {
            @Override
            public void processFinish(Boolean output) {
                if (output) {
                    EstadoDoacao estadoDoacao = new EstadoDoacao();
                    estadoDoacao.setAtivo(true);
                    estadoDoacao.setData(new Date());
                    estadoDoacao.setEstadoDoacao(Estado.RECOLHIDO);
                    donativo.getEstadosDaDoacao().add(estadoDoacao);
                    executeUpdateDonativoTask(donativo, getString(R.string.coleta_recolhida));
                } else {
                    CustomToast.getInstance(getContext()).createSuperToastSimpleCustomSuperToast(getString(R.string.recolhimento_not_avaible));
                }
            }
        };
        validateRecolhimentoTask.execute();

    }

    /**
     * Dialog para mostrar a lista de disponibilidade para coletar doação
     */
    private void showDialogListDisponibilidade() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_list_disponibilidades, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycle_view_list_dispo);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
        DisponibilidadeHorarioAdapter disponibilidadeHorarioAdapter = new DisponibilidadeHorarioAdapter(donativo.getHorariosDisponiveis(), getContext());
        recyclerView.setAdapter(disponibilidadeHorarioAdapter);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    /**
     * Dialog para confirmação de donativo não recolhido
     */
    private void showDialogBtns(final int option, String dialogTitle) {
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setTitle(dialogTitle)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        for (int i = 0; i < donativo.getEstadosDaDoacao().size(); i++) {
                            if (donativo.getEstadosDaDoacao().get(i).getAtivo()) {
                                donativo.getEstadosDaDoacao().get(i).setAtivo(false);
                            }
                        }

                        switch (option) {
                            case 1:
                                executeValidateRecolhimentoDonativoTask();
                                break;
                            case 2:
                                Intent intent = new Intent(getContext(), NotRecolhidoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("Donativo", donativo);
                                startActivity(intent);
                                break;
                            case 3:
                                EstadoDoacao estadoDoacao = new EstadoDoacao();
                                estadoDoacao.setAtivo(true);
                                estadoDoacao.setData(new Date());
                                estadoDoacao.setEstadoDoacao(Estado.RECEBIDO);
                                donativo.getEstadosDaDoacao().add(estadoDoacao);
                                executeUpdateDonativoTask(donativo, getString(R.string.coleta_entregue));
                                break;
                        }
                    }
                })

                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    /**
     * Executa Asycn task para atualizar o estado do donativo;
     */
    private void executeUpdateDonativoTask(final Donativo donativo, final String msg) {
        updateEstadoDonativoTask = new UpdateEstadoDonativoTask(getContext(), donativo);
        updateEstadoDonativoTask.delegate = new AsyncResponse<Donativo>() {
            @Override
            public void processFinish(Donativo output) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                CustomToast.getInstance(getContext()).createSuperToastSimpleCustomSuperToast(msg);
            }
        };
        updateEstadoDonativoTask.execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION: {
                if (callPhoneDevicePermission.isCallPhonePermissionGranted()) {
                    callPhoneDevicePermission.callPhone(donativo.getMensageiro().getTelefone());
                }
                break;
            }
        }
    }

    /**
     *
     */
    private void setVisibleBtns(Donativo donativo) {
        EstadoDoacao estadoDoacao = this.getEstadoDoacaoAtivo(donativo);

        if (donativo.getMensageiro() != null) {
            btnAceito.setVisibility(view.GONE);
            btnRejeitado.setVisibility(view.GONE);

            if (estadoDoacao != null && estadoDoacao.getEstadoDoacao().equals(Estado.ACEITO)) {
                btnRecolher.setVisibility(view.VISIBLE);
                btnNoRecolhido.setVisibility(view.VISIBLE);

            } else if (estadoDoacao != null && estadoDoacao.getEstadoDoacao().equals(Estado.RECOLHIDO)) {
                btnConfirmEntrega.setVisibility(view.VISIBLE);
            }
        }
    }

    /**
     * @param donativo
     * @return
     */
    private EstadoDoacao getEstadoDoacaoAtivo(Donativo donativo) {
        EstadoDoacao estadoDoacao = null;
        for (EstadoDoacao estado : donativo.getEstadosDaDoacao()) {
            if (estado.getAtivo()) {
                estadoDoacao = estado;
            }
        }
        return estadoDoacao;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Donativo", donativo);
        outState.putByteArray("ImagemDoador", imagem);

    }
}
