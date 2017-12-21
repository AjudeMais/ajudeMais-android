package br.edu.ifpb.ajudemais.activities;

import android.support.v7.app.AppCompatActivity;

import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CapturePhotoUtils;

/**
 * <p>
 * <b>{@link BaseActivity}</b>
 * </p>
 * <p>
 * <p>
 * Activity Base para reaproveitamento de código e padrões a serem seguidos.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected AndroidUtil androidUtil;
    protected CapturePhotoUtils capturePhotoUtils;

    /**
     * Inicializa algumas propriedades independentes de activities.
     */
    public void initProperties(){
        this.androidUtil = new AndroidUtil(this);
        this.capturePhotoUtils = new CapturePhotoUtils(this);
    }

    /**
     * Método para encapsular inicialização de propriedades nas Acticvities
     */
    public abstract void init();


}
