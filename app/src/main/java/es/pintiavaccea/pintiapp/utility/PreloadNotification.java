package es.pintiavaccea.pintiapp.utility;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import es.pintiavaccea.pintiapp.R;

import static android.content.ContentValues.TAG;

/**
 * Created by madri on 24/04/2017.
 */

public class PreloadNotification {

    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;

    public static void buildNotification(Activity activity) {
        mNotifyManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(activity);
        mBuilder.setContentTitle("Precarga de datos")
                .setContentText("Descarga en progreso")
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon);
        mBuilder.setProgress(0, 0, true);
    }

    public static void startNotification(){
        mNotifyManager.notify(1, mBuilder.build());
    }

    public static void finishNotification(){
        mBuilder.setContentText("Download complete")
                // Removes the progress bar
                .setProgress(0, 0, false);
        mNotifyManager.notify(1, mBuilder.build());
    }
}
