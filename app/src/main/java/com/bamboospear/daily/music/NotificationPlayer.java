package com.bamboospear.daily.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bamboospear.daily.MainActivity;
import com.bamboospear.daily.R;

public class NotificationPlayer {
    private static final String GROUP_TED_PARK = "tedPark";
    MusicService musicService;
    NotificationManager notificationManager;
    NotificationManagerBuilder mNotificationManagerBuilder;
    private boolean isForeground;



    public NotificationPlayer(MusicService service) {
        musicService = service;
        notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void updateNotificationPlayer() {
        cancel();
        mNotificationManagerBuilder = new NotificationManagerBuilder();
        mNotificationManagerBuilder.execute();
    }

    private void cancel() {
        if (mNotificationManagerBuilder != null) {
            mNotificationManagerBuilder.cancel(true);
            mNotificationManagerBuilder = null;
        }
    }

    public void removeNotificationPlayer() {
        cancel();
        musicService.stopForeground(true);
        isForeground = false;
    }

    private class NotificationManagerBuilder extends AsyncTask<Void, Void, Notification> {
        private RemoteViews mRemoteViews;
        private NotificationCompat.Builder mNotificationBuilder;
        private PendingIntent mMainPendingIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*            String chnnelId = "1";
            String channelName = "Channel Name";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(chnnelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) musicService.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            Intent mainActivity = new Intent(musicService, MainActivity.class);
            mMainPendingIntent = PendingIntent.getActivity(musicService, 0, mainActivity, 0);
            mRemoteViews = createRemoteView(R.layout.notification_player);
            mNotificationBuilder = new NotificationCompat.Builder(musicService, "channel_id");
            mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews);

            Notification notification = mNotificationBuilder.build();
            notification.priority = Notification.PRIORITY_MAX;
            notification.contentIntent = mMainPendingIntent;
            if (!isForeground) {
                isForeground = true;
                // 서비스를 Foreground 상태로 만든다
                musicService.startForeground(1, notification);
            }
        }

        @Override
        protected Notification doInBackground(Void... params) {
            mNotificationBuilder.setContent(mRemoteViews);
            mNotificationBuilder.setContentIntent(mMainPendingIntent);
            mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
            Notification notification = mNotificationBuilder.build();
            updateRemoteView(mRemoteViews, notification);
            return notification;
        }

        @Override
        protected void onPostExecute(Notification notification) {
            super.onPostExecute(notification);
            try {
                notificationManager.notify(1, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private RemoteViews createRemoteView(int layoutId) {
            RemoteViews remoteView = new RemoteViews(musicService.getPackageName(), layoutId);
            Intent actionPlay = new Intent(CommandActions.TOGGLE_PLAY);
            Intent actionClose = new Intent(CommandActions.CLOSE);
            PendingIntent toggle_play = PendingIntent.getService(musicService, 0, actionPlay, 0);
            PendingIntent close = PendingIntent.getService(musicService, 0, actionClose, 0);

            remoteView.setOnClickPendingIntent(R.id.btn_play, toggle_play);
            remoteView.setOnClickPendingIntent(R.id.btn_close, close);
            return remoteView;
        }

        private void updateRemoteView(RemoteViews remoteViews, Notification notification) {
            if (musicService.checkPlaying()) {
                remoteViews.setImageViewResource(R.id.btn_play, R.drawable.stop);
            } else {
                remoteViews.setImageViewResource(R.id.btn_play, R.drawable.play);
            }
/*             String title = musicService.getAudioItem().mTitle;
            remoteViews.setTextViewText(R.id.txt_title, title);
            Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), musicService.getAudioItem().mAlbumId);
            Picasso.with(musicService).load(albumArtUri).error(R.drawable.empty_albumart).into(remoteViews, R.id.img_albumart, NOTIFICATION_PLAYER_ID, notification);*/
        }
    }
}
