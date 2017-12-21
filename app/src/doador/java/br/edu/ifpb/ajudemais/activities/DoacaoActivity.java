package br.edu.ifpb.ajudemais.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.FindByMyLocationActualTask;
import br.edu.ifpb.ajudemais.asyncTasks.GetTempImageTask;
import br.edu.ifpb.ajudemais.asyncTasks.RemoveTmpImageTask;
import br.edu.ifpb.ajudemais.asyncTasks.UploadImageTask;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.domain.Categoria;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.DonativoCampanha;
import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.domain.Imagem;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission;
import br.edu.ifpb.ajudemais.utils.CustomToast;

import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.MY_PERMISSIONS_REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.REQUEST_CAMERA;
import static br.edu.ifpb.ajudemais.permissionsPolyce.AccessCameraAndGalleryDevicePermission.SELECT_FILE;


/**
 * <p>
 * <b>{@link DoacaoActivity}</b>
 * </p>
 * <p>
 * Activity para finalizar criação de conta utilizando o facebook
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/amslv">Ana Silva</a>
 */
public class DoacaoActivity extends LocationActivity implements View.OnClickListener, Validator.ValidationListener {

    private static int PROFILE_PIC_COUNT = 0;
    private FindByMyLocationActualTask findByMyLocationActualTask;
    private Donativo donativo;
    private Button btnAddAddress;
    private Button btnKeep;
    private CardView cardView;
    private ImageView img1, img2, img3;
    private HashMap<String, byte[]> donativeImages;
    private AccessCameraAndGalleryDevicePermission permissionSelectImagem;
    private String keyImg;
    private Toolbar mToolbar;
    private Validator validator;
    private TextView tvEndereco;
    private UploadImageTask uploadImageTask;
    private GetTempImageTask getTempImageTask;
    private RemoveTmpImageTask removeTmpImageTask;
    private Campanha campanha;

    @Order(3)
    @NotEmpty(messageResId = R.string.msgNameNotInformed)
    private TextInputEditText edtNome;

    private TextInputEditText edtDescription;

