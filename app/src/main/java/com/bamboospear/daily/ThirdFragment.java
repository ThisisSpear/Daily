package com.bamboospear.daily;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bamboospear.daily.databinding.FragmentThreeBinding;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ThirdFragment extends android.support.v4.app.Fragment {

    FragmentThreeBinding binding;
    ArrayList<ContactItem> contactItems;
    public ThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_three, container, false);

        contactItems = getContactList();
        ArrayList<String> mData = new ArrayList<>();
        // 초기 데이터
        for (int i = 0; i < contactItems.size(); i++) {
            mData.add(contactItems.get(i).getUser_Name());
        }

        // 리스트뷰 초기화
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mData);
        binding.list.setAdapter(adapter);

        // 클릭 이벤트 처리
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("이름 : " + contactItems.get(position).getUser_Name() + "\n전화번호 : " + contactItems.get(position).getUser_phNumber());
                builder.setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+contactItems.get(position).getUser_phNumber()));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });

        return binding.getRoot();
    }

    public ArrayList<ContactItem> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
                selectionArgs, sortOrder);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        if (cursor.moveToFirst()) {
            do {
                long photo_id = cursor.getLong(2);
                long person_id = cursor.getLong(3);
                ContactItem contactItem = new ContactItem();
                contactItem.setUser_phNumber(cursor.getString(0));
                contactItem.setUser_Name(cursor.getString(1));
                contactItem.setPhoto_id(photo_id);
                contactItem.setPerson_id(person_id);

                hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }
        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);
        for (int i = 0; i < contactItems.size(); i++) {
            contactItems.get(i).setId(i);
        }
        return contactItems;
    }

}