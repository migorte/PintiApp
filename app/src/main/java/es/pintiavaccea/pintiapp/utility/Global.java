package es.pintiavaccea.pintiapp.utility;

/**
 * Created by madri on 14/11/2016.
 */

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
//        builder.downloader(new OkHttpDownloader(new OkHttpClient()));
//        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
//        built.setLoggingEnabled(true);
//        Picasso.setSingletonInstance(built);

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
