package br.edu.ifpb.ajudemais.asyncTasks;

/**
 * <p>
 * <b>{@link AsyncResponse}</b>
 * </p>
 * <p>
 * <p>
 * Interface para capturar resulta de async taks
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public interface AsyncResponse<T> {

    /**
     * Chamado quando a task retorna o resultado.
     * @param output
     */
    void processFinish(T output);
}

