package com.bamboospear.daily;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bamboospear.daily.databinding.FragmentTwoBinding;
import com.bamboospear.daily.music.MusicService;
import com.bamboospear.daily.music.MyReceiver;

public class SecondFragment extends android.support.v4.app.Fragment {
    public static ImageButton imageView;
    public static boolean isPlay;
    private Intent intent;
    private BroadcastReceiver mReceiver;
    private FragmentTwoBinding binding;
    public SecondFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_two, container, false);
        // 브로드캐스트
        mReceiver = new MyReceiver();

        imageView = binding.playBtn;
        IntentFilter filter = new IntentFilter();
        filter.addAction("test");
        getActivity().registerReceiver(mReceiver, filter);

        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlay) {
                    binding.playBtn.setImageResource(R.drawable.stop);
                    intent = new Intent(getActivity(), MusicService.class);
                    intent.setAction("andbook.example.PLAYMUSIC");
                    getActivity().startService(intent);
                    isPlay = !isPlay;
                } else {
                    binding.playBtn.setImageResource(R.drawable.play);
                    intent = new Intent(getActivity(), MusicService.class);
                    intent.setAction("andbook.example.STOPMUSIC");
                    getActivity().startService(intent);
                    isPlay = !isPlay;
                }
            }
        });

        return binding.getRoot();
    }

/*    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().stopService(intent);
        removeNotification();
    }*/

    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(getActivity()).cancel(1);
    }
}