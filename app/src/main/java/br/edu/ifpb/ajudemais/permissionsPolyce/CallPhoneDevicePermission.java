package br.edu.ifpb.ajudemais.permissionsPolyce;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link CallPhoneDevicePermission}/b>
 * </p>
 * <p>
 * <p>
 * Lida com as permissões para fazer um ligação por meio do app.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class CallPhoneDevicePermission {

    private Context context;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION = 15;


    public CallPhoneDevicePermission(Context context){
        this.context = context;
    }

    /**
     * Checa se permissão Para realizar uma ligação por meio do app está aceita, Se não pede permissão
     *
     * @return
     */
    public boolean isCallPhonePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context),
                        Manifest.permission.CALL_PHONE)) {


                    ActivityCompat.requestPermissions(((Activity)context),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION);
                    return  false;
                } else {
                    ActivityCompat.requestPermissions(((Activity)context),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION);
                    return  false;

                }
            }else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void callPhone(String numeroTelefone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + numeroTelefone));
        try {
            context.startActivity(callIntent);
        } catch (Exception e) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(context.getString(R.string.errorNotCallPhone));
        }
    }


}

