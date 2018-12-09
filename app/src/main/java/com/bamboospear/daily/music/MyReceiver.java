package com.bamboospear.daily.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bamboospear.daily.MainActivity;
import com.bamboospear.daily.R;
import com.bamboospear.daily.SecondFragment;

public class MyReceiver extends BroadcastReceiver {

    public static final String MY_ACTION = "com.example.geonchang.mybro.action.ACTION_MY_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!SecondFragment.isPlay) {
            SecondFragment.imageView.setImageResource(R.drawable.stop);
            SecondFragment.isPlay = !SecondFragment.isPlay;
        } else {
            SecondFragment.imageView.setImageResource(R.drawable.play);
            SecondFragment.isPlay = !SecondFragment.isPlay;
        }




        Log.e("test", "onReceive()");
    }
}