    @Order(1)
    @Min(value = 1, messageResId = R.string.msgNumberInvalid, sequence = 3)
    @NotEmpty(messageResId = R.string.msgQuantidadeNotInformed, sequence = 1)
    private TextInputEditText edtQuantidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docao);
        initGoogleAPIClient();

        init();
        donativo = (Donativo) getIntent().getSerializableExtra("Donativo");

        if (getIntent().hasExtra("Campanha")){
            campanha = (Campanha) getIntent().getSerializableExtra("Campanha");
        }


        if (donativo == null) {
            donativo = new Donativo();
        } else {
            setValueDonativoInForm();
        }

        if (donativeImages == null) {
            donativeImages = new HashMap<>();
        }

        if (getIntent().hasExtra("Endereco")) {
            Endereco endereco = (Endereco) getIntent().getSerializableExtra("Endereco");
            donativo.setEndereco(endereco);
            setAtrAddressIntoCard(endereco);
        }

        if (getIntent().hasExtra("Categoria")) {
            Categoria categoria = (Categoria) getIntent().getSerializableExtra("Categoria");
            donativo.setCategoria(categoria);
        }

        if (donativo.getFotosDonativo() != null) {
            if (donativo.getFotosDonativo() != null) {
                for (int i = 0; donativo.getFotosDonativo().size() > i; i++) {
                    if (donativo.getFotosDonativo().get(i).getNome() != null) {

                        executeGetTempImageTask(donativo.getFotosDonativo().get(i).getNome(), i);
                    }
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        permissionSelectImagem = new AccessCameraAndGalleryDevicePermission(this);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        btnAddAddress = (Button) findViewById(R.id.btnAddAddress);
        btnKeep = (Button) findViewById(R.id.btnKeepAgendamento);
        edtDescription = (TextInputEditText) findViewById(R.id.edtDescription);
        edtNome = (TextInputEditText) findViewById(R.id.edtNome);
        tvEndereco = (TextView) findViewById(R.id.tvEnderecoColeta);
        edtQuantidade = (TextInputEditText) findViewById(R.id.edtQuantidade);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        validator = new Validator(DoacaoActivity.this);
        validator.setValidationListener(this);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);

        btnAddAddress.setOnClickListener(this);
        btnKeep.setOnClickListener(this);

    }

    /**
     * Seta endereço no cardview
     */
    private void setAtrAddressIntoCard(Endereco endereco) {
        btnAddAddress.setVisibility(View.GONE);
        cardView = (CardView) findViewById(R.id.card_address);
        cardView.setVisibility(View.VISIBLE);
        tvEndereco.setVisibility(View.VISIBLE);
        ((TextView) cardView.findViewById(R.id.tv_logradouro_name)).setText(endereco.getLogradouro());
        ((TextView) cardView.findViewById(R.id.tv_bairro)).setText(endereco.getBairro());
        ((TextView) cardView.findViewById(R.id.tv_number)).setText(endereco.getNumero());
        ((TextView) cardView.findViewById(R.id.tv_cep_name)).setText(endereco.getCep());
        ((TextView) cardView.findViewById(R.id.tv_city)).setText(endereco.getLocalidade());
        ((TextView) cardView.findViewById(R.id.tv_uf_name)).setText(endereco.getUf());
        cardView.setOnClickListener(this);

    }

    /**
     * Dialog para seleção da opção para Editar ou  remover endereço.
     */
    private void openDialogEditOrRemoveddress() {
        final CharSequence[] items = {getString(R.string.tv_edit), this.getString(R.string.removeAddress), getString(R.string.cancelar)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectOption));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.tv_edit))) {
                    setValueInDonativo();
                    Intent intent = new Intent(DoacaoActivity.this, AddEnderecoActivity.class);
                    intent.putExtra("Donativo", donativo);
                    intent.putExtra("Campanha", campanha);
                    startActivity(intent);
                } else if (items[item].equals(getString(R.string.removeAddress))) {
                    cardView.setVisibility(View.GONE);
                    btnAddAddress.setVisibility(View.VISIBLE);
                    donativo.setEndereco(null);
                    CustomToast.getInstance(DoacaoActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.endereco_removed));
                } else if (items[item].equals(getString(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
     * Seta propriedades e executa Task para get location
     */
    private void runTaskLocation() {
        startLocationUpdates();

        if (mLastLocation == null) {
            mLastLocation = getLocation();
        }

        if (mLastLocation != null) {
            findByMyLocationActualTask = new FindByMyLocationActualTask(DoacaoActivity.this, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            findByMyLocationActualTask.delegate = new AsyncResponse<Endereco>() {
                @Override
                public void processFinish(Endereco output) {
                    setValueInDonativo();
                    Intent intent = new Intent(DoacaoActivity.this, AddEnderecoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Endereco", output);
                    intent.putExtra("Donativo", donativo);
                    intent.putExtra("Campanha", campanha);
                    startActivity(intent);
                }
            };
            findByMyLocationActualTask.execute();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddAddress) {
            checkPermissions();

        } else if (v.getId() == R.id.img1) {
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
        } else if (v.getId() == R.id.btnKeepAgendamento) {
            validator.validate();
        } else if (v.getId() == R.id.card_address) {
            openDialogEditOrRemoveddress();
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
                    CustomToast.getInstance(DoacaoActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.removed_image));
                } else if (items[item].equals(getString(R.string.cancelar))) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    /**
     * @param outState
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edtNome", edtNome.getText().toString().trim());
        outState.putString("edtDescription", edtDescription.getText().toString().trim());
        outState.putString("quantidade", edtQuantidade.getText().toString().trim());

        outState.putSerializable("Donativo", donativo);
        outState.putSerializable("Campanha", campanha);
        outState.putSerializable("images", donativeImages);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        donativeImages = (HashMap<String, byte[]>) savedState.getSerializable("images");
        donativo = (Donativo) savedState.getSerializable("Donativo");
        campanha = (Campanha) savedState.getSerializable("Campanha");
        String nome = savedState.getString("edtNome");
        String desciption = savedState.getString("edtDescription");
        String quantidade = savedState.getString("quantidade");
        edtNome.setText(nome);
        edtDescription.setText(desciption);
        edtQuantidade.setText(quantidade);
        loadingImages();

    }

    /**
     * Carrega imagens salvas;
     */
    private void loadingImages() {
        if (donativeImages != null) {
            if (donativeImages.get("img1") != null) {
                img1.setImageBitmap(androidUtil.convertBytesInBitmap(donativeImages.get("img1")));
            }

            if (donativeImages.get("img2") != null) {
                img2.setImageBitmap(androidUtil.convertBytesInBitmap(donativeImages.get("img2")));
            }

            if (donativeImages.get("img3") != null) {
                img3.setImageBitmap(androidUtil.convertBytesInBitmap(donativeImages.get("img3")));
            }
        }
    }

    /**
     * Executa Asycn Task para recuperar imagens do donativo salvas em Temp.
     *
     * @param namePhoto
     */
    private void executeGetTempImageTask(String namePhoto, final int position) {
        getTempImageTask = new GetTempImageTask(this, namePhoto);
        getTempImageTask.delegate = new AsyncResponse<byte[]>() {
            @Override
            public void processFinish(byte[] output) {
                Bitmap photo = androidUtil.convertBytesInBitmap(output);
                ImageView imageView;
                if (position == 0) {
                    imageView = img1;
                } else if (position == 1) {
                    imageView = img2;

                } else {
                    imageView = img3;
                }

                imageView.setImageBitmap(photo);

            }
        };

        getTempImageTask.execute();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
                runTaskLocation();
            }


        }

        Bitmap photo = null;

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            selectedImage = data.getData();
            photo = capturePhotoUtils.getImageResized(this, selectedImage);
        }

        if (photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            executeUpdateImageTask(imageBytes);
            donativeImages.put(keyImg, imageBytes);
            selectImageClicked().setImageBitmap(photo);
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
                addImageInListImages(output);
            }
        };

        uploadImageTask.execute();
    }

    /**
     * Inicializa lista de imagens de donativo para evitar null.
     */
    private void initListImage() {
        donativo.setFotosDonativo(new ArrayList<Imagem>());
        donativo.getFotosDonativo().add(0, new Imagem());
        donativo.getFotosDonativo().add(1, new Imagem());
        donativo.getFotosDonativo().add(2, new Imagem());

    }

    /**
     * Add image na lista de imagens
     */
    private void addImageInListImages(Imagem output) {
        if (donativo.getFotosDonativo() != null && donativo.getFotosDonativo().get(getPostionClickedImage()) != null) {
            if (donativo.getFotosDonativo().get(getPostionClickedImage()).getNome() != null) {
                removeTmpImageTask = new RemoveTmpImageTask(this, donativo.getFotosDonativo().get(getPostionClickedImage()).getNome());
                removeTmpImageTask.execute();
            }
            setValuesInImage(output);
        } else {
            initListImage();
            setValuesInImage(output);
        }
    }

    private void setValuesInImage(Imagem output) {
        donativo.getFotosDonativo().get(getPostionClickedImage()).setNome(output.getNome());
        donativo.getFotosDonativo().get(getPostionClickedImage()).setId(output.getId());
        donativo.getFotosDonativo().get(getPostionClickedImage()).setContentType(output.getContentType());

    }


    /**
     * Recupera a posição da image que foi clicada.
     *
     * @return
     */
    private Integer getPostionClickedImage() {
        if (keyImg.equals("img1")) {
            return 0;
        } else if (keyImg.equals("img2")) {
            return 1;

        } else {
            return 2;
        }
    }

    /**
     * Set os valores do formulário no donativo.
     */
    public void setValueInDonativo() {
        if (edtNome.getText().toString().trim().length() > 0) {
            donativo.setNome(edtNome.getText().toString().trim());
        }

        if (edtDescription.getText().toString().trim().length() > 0) {
            donativo.setDescricao(edtDescription.getText().toString().trim());
        }

        if (edtQuantidade.getText().toString().trim().length()>0){
            donativo.setQuantidade(Integer.parseInt(edtQuantidade.getText().toString().trim()));
        }
    }


    /**
     * Set os valores de donativo no formulário.
     */
    public void setValueDonativoInForm() {
        if (donativo.getNome() != null) {
            edtNome.setText(donativo.getNome());
        }

        if (donativo.getDescricao() != null) {
            edtDescription.setText(donativo.getDescricao());
        }

        if (donativo.getQuantidade() != null){
            edtQuantidade.setText(Integer.toString(donativo.getQuantidade()));
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
     * remove imagens que não existem da lista de imagens do donativo.
     */
    private void validateListImages() {
        List<Imagem> imagens = new ArrayList<>();
        if (donativo.getFotosDonativo() != null) {
            for (Imagem imagem : donativo.getFotosDonativo()) {
                if (imagem.getNome() != null) {
                    imagens.add(imagem);
                }
            }
        }
        if (imagens.size()>0) {
            donativo.setFotosDonativo(imagens);
        }else {
            donativo.setFotosDonativo(null);
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (donativo.getEndereco() != null) {
            validateListImages();
            setValueInDonativo();
            Intent intent = new Intent(DoacaoActivity.this, AgendamentoDoacaoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Donativo", donativo);
            intent.putExtra("Campanha", campanha);
            startActivity(intent);
        } else {
            CustomToast.getInstance(this).createSuperToastSimpleCustomSuperToast(getString(R.string.addres_not_informed));
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
                view.requestFocus();
            } else {
                CustomToast.getInstance(DoacaoActivity.this).createSuperToastSimpleCustomSuperToast(message);
            }
        }
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
                    Toast.makeText(DoacaoActivity.this, getString(R.string.permissionDeniedAccessCamera), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * @param locationSettingsResult
     */
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                onLocationChanged(getLocation());
                runTaskLocation();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(DoacaoActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }


}