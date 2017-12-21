package br.edu.ifpb.ajudemais.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * <p>
 * <b>{@link CapturePhotoUtils}</b>
 * </p>
 * <p>
 * <p>
 * Utilitário para salvar uma image local
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>**/

public class CapturePhotoUtils {

    private Context context;
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;
    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;

    public CapturePhotoUtils(Context context){
        this.context = context;
    }

    /**
     * Salva image no Storage do device.
     * @param image
     */
    public void saveToInternalStorage(Bitmap image){
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("AJUDEMAIS",
                    "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("AJUDEMAIS", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("AJUDEMAIS", "Error accessing file: " + e.getMessage());
        }

    }

    /**
     * Remove image armazenada no storage do device.
     * @return
     */
    public boolean deleteImageProfile(){
        File file =new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files", "profilePhoto.png");

       return file.delete();

    }

    /**
     * Recupera Imagem do perfil.
     * @return
     */
    private  File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String mImageName="profilePhoto.png";
        return new File(mediaStorageDir.getPath() + File.separator + mImageName);
    }

    /**
     * Carrega Image do Store do device
     * @return
     */
    public Bitmap loadImageFromStorage()
    {

        try {
            File file =new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    +  context.getApplicationContext().getPackageName()
                    + "/Files", "profilePhoto.png");
            Bitmap profilePhoto = BitmapFactory.decodeStream(new FileInputStream(file));
            return  profilePhoto;
        } catch (FileNotFoundException e) {
            return null;
        }catch (Exception e){
            return null;
        }


    }
    /**
     * @param context
     * @param theUri
     * @param sampleSize
     * @return
     */
    private  Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d("AjudeMais", options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }




    /**
     * Get file save.
     *
     * @param context
     * @return
     */
    public   File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), "tempImageProfile");
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }



    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    public  Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }
}
