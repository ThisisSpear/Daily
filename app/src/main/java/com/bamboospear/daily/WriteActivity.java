package com.bamboospear.daily;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bamboospear.daily.databinding.ActivityWriteBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity {
    private MyDBHelper mDBHelper;

    private ActivityWriteBinding binding;
    private File mFile;
    MyData myData;

    byte[] image;

    private ArrayList<String> mData = new ArrayList<>();
    private Spinner mSpinner1;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mDBHelper = new MyDBHelper(this);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_write);
        binding.setActivity(this);

        File sdcard = Environment.getExternalStorageDirectory();
        mFile = new File(sdcard.getAbsolutePath() + "/picture1.jpg");

        // 초기 데이터
        mData.add("맑음");
        mData.add("구름조금");
        mData.add("구름많음");
        mData.add("흐림");
        mData.add("소나기");
        mData.add("비");
        mData.add("눈");

        // 스피너 초기화
        mSpinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mData);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(adapter1);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.date.setText(sdf.format(myCalendar.getTime()));
    }

    public void mOnClick(View v) {
        SQLiteDatabase db;
        ContentValues values;
        String[] projection = { "_id", "name", "age", "weather", "date"};
        Cursor cur;

        switch (v.getId()) {
            case R.id.btnTakePicture:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                startActivityForResult(intent, 0);
                break;
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnWrite:

                MainActivity.viewPager.setAdapter(MainActivity.adapter);

                db = mDBHelper.getWritableDatabase();
                values = new ContentValues();
                values.put("title", binding.editTitle.getText().toString());
                values.put("content", binding.editContent.getText().toString());
                values.put("image", image);
                values.put("weather", mData.get(mSpinner1.getSelectedItemPosition()));
                values.put("date", binding.date.getText().toString());
                db.insert("daily", null, values);
                mDBHelper.close();
                myData = new MyData(binding.editTitle.getText().toString(), binding.editContent.getText().toString(), getAppIcon(image), mData.get(mSpinner1.getSelectedItemPosition()), binding.date.getText().toString());
                FirstFragment.list.add(myData);



                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            int width = binding.imgViewPicture.getWidth();
            int height = binding.imgViewPicture.getHeight();

            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mFile.getAbsolutePath(), bmpFactoryOptions);

            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
            if (heightRatio > 1 || widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeFile(mFile.getAbsolutePath(), bmpFactoryOptions);
            binding.imgViewPicture.setImageBitmap(bmp);

            // bitmap 타입을 drawable로 변경
            Drawable drawable = new BitmapDrawable(bmp);
            image = getByteArrayFromDrawable(drawable);

        }
    }

    public byte[] getByteArrayFromDrawable(Drawable d) {
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        return data;
    }

    public Bitmap getAppIcon(byte[] b) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

}