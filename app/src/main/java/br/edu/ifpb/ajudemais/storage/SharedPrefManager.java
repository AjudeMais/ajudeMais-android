package br.edu.ifpb.ajudemais.storage;

import android.content.Context;
import android.content.SharedPreferences;

import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.dto.LatLng;

/**
 * <p>
 * <b>SharedPrefManager</b>
 * </p>
 * Classe utilitaria para armazenamento em preferências do app.
 * <p>
 * <p>
 * Gerencia informações constantemente usadas
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "authPrefs";
    private static final String SHARED_PREF_LOCATION = "br.edu.ifpb.ajudemais.location";

    private static final String KEY_ACCESS_TOKEN = "authToken";
    private static final String USER_SESSION_USERNAME = "userSessionUsername";
    private static final String USER_SESSION_MAIL = "userSessionMail";
    private static final String LOCATION_LAT = "location_lat";
    private static final String LOCATION_LONG = "location_lng";
    private static final String FCM_KEY = "fcm_key";

    private static Context context;

    private static SharedPrefManager instance;

    /**
     * construtor padrão
     *
     * @param context
     */
    public SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }

    /**
     * Recupera a instância da classe.
     *
     * @param context
     * @return
     */
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPrefManager(context);
        return instance;

    }

    /**
     * Salva token no arquivo de configuraçeõs do app.
     *
     * @param token
     * @return
     */
    public boolean storeToken(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        return true;
    }

    /**
     * Salva token do FCM no arquivo de configuraçeõs do app.
     *
     * @param token
     * @return
     */
    public boolean storageFcmToken(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FCM_KEY, token);
        editor.apply();
        return true;
    }

    /**
     * Guarda  a conta de acesso do usuário para o mesmo não precisar inserir suas credencias novamente
     *
     * @param conta
     * @return
     */
    public boolean storeUser(Conta conta) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_SESSION_USERNAME, conta.getUsername());
        editor.putString(USER_SESSION_MAIL, conta.getEmail());
        editor.apply();
        return true;
    }

    /**
     * Salva a localização do device.
     *
     * @param latLng
     * @return
     */
    public boolean storeLatLng(LatLng latLng) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_LOCATION, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCATION_LAT, Double.toString(latLng.getLatitude()));
        editor.putString(LOCATION_LONG, Double.toString(latLng.getLongitude()));
        editor.apply();

        return true;
    }

    /**
     * Obtem token
     *
     * @return token salvo nas preferencias do app.
     */
    public String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    /**
     * Obtem token
     *
     * @return token salvo nas preferencias do app.
     */
    public String getFcmToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FCM_KEY, null);
    }

    /**
     * Recupera Usuário.
     *
     * @return
     */
    public Conta getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(USER_SESSION_USERNAME, null);
        String mail = sharedPreferences.getString(USER_SESSION_MAIL, null);

        Conta conta = new Conta();
        conta.setUsername(username);
        conta.setEmail(mail);
        return conta;
    }

    /**
     * Retorna a última localização salva.
     *
     * @return
     */
    public LatLng getLocation() {
        LatLng latLng = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_LOCATION, Context.MODE_PRIVATE);
        String lat = sharedPreferences.getString(LOCATION_LAT, null);
        String lgn = sharedPreferences.getString(LOCATION_LONG, null);

        if (lat != null && lgn != null) {
            latLng = new LatLng();
            latLng.setLatitude(Double.parseDouble(lat));
            latLng.setLongitude(Double.parseDouble(lgn));
        }

        return latLng;
    }

    /**
     * Limpa dados armazenados da conta do usuário e localização no device.
     */
    public void clearSharedPrefs() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_LOCATION, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
