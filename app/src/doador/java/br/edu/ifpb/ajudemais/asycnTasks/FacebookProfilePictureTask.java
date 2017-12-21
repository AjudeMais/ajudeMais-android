package br.edu.ifpb.ajudemais.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Profile;

import java.io.IOException;
import java.net.URL;


/**
 * <p>
 * <b>{@link FacebookProfilePictureTask]}</b>
 * </p>
 * <p>
 *  Task para buscar e retornar a foto de um usuário logado via facebook.
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/amslv">Ana Silva</a>
 */
public class FacebookProfilePictureTask extends AsyncTask<Void, Void, Bitmap> {

    public AsyncResponse<Bitmap> delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        URL imageURL;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        delegate.processFinish(bitmap);
    }
}
