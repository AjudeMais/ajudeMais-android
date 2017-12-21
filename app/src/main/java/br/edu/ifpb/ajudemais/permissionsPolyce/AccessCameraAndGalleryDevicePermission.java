package br.edu.ifpb.ajudemais.permissionsPolyce;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import br.edu.ifpb.ajudemais.R;

/**
 * <p>
 * <b>{@link AccessCameraAndGalleryDevicePermission}</b>
 * </p>
 * <p>
 * <p>
 * Fornece mecanismos para permitir o aplicativo utilizar a camera do device com a permissão do usuário
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class AccessCameraAndGalleryDevicePermission {

    private Context context;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 11;
    public static final int MY_PERMISSIONS_GRANTED_CAMERA = 12;
    public int PROFILE_PIC_COUNT = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 13;

    public AccessCameraAndGalleryDevicePermission (Context context){
        this.context = context;
    }

    /**
     * Checa se permissão Para Gravar arquivo está aceita, Se não pede permissão
     *
     * @return
     */
    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context),
                        Manifest.permission.CAMERA)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    //temporário
                    ActivityCompat.requestPermissions(((Activity)context),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                    return  false;
                } else {
                    ActivityCompat.requestPermissions(((Activity)context),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                    return  false;

                }
            }else {
                return true;
            }
        } else {
            return true;
        }
    }


    /**
     * Dialog para seleção de camera ou galeria para selecionar Imagem.
     */
    public void openDialogSelectImage() {
        final CharSequence[] items = {context.getString(R.string.TakePhoto), context.getString(R.string.gallery), context.getString(R.string.cancelar)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.selectPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(context.getString(R.string.TakePhoto))) {
                    PROFILE_PIC_COUNT = 1;
                    if (isCameraPermissionGranted()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        ((Activity)context).startActivityForResult(intent, REQUEST_CAMERA);
                    }
                } else if (items[item].equals(context.getString(R.string.gallery))) {
                    PROFILE_PIC_COUNT = 1;
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    ((Activity)context).startActivityForResult(photoPickerIntent, SELECT_FILE);

                } else if (items[item].equals(context.getString(R.string.cancelar))) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
