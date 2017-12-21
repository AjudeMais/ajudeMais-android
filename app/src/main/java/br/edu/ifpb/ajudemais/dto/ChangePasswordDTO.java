package br.edu.ifpb.ajudemais.dto;


/**
 *
 * <p>
 * <b> {@link ChangePasswordDTO} </b>
 * </p>
 *
 * <p>
 * Classe utilizada para alteração de Senha.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ChangePasswordDTO {

    private String password;
    private String newPassword;

    public ChangePasswordDTO(){

    }

    public ChangePasswordDTO(String password, String newPassword){
        this.password = password;
        this.newPassword = newPassword;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }
    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
