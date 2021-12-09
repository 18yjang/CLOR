package org.ewha5.clorapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lib.kingja.switchbutton.SwitchMultiButton;


public class ViewDB extends AppCompatActivity {
    private static final String TAG = "ViewDB";

    RecyclerView recyclerView;
    ClorAdapter adapter;
    Context context;
    SimpleDateFormat todayDateFormat;

    OnTabItemSelectedListener listener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_db);

        Button todayWriteButton = findViewById(R.id.todayWriteButton);
        todayWriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Fragment1.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_BUTTON1);
            }
        });


        SwitchMultiButton switchButton = findViewById(R.id.switchButton);
        switchButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                //Toast.makeText(getContext(), tabText, Toast.LENGTH_SHORT).show();
                adapter.switchLayout(position);
                adapter.notifyDataSetChanged();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ClorAdapter();

        adapter.addItem(new Clor(0, "블라우스", "0", "0", null, "11월 10일"));
        adapter.addItem(new Clor(1, "바지", "1", "0", null, "11월 11일"));
        adapter.addItem(new Clor(2, "티셔츠", "1", "0", null, "11월 12일"));

        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new OnNoteItemClickListener() {
            @Override
            public void onItemClick(ClorAdapter.ViewHolder holder, View view, int position) {
                Clor item = adapter.getItem(position);

                Log.d(TAG, "아이템 선택됨 : " + item.get_id());

                if (listener != null) {
                    listener.showFragment2(item);
                }
            }
        });


        // 데이터 로딩
        loadClorListData();

    }

    /**
     * 리스트 데이터 로딩
     */
    public int loadClorListData() {
        AppConstants.println("loadClorListData called.");

        String sql = "select _id, CATEGORY, COMB, MOOD, PICTURE, CREATE_DATE from " + ClorDatabase.TABLE_CLOR + " order by CREATE_DATE desc";

        int recordCount = -1;
        ClorDatabase database = ClorDatabase.getInstance(context);
        if (database != null) {
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AppConstants.println("record count : " + recordCount + "\n");

            ArrayList<Clor> items = new ArrayList<Clor>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String category = outCursor.getString(1);
                String comb = outCursor.getString(2);
                String mood = outCursor.getString(3);
                String picture = outCursor.getString(4);
                String dateStr = outCursor.getString(5);
                String createDateStr = null;
                if (dateStr != null && dateStr.length() > 10) {
                    try {
                        Date inDate = AppConstants.dateFormat4.parse(dateStr);

                        if (todayDateFormat == null) {
                            todayDateFormat = new SimpleDateFormat(getResources().getString(R.string.today_date_format));
                        }
                        createDateStr = todayDateFormat.format(inDate);
                        AppConstants.println("currentDateString : " + createDateStr);
                        //createDateStr = AppConstants.dateFormat3.format(inDate);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    createDateStr = "";
                }

                AppConstants.println("#" + i + " -> " + _id + ", " + category + ", " +
                        comb + ", " + mood + ", " + picture + ", " + createDateStr);

                items.add(new Clor(_id, category, comb, mood, picture, dateStr));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        }

        return recordCount;
    }

}
