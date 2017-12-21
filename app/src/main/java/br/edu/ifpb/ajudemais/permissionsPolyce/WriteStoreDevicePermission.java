package br.edu.ifpb.ajudemais.permissionsPolyce;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * <p>
 * <b>{@link WriteStoreDevicePermission}</b>
 * </p>
 * <p>
 * <p>
 * Gerencia Permissão para gravar arquivos no sistema de arquivo do device.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class WriteStoreDevicePermission {

    private Context context;
    public static final int MY_PERMISSIONS_REQUEST_STORE_PERMISSION = 14;


    public WriteStoreDevicePermission(Context context) {
        this.context = context;
    }


    /**
     * Checa se permissão Para Gravar arquivo está aceita, Se não pede permissão
     *
     * @return
     */
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ActivityCompat.requestPermissions(((Activity)context),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_STORE_PERMISSION);
                    return  false;
                } else {
                    ActivityCompat.requestPermissions(((Activity)context),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_STORE_PERMISSION);
                    return  false;

                }
            }else {
                return true;
            }
        } else {
            return true;
        }
    }



}
