package br.edu.ifpb.ajudemais.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingDoadorTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.ChangePasswordTask;
import br.edu.ifpb.ajudemais.asyncTasks.FacebookProfilePictureTask;
import br.edu.ifpb.ajudemais.asyncTasks.UploadImageTask;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.Imagem;
import br.edu.ifpb.ajudemais.fragments.ProfileSettingsFragment;
import br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission;
import br.edu.ifpb.ajudemais.permissionsPolyce.WriteStoreDevicePermission;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.CapturePhotoUtils;
import br.edu.ifpb.ajudemais.utils.CustomToast;

import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.MY_PERMISSIONS_REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.SELECT_FILE;

public class ProfileSettingsActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private Doador doador;
    private Button btnChangePassword;
    private ImageView imageView;
    private NestedScrollView nestedScrollView;
    private UploadImageTask uploadImageTask;
    private AccessCameraAndGalleryDevicePermission permissionSelectImagem;
    private LoadingDoadorTask loadingDoadorTask;
    private UpdateDoadorTask updateDoadorTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        init();
        executeLoadingDoadorTask();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, CreateAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Doador", doador);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(this);

        if (AccessToken.getCurrentAccessToken() != null) {
            btnChangePassword.setVisibility(View.GONE);
        }
    }

    /**
     * Executa a AsycnTask para carregar dodador logado.
     */
    private void executeLoadingDoadorTask() {
        loadingDoadorTask = new LoadingDoadorTask(this, SharedPrefManager.getInstance(ProfileSettingsActivity.this).getUser().getUsername());
        loadingDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(Doador output) {
                doador = output;
                setProprieties();
            }
        };

        loadingDoadorTask.execute();
    }

    @Override
    public void init() {
        initProperties();

        permissionSelectImagem = new AccessCameraAndGalleryDevicePermission(this);

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.netScroll);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_edit_profile);
        collapsingToolbarLayout.setTitle(getString(R.string.loading));

        imageView = (ImageView) findViewById(R.id.image_profile);
        Bitmap bitmap = capturePhotoUtils.loadImageFromStorage();

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            executeLoadingImageProfileTask();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionSelectImagem.openDialogSelectImage();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fabEditAccount);
        fab.setEnabled(false);

    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(ProfileSettingsActivity.this, getString(R.string.permissionDeniedAccessCamera), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ProfileSettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_seletec_image:
                permissionSelectImagem.openDialogSelectImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            File imageFile = capturePhotoUtils.getTempFile(this);
            selectedImage = data.getData();
            photo = capturePhotoUtils.getImageResized(this, selectedImage);
        }

        if (photo != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            executeUpdateImageTask(imageBytes);

            imageView.setImageBitmap(photo);
            capturePhotoUtils.saveToInternalStorage(photo);

        }
    }

    /**
     * Executa asycn task para atualizar imagem do doador
     *
     * @param imageBytes
     */
    private void executeUpdateImageTask(byte[] imageBytes) {
        uploadImageTask = new UploadImageTask(this, imageBytes);
        uploadImageTask.delegate = new AsyncResponse<Imagem>() {
            @Override
            public void processFinish(Imagem output) {

                if (doador.getFoto() != null) {
                    doador.getFoto().setNome(output.getNome());
                } else {
                    doador.setFoto(output);
                }

                updateDoadorTask = new UpdateDoadorTask(ProfileSettingsActivity.this, doador);
                updateDoadorTask.delegate = new AsyncResponse<Doador>() {
                    @Override
                    public void processFinish(Doador output) {
                        doador = output;
                        CustomToast.getInstance(ProfileSettingsActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.imageUpdated));
                    }
                };
                updateDoadorTask.execute();

            }
        };

        uploadImageTask.execute();
    }


    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile_menu, menu);
        return true;
    }


    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChangePassword) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_change_password, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
            alertDialogBuilderUserInput.setView(mView);

            final TextInputEditText password = (TextInputEditText) mView.findViewById(R.id.edtPassword);
            final TextInputEditText newPassword = (TextInputEditText) mView.findViewById(R.id.edtNewPassword);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton(R.string.btn_change_password, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                            if (newPassword.getText().toString().trim().length() > 5) {
                                new ChangePasswordTask(ProfileSettingsActivity.this, password.getText().toString().trim(), newPassword.getText().toString().trim()).execute();
                            } else {
                                CustomToast.getInstance(ProfileSettingsActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.passwordLengthNotPermission));
                            }
                        }
                    })

                    .setNegativeButton(R.string.cancelar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("doador", doador);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        doador = (Doador) savedState.getSerializable("doador");
        setProprieties();
    }


    /**
     * Set propriedades após executar task.
     */
    public void setProprieties() {
        if (!isDestroyed()) {
            collapsingToolbarLayout.setTitle(doador.getConta().getUsername());

            if (AccessToken.getCurrentAccessToken() != null) {
                collapsingToolbarLayout.setTitle(Profile.getCurrentProfile().getName());
            }

            ProfileSettingsFragment fragment = new ProfileSettingsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("doador", doador);
            fragment.setArguments(bundle);

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.editprofile_fragment, fragment);
            fragmentTransaction.commit();
            nestedScrollView.setVisibility(View.VISIBLE);
            fab.setEnabled(true);
        }
    }

    /**
     * Executa Asycn task para recuperar foto do facebook
     *
     */
    private void executeLoadingImageProfileTask() {

        if (AccessToken.getCurrentAccessToken() != null) {
            FacebookProfilePictureTask loadingImageFbTask = new FacebookProfilePictureTask();
            loadingImageFbTask.delegate = new AsyncResponse<Bitmap>() {
                @Override
                public void processFinish(Bitmap output) {
                    if (output != null) {
                        imageView.setImageBitmap(output);
                    }
                }
            };
            loadingImageFbTask.execute();


        }
    }

}
