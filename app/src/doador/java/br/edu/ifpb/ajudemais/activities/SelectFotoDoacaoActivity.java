package br.edu.ifpb.ajudemais.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission;

import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.SELECT_FILE;


public class SelectFotoDoacaoActivity extends BaseActivity implements View.OnClickListener{

    private HashMap<String, Bitmap> donativeImages;
    private static int PROFILE_PIC_COUNT = 0;
    private Donativo donativo;
    private ImageView img1, img2, img3;
    private AccessCameraAndGalleryDevicePermission permissionSelectImagem;
    private String keyImg;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_foto_doacao);

        init();
    }

    @Override
    public void init() {
        initProperties();
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        permissionSelectImagem = new AccessCameraAndGalleryDevicePermission(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);

        if (donativeImages == null) {
            donativeImages = new HashMap<>();
        }

    }

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.img1) {
            keyImg = "img1";
            if (donativeImages.get(keyImg) != null) {
                openDialogSelectEditImage();
            } else
                permissionSelectImagem.openDialogSelectImage();
        } else if (v.getId() == R.id.img2) {
            keyImg = "img2";
            if (donativeImages.get(keyImg) != null) {
                openDialogSelectEditImage();
            } else
                permissionSelectImagem.openDialogSelectImage();
        } else if (v.getId() == R.id.img3) {
            keyImg = "img3";
            if (donativeImages.get(keyImg) != null) {
                openDialogSelectEditImage();
            } else
                permissionSelectImagem.openDialogSelectImage();
        }
    }

    /**
     * Dialog para seleção de camera ou galeria para selecionar Imagem.
     */
    public void openDialogSelectEditImage() {
        final CharSequence[] items = {this.getString(R.string.TakePhoto), this.getString(R.string.gallery), this.getString(R.string.remove), this.getString(R.string.cancelar)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.selectPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.TakePhoto))) {
                    PROFILE_PIC_COUNT = 1;
                    if (permissionSelectImagem.isCameraPermissionGranted()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.gallery))) {
                    PROFILE_PIC_COUNT = 1;
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_FILE);

                } else if (items[item].equals(getString(R.string.remove))) {
                    donativeImages.remove(keyImg);
                    selectImageClicked().setImageDrawable(getDrawable(R.drawable.add));

                } else if (items[item].equals(getString(R.string.cancelar))) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        donativeImages = (HashMap<String, Bitmap>) savedState.getSerializable("images");
        donativo = (Donativo) savedState.getSerializable("Donativo");
        if (donativeImages != null) {
            if (donativeImages.get("img1") != null) {
                img1.setImageBitmap(donativeImages.get("img1"));
            } else if (donativeImages.get("img2") != null) {
                img2.setImageBitmap(donativeImages.get("img2"));
            } else {
                img3.setImageBitmap(donativeImages.get("img3"));
            }
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap photo = null;

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            selectedImage = data.getData();
            photo = capturePhotoUtils.getImageResized(this, selectedImage);
        }

        if (photo != null) {

            selectImageClicked().setImageBitmap(photo);

            donativeImages.put(keyImg, photo);

        }
    }


    /**
     * Retorna a ImageView Clicada de acordo com keyImg
     *
     * @return
     */
    private ImageView selectImageClicked() {
        if (keyImg != null) {
            if (keyImg.equals("img1")) {
                return img1;
            } else if (keyImg.equals("img2")) {
                return img2;
            } else {
                return img3;
            }
        }

        return null;
    }

    /**
     * @param outState
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Donativo", donativo);
        outState.putSerializable("images", donativeImages);
    }

    /**
     * Implementação para controlar operações na action bar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
