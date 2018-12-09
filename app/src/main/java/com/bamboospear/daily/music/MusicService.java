package com.bamboospear.daily.music;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bamboospear.daily.R;
import com.bamboospear.daily.music.CommandActions;
import com.bamboospear.daily.music.NotificationPlayer;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private NotificationPlayer notificationPlayer;
    boolean isStart;
    NotificationManager notificationManager;
    ImageView imageView;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.test);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                updateNotificationPlayer();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                updateNotificationPlayer();
            }
        });
        //createNotification();
        notificationPlayer = new NotificationPlayer(this);
        Log.e("Service test", "onCreate() 호출!");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service test", "onStartCommand() 호출!");
/*        if (intent.getAction().equals("andbook.example.PLAYMUSIC")) {
            mediaPlayer.start();
            Log.e("Service test", "onStartCommand() 호출!");
            //new MusicThread().start();
        }*/
        if (intent != null) {
            String action = intent.getAction();
            if (CommandActions.TOGGLE_PLAY.equals(action)) {
//                Toast.makeText(this, "TOGGLE_PLAY 클릭", Toast.LENGTH_SHORT).show();
                isPlaying();
                updateNotificationPlayer();
                Intent intent1 = new Intent("test");
                sendBroadcast(intent1);
            } else if (CommandActions.CLOSE.equals(action)) {
                Toast.makeText(this, "CLOSE 클릭", Toast.LENGTH_SHORT).show();
                notificationPlayer.removeNotificationPlayer();
                mediaPlayer.pause();
                isStart = !isStart;
//                this.onDestroy();
            } else if (intent.getAction().equals("andbook.example.PLAYMUSIC") && isStart) {
                mediaPlayer.start();
                updateNotificationPlayer();
                isStart = !isStart;
            } else if (intent.getAction().equals("andbook.example.STOPMUSIC")) {
                mediaPlayer.pause();
                updateNotificationPlayer();
                isStart = !isStart;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service test", "onDestory() 호출!");
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void updateNotificationPlayer() {
        if (notificationPlayer != null) {
            notificationPlayer.updateNotificationPlayer();
        }
    }

    public void isPlaying() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }
    }

    public boolean checkPlaying() {
        return mediaPlayer.isPlaying();
    }
/*    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알림 제목");
        builder.setContentText("알람 세부 텍스트");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }*/

}

/*class MusicThread extends Thread {
    @Override
    public void run() {
        Log.e("Thread test", "run() 실행!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("Thread test", "run() 종료!");
    }
}*/
