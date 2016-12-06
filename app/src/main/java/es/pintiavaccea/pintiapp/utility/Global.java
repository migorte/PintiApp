package es.pintiavaccea.pintiapp.utility;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

/**
 * Created by Miguel Ortega on 14/11/2016.
 *
 * Crea una instancia del builder de Picasso para que sea utilizado en toda la aplicación.
 */
public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);

        client.setCache(new com.squareup.okhttp.Cache(getApplicationContext().getCacheDir(), 30000000));

        Picasso sPicasso = new Picasso.Builder(getApplicationContext())
                .downloader(new OkHttpDownloader(client))
                .build();
        sPicasso.setIndicatorsEnabled(true);
        sPicasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(sPicasso);
    }
}
