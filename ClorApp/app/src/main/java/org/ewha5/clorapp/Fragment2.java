package org.ewha5.clorapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Fragment2 extends AppCompatActivity {

    private static final String TAG = "Fragment2";

    PieChart chart;
    BarChart chart2;

    Context context;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2);

        //set for the first graph
        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        chart.setCenterText("유사색조합\n 보색조합");
        chart.setCenterTextSize(14);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setHighlightPerTapEnabled(true);


        Legend legent1 = chart.getLegend();
        legent1.setEnabled(false);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);

        //setData1();


        //set for second graph
        chart2 = findViewById(R.id.chart2);
        chart2.setDrawValueAboveBar(true);

        chart2.getDescription().setEnabled(false);
        chart2.setDrawGridBackground(false);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = chart2.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        leftAxis.setAxisMinimum(0.0f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(1f);


        YAxis rightAxis = chart2.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend2 = chart2.getLegend();
        legend2.setEnabled(false);

        chart2.animateXY(1500, 1500);

        //setData2();
        loadStatData();
    }

    //색상 조합 선택별 비율
    private void setData1(HashMap<String,Integer> dataHash1) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        String[] keys = {"0", "1"};
        int[] icons = {R.drawable.conicon_33, R.drawable.simicon_33};

        for (int i = 0; i < keys.length; i++) {
            int value = 0;
            Integer outValue = dataHash1.get(keys[i]);
            if (outValue != null) {
                value = outValue.intValue();
            }

            if (value > 0) {
                entries.add(new PieEntry(value, "",
                        getResources().getDrawable(icons[i])));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, getResources().getString(R.string.graph1_title));

        dataSet.setDrawIcons(true);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, -40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(22.0f);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }

    private void setData2(HashMap<String,Integer> dataHash2) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        String[] keys = {"0", "1", "2", "3", "4"};
        int[] icons = {R.drawable.face1_33, R.drawable.face2_33,
                R.drawable.face3_33, R.drawable.face4_33,
                R.drawable.face5_33};

        for (int i = 0; i < keys.length; i++) {
            int value = 0;
            Integer outValue = dataHash2.get(keys[i]);
            AppConstants.println("#" + i + " -> " + outValue);
            if (outValue != null) {
                value = outValue.intValue();
            }
            /*
            if (value > 0) {
                entries.add(new PieEntry(value, "",
                        getResources().getDrawable(icons[i])));

                        if (value == 0) {
                entries.add(new BarEntry(Float.valueOf(String.valueOf(i+1)), value, getResources().getDrawable(icons[0])));
            } else if (value == 1) {
                entries.add(new BarEntry(Float.valueOf(String.valueOf(i+1)), value, getResources().getDrawable(icons[1])));
            } else if (value == 2) {
                entries.add(new BarEntry(Float.valueOf(String.valueOf(i+1)), value, getResources().getDrawable(icons[2])));
            } else if (value == 3) {
                entries.add(new BarEntry(Float.valueOf(String.valueOf(i+1)), value, getResources().getDrawable(icons[3])));
            } else if (value == 4) {
                entries.add(new BarEntry(Float.valueOf(String.valueOf(i+1)), value, getResources().getDrawable(icons[4])));
            }
            */
            if (value > 0) {
                entries.add(new BarEntry(Float.valueOf(String.valueOf(i+1)), value, getResources().getDrawable(icons[i])));
            }
        }

        BarDataSet dataSet2 = new BarDataSet(entries, getResources().getString(R.string.graph2_title));
        dataSet2.setColor(Color.rgb(240, 120, 124));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        dataSet2.setColors(colors);
        dataSet2.setIconsOffset(new MPPointF(0, -10));

        BarData data = new BarData(dataSet2);
        data.setValueTextSize(10f);
        data.setDrawValues(false);
        data.setBarWidth(0.8f);

        chart2.setData(data);
        chart2.invalidate();
    }

    public void loadStatData() {
        ClorDatabase database = ClorDatabase.getInstance(context);



        // first graph
        String sql = "select comb " +
                "  , count(comb) " +
                "from " + ClorDatabase.TABLE_CLOR + " " +
                "where create_date > '" + getMonthBefore(1) + "' " +
                "  and create_date < '" + getTomorrow() + "' " +
                "group by comb";

        Cursor cursor = database.rawQuery(sql);
        int recordCount = cursor.getCount();
        AppConstants.println("recordCount : " + recordCount);

        HashMap<String,Integer> dataHash1 = new HashMap<String,Integer>();
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();

            String combName = cursor.getString(0);
            int combCount = cursor.getInt(1);

            AppConstants.println("#" + i + " -> " + combName + ", " + combCount);
            dataHash1.put(combName, combCount);
        }

        setData1(dataHash1);

        // second graph

        sql = "select mood " +
                "  , count(mood) " +
                "from " + ClorDatabase.TABLE_CLOR + " " +
                "where create_date > '" + getMonthBefore(1) + "' " +
                "  and create_date < '" + getTomorrow() + "' " +
                "group by mood";


        cursor = database.rawQuery(sql);
        recordCount = cursor.getCount();
        AppConstants.println("recordCount : " + recordCount);

        HashMap<String,Integer> dataHash2 = new HashMap<String,Integer>();

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();

            String moodName = cursor.getString(0);
            int moodCount = cursor.getInt(1);

            AppConstants.println("#" + i + " -> " + moodName + ", " + moodCount);
            dataHash2.put(moodName, moodCount);
        }

        setData2(dataHash2);
    }


    public String getToday() {
        Date todayDate = new Date();

        return AppConstants.dateFormat5.format(todayDate);
    }

    public String getTomorrow() {
        Date todayDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return AppConstants.dateFormat5.format(cal.getTime());
    }

    public String getDayBefore(int amount) {
        Date todayDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        cal.add(Calendar.DAY_OF_MONTH, (amount * -1));

        return AppConstants.dateFormat5.format(cal.getTime());
    }

    public String getMonthBefore(int amount) {
        Date todayDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        cal.add(Calendar.MONTH, (amount * -1));

        return AppConstants.dateFormat5.format(cal.getTime());
    }

    /*
    private void setData1() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(60.0f, "",
                getResources().getDrawable(R.drawable.conicon_33)));
        entries.add(new PieEntry(40.0f, "",
                getResources().getDrawable(R.drawable.simicon_33)));

        PieDataSet dataSet = new PieDataSet(entries, "색상 조합 선택 비율");

        dataSet.setDrawIcons(true);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, -40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(22.0f);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }

    private void setData2() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(1.0f, 10.0f,
                getResources().getDrawable(R.drawable.face1_33)));
        entries.add(new BarEntry(2.0f, 20.0f,
                getResources().getDrawable(R.drawable.face2_33)));
        entries.add(new BarEntry(3.0f, 50.0f,
                getResources().getDrawable(R.drawable.face3_33)));
        entries.add(new BarEntry(4.0f, 70.0f,
                getResources().getDrawable(R.drawable.face4_33)));
        entries.add(new BarEntry(5.0f, 90.0f,
                getResources().getDrawable(R.drawable.face5_33)));

        BarDataSet dataSet2 = new BarDataSet(entries, "Sinus Function");
        dataSet2.setColor(Color.rgb(240, 120, 124));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        dataSet2.setColors(colors);
        dataSet2.setIconsOffset(new MPPointF(0, -10));

        BarData data = new BarData(dataSet2);
        data.setValueTextSize(10f);
        data.setDrawValues(false);
        data.setBarWidth(0.8f);

        chart2.setData(data);
        chart2.invalidate();
    }
     */


}