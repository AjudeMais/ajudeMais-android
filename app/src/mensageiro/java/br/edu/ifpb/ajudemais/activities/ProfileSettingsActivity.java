package br.edu.ifpb.ajudemais.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingMensageiroTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.ChangePasswordTask;
import br.edu.ifpb.ajudemais.asyncTasks.UploadImageTask;
import br.edu.ifpb.ajudemais.domain.Imagem;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.fragments.ProfileSettingsFragment;
import br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.CustomToast;

import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.MY_PERMISSIONS_REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.SELECT_FILE;

public class ProfileSettingsActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;

    private Button btnChangePassword;
    private ImageView imageView;
    private NestedScrollView nestedScrollView;
    private Mensageiro mensageiro;

    private UploadImageTask uploadImageTask;
    private UpdateMensageiroTask updateMensageiroTask;

    private AccessCameraAndGalleryDevicePermission permissionSelectImagem;
    private LoadingMensageiroTask loadingMensageiroTask;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        if (savedInstanceState != null) {
            mensageiro = (Mensageiro) savedInstanceState.getSerializable("Mensageiro");
        }

        init();

        executeLoadingMensageiroTask();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ProfileSettingsActivity.this, CreateMensageiroAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Mensageiro", mensageiro);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(this);

    }


    /**
     * Inicializa e executa a AsycnTask para carregar o mensageiro Logado
     */
    private void executeLoadingMensageiroTask() {

        if (mensageiro == null) {
            loadingMensageiroTask = new LoadingMensageiroTask(this, SharedPrefManager.getInstance(this).getUser().getUsername());

            loadingMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
                @Override
                public void processFinish(Mensageiro output) {
                    mensageiro = output;
                    collapsingToolbarLayout.setTitle(mensageiro.getConta().getUsername());
                    ProfileSettingsFragment fragment = new ProfileSettingsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Mensageiro", mensageiro);
                    fragment.setArguments(bundle);

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.editprofile_fragment, fragment);
                    fragmentTransaction.commit();
                    nestedScrollView.setVisibility(View.VISIBLE);
                    fab.setEnabled(true);
                }
            };

            loadingMensageiroTask.execute();
        }
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
                    CustomToast.getInstance(ProfileSettingsActivity.this).createSimpleCustomSuperToastActivity(getString(R.string.permissionDeniedAccessCamera));

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

            executeUploadImageTask(imageBytes);

            imageView.setImageBitmap(photo);
            capturePhotoUtils.saveToInternalStorage(photo);

        }
    }

    /**
     * Inicializa e executa AsycnTask para fazer upload de image do perfil do mensageiro.
     *
     * @param imageBytes
     */
    private void executeUploadImageTask(byte[] imageBytes) {
        uploadImageTask = new UploadImageTask(this, imageBytes);
        uploadImageTask.delegate = new AsyncResponse<Imagem>() {
            @Override
            public void processFinish(Imagem output) {
                if (mensageiro.getFoto() != null) {
                    mensageiro.getFoto().setNome(output.getNome());
                } else {
                    mensageiro.setFoto(output);
                }
                updateMensageiroTask = new UpdateMensageiroTask(ProfileSettingsActivity.this, mensageiro);
                updateMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
                    @Override
                    public void processFinish(Mensageiro output) {
                        mensageiro = output;
                        CustomToast.getInstance(ProfileSettingsActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.imageUpdated));
                    }
                };

                updateMensageiroTask.execute();
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
                                CustomToast.getInstance(ProfileSettingsActivity.this).createSimpleCustomSuperToastActivity(getString(R.string.passwordLengthNotPermission));
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("Mensageiro", mensageiro);

    }
}
