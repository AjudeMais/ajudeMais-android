package br.edu.ifpb.ajudemais.activities;

/**
 * <p>
 * <b>{@link AsyncActivity}</b>
 * </p>
 *
 * <p>
 *     Interface base para implementação de dialog de carregamento de Tasks.
 *</p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public interface AsyncActivity {

    /**
     * Chama Progress Dialog para ser exibido no carregamento de alguma task.
     */
    void showLoadingProgressDialog();

    /**
     * Chama Progress Dialog para ser exibido no carregamento de alguma task com uma mensagem a ser exibida de alerta.
     * @param message
     */
    void showProgressDialog(CharSequence message);

    /**
     * Fecha Progress Dialog em execução.
     */
    void dismissProgressDialog();
}
