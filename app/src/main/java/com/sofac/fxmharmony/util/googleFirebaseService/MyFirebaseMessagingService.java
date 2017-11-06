package com.sofac.fxmharmony.util.googleFirebaseService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PushMessage;
import com.sofac.fxmharmony.view.SplashActivity;


import timber.log.Timber;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    NotificationCompat.Builder builder;
    NotificationManager mNotificationManager;
    String pushMessageType = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.e("onMessageReceived -> " + remoteMessage.getData());

        if (remoteMessage.getData().size() > 0) {
            buildNotificationToShow(remoteMessage.getData().get("message"), remoteMessage.getData().get("date"), remoteMessage.getData().get("title"));

            //PushMessage newPushMessage = new PushMessage(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("date"));
            //newPushMessage.save();
        }

        if (remoteMessage.getNotification() != null) {
            Timber.i("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


    private void buildNotificationToShow(String messageText, String date, String title) {

        Intent notificationIntent = null;
        notificationIntent = new Intent(this, SplashActivity.class);

        mNotificationManager = (NotificationManager) this
                .getSystemService(this.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(Html.fromHtml(messageText).toString())
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
              /*  .setStyle(bigPictureStyle)*/
                .setAutoCancel(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 10,
                                notificationIntent, 0));

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        mNotificationManager.notify((int) (Math.random() * 100000), builder.build());

    }

//    private int getNotificationIcon() {
//        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.drawable.ic_stat_onesignal_default : R.drawable.icon;
//    }

}