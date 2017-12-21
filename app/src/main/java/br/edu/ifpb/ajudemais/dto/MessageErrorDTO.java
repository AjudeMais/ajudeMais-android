package br.edu.ifpb.ajudemais.dto;

/**
 * <p>
 * <b>{@link MessageErrorDTO}</b>
 * </p>
 * <p>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class MessageErrorDTO {

    private String msg;

    public MessageErrorDTO() {

    }

    public MessageErrorDTO(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
