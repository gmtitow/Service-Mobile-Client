package ru.bstu.it41.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.coordination.ChatFragment;
import ru.bstu.it41.service.models.Message;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessService";

    private static boolean notification = true;

    public static void setNotification(boolean notify) {
        notification = notify;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {


            Map<String, String> data = remoteMessage.getData();
            Message message = new Message();
            message.setMessage(data.get("message"));
            try {
                message.setDate(Message.mFormatFromServer.parse(data.get("date")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            message.setInput(Integer.parseInt(data.get("input")));
            message.setAuctionId(Integer.parseInt(data.get("auctionId")));
            message.setMessageId(Integer.parseInt(data.get("messageId")));

            if (!notification) {
                //sendNotification(message);
                EventBus.getDefault().post(message);
            } else {
                sendNotification(message);
            }
        }

        // Check if message contains a notification payload.
       /* if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(Message messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_LOGIN);
        /*intent.putExtra(ChatFragment.EXTRA_TENDER_ID,getPresenter().getStateModel().getCurrentTaskAndTender().getTender().getTenderId());
        intent.putExtra(ChatFragment.EXTRA_IS_CLIENT,true);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_textsms_white)
                        .setColor(getResources().getColor(R.color.secondaryColor))
                        .setContentTitle("Сообщение")
                        .setContentText(messageBody.getMessage())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Уведомление",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
