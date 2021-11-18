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

import java.util.ArrayList;

import lib.kingja.switchbutton.SwitchMultiButton;


public class ViewDB extends AppCompatActivity {
    private static final String TAG = "ViewDB";

    RecyclerView recyclerView;
    ClorAdapter adapter;

    Context context;
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

        adapter.addItem(new Clor(0, "블라우스", "보색 조합", "", ""));
        adapter.addItem(new Clor(1, "바지", "유사색 조합", "", ""));
        adapter.addItem(new Clor(2, "티셔츠", "유사색 조합", "", ""));

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

        String sql = "select _id, CATEGORY, COMB, MOOD, PICTURE from " + ClorDatabase.TABLE_CLOR ; //+ " order by _id desc"

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

                AppConstants.println("#" + i + " -> " + _id + ", " + category + ", " +
                        comb + ", " + mood + ", " + picture + ", " );

                items.add(new Clor(_id, category, comb, mood, picture));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        }

        return recordCount;
    }

}
