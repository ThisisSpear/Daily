package com.bamboospear.daily;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends android.support.v4.app.Fragment {


    public static List<MyData> list = new ArrayList<>();
    private ListView mList;
    private MyAdapter mAdapter;

    public FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_one, container, false);
        // 리스트뷰 초기화
        mList = (ListView) layout.findViewById(R.id.list);
        mAdapter = new MyAdapter(getActivity(), list);
        mList.setAdapter(mAdapter);

        // 클릭 이벤트 처리
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), list.get(position).title + " 선택!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return layout;
    }
}

