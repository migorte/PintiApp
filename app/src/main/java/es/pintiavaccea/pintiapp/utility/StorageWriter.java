package es.pintiavaccea.pintiapp.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Miguel on 07/07/2016.
 */
public class StorageWriter {

    //target to save
    public static Target getTarget(final String url, final Context context){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
                        File directory = cw.getDir("images", Context.MODE_PRIVATE);
                        File file = new File(directory + "/" + url);
//                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
//                        String path = Environment.getExternalStorageDirectory().getPath() + "/" + url;
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

}
