package org.ewha5.clorapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.channguyen.rsv.RangeSliderView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowResult3 extends AppCompatActivity {

    private static final String TAG = "ShowResult3";

    //전달받아야 하는 값 + [카테고리 값]
    public String color_value = "#899cdc";
    //

    ImageView final1;
    ImageView final2;

    ImageView final1_1;
    ImageView final2_1;

    ImageView line5;
    ImageView line6;
    ImageView cloricon;

    TextView rating;
    TextView finalresult;

    RangeSliderView moodSlider;
    int moodIndex = 2;

    //
    int mMode = AppConstants.MODE_INSERT;
    int _id = -1;
    Clor item;
    Context context;
    OnTabItemSelectedListener listener;
    OnRequestListener requestListener;

    //받아오는 값
    String comb;
    String path;
    String type;
    //

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_result3);

        line5 = findViewById(R.id.line5);
        line6 = findViewById(R.id.line6);
        cloricon = findViewById(R.id.cloricon);

        rating = findViewById(R.id.rating);
        finalresult = findViewById(R.id.finalresult);

        //앞에서 선택값 받아오기
        Intent SecondIntent = getIntent();
        String color_show = SecondIntent.getStringExtra("final");

        //추가함 DB용
        comb = SecondIntent.getStringExtra("comb");
        //path = SecondIntent.getStringExtra("path");
        type = SecondIntent.getStringExtra("type");
        //

        path = "img01";

        //색상 변경(상의 기준) - 2.png 기준 [if와 else로 final1과 final2 위치 바꾸기]
        final1 = findViewById(R.id.final1);
        final1.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
        final1.bringToFront();
        final1_1 = findViewById(R.id.final1_1);
        final1_1.bringToFront();
        final2 = findViewById(R.id.final2);
        final2.setColorFilter(Color.parseColor(color_show), PorterDuff.Mode.SRC_IN);
        final2_1 = findViewById(R.id.final2_1);
        //final2_1.bringToFront();

        //통계로 이동
        Button btnstatics = findViewById(R.id.btnstatics);
        btnstatics.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Fragment2.class);
                startActivityForResult(intent, AppConstants.STATICS);
            }
        });

        //홈으로 연결
        Button btnhome = findViewById(R.id.btnhome);
        btnhome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, AppConstants.MAIN);
            }
        });

        //웹으로 연결
        Button btnweb = findViewById(R.id.btnweb);
        btnweb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.shopping.naver.com/home/m/index.naver"));
                startActivity(myIntent);
            }
        });

        moodSlider = findViewById(R.id.sliderView);
        final RangeSliderView.OnSlideListener listener = new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                AppConstants.println("moodIndex changed to " + index);
                moodIndex = index;
            }
        };

        moodSlider.setOnSlideListener(listener);
        moodSlider.setInitialIndex(2);


        //평가도 저장****
        Button saveRate = findViewById(R.id.saverate);
        saveRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMode == AppConstants.MODE_INSERT) {
                    saveNote();
                }
                /*else if(mMode == AppConstants.MODE_MODIFY) {
                    modifyNote();
                }*/
            }
        });

    }

    //저장하는 코드
    public void setMood(String mood) {
        try {
            moodIndex = Integer.parseInt(mood);
            moodSlider.setInitialIndex(moodIndex);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setItem(Clor item) {
        this.item = item;
    }

    /*
    public void applyItem() {
        AppConstants.println("applyItem called.");

        if (item != null) {
            mMode = AppConstants.MODE_MODIFY;

            setWeatherIndex(Integer.parseInt(item.getWeather()));
            setAddress(item.getAddress());
            setDateString(item.getCreateDateStr());
            setContents(item.getContents());

            String picturePath = item.getPicture();
            AppConstants.println("picturePath : " + picturePath);

            if (picturePath == null || picturePath.equals("")) {
                pictureImageView.setImageResource(R.drawable.noimagefound);
            } else {
                setPicture(item.getPicture(), 1);
            }

            setMood(item.getMood());

        } else {
            mMode = AppConstants.MODE_INSERT;

            setWeatherIndex(0);
            setAddress("");

            Date currentDate = new Date();
            if (todayDateFormat == null) {
                todayDateFormat = new SimpleDateFormat(getResources().getString(R.string.today_date_format));
            }
            currentDateString = todayDateFormat.format(currentDate);
            AppConstants.println("currentDateString : " + currentDateString);
            setDateString(currentDateString);

            contentsInput.setText("");
            pictureImageView.setImageResource(R.drawable.noimagefound);
            setMood("2");
        }

    }
    */

    /*
     * 데이터베이스 레코드 추가

     */

    private void saveNote() {

        //데이터베이스 레코드 - id 고민

        String sql = "insert into " + ClorDatabase.TABLE_CLOR +
                "(CATEGORY, COMB, MOOD, PICTURE) values(" +
                "'"+ type + "', " +
                "'"+ comb + "', " +
                "'"+ moodIndex + "', " +
                "'"+ path + "')";

        Log.d(TAG, "sql : " + sql);
        ClorDatabase database = ClorDatabase.getInstance(context);
        database.execSQL(sql);

    }

    /**
     * 데이터베이스 레코드 수정
     */
    private void modifyNote() {
        if (item != null) {
            // update note
            String sql = "update " + ClorDatabase.TABLE_CLOR +
                    " set " +
                    "   CATEGORY = '" + type + "'" +
                    "   ,COMB = '" + comb + "'" +
                    "   ,MOOD = '" + moodIndex + "'" +
                    "   ,PICTURE = '" + path + "'" +
                    " where " +
                    "   _id = " + item._id;

            Log.d(TAG, "sql : " + sql);
            ClorDatabase database = ClorDatabase.getInstance(context);
            database.execSQL(sql);
        }
    }


    /*
     * 레코드 삭제

     */
    private void deleteNote() {
        AppConstants.println("deleteNote called.");

        if (item != null) {
            // delete note
            String sql = "delete from " + ClorDatabase.TABLE_CLOR +
                    " where " +
                    "   _id = " + item._id;

            Log.d(TAG, "sql : " + sql);
            ClorDatabase database = ClorDatabase.getInstance(context);
            database.execSQL(sql);
        }
    }



}
