package org.ewha5.clorapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShowResult2 extends AppCompatActivity {

    private static final String TAG = "ShowResult2";

    //전달 받아야하는 값
    public String color_value = Fragment1.result_color[0];
    public String[] comp = {Fragment1.result_color[1], Fragment1.result_color[2], Fragment1.result_color[3]};
    public String[] tonal = {Fragment1.result_color[4], Fragment1.result_color[5], Fragment1.result_color[6]};
    //

    String finalcolor;

    ImageView upper1;
    ImageView upper2;
    ImageView upper3;
    ImageView under1;
    ImageView under2;
    ImageView under3;
    ImageView line4;
    ImageView conicon2;
    ImageView tonalicon2;

    TextView select_comb2;

    RadioButton comb1, comb2, comb3;
    RadioGroup radioGroup2;

    //앞에서 받기
    String choice_comb;
    String path;
    String type;
    //

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_result2);

        line4 = findViewById(R.id.line4);
        conicon2 = findViewById(R.id.combicon2);
        tonalicon2 = findViewById(R.id.tonalicon2);

        select_comb2 = findViewById(R.id.select_comb2);

        Intent secondIntent = getIntent();
        int choice = secondIntent.getExtras().getInt("choice");

        //추가로 받아오기
        path = secondIntent.getStringExtra("path");
        type = secondIntent.getStringExtra("type");
        //

        /* //확인용 msg
        Toast.makeText(getApplicationContext(), path+" 파일경로 "+ type+ " 카테고리 ",
                Toast.LENGTH_SHORT).show();
        */

        if(choice == 0){
            choice_comb = "0";
            tonalicon2.setVisibility(View.INVISIBLE);
            //색상 변경 - 보색으로 2.png 기준
            upper1 = findViewById(R.id.upper1);
            upper1.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
            under1 = findViewById(R.id.under1);
            under1.setColorFilter(Color.parseColor(comp[0]), PorterDuff.Mode.SRC_IN);
            under1.bringToFront();

            upper2 = findViewById(R.id.upper2);
            upper2.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
            under2 = findViewById(R.id.under2);
            under2.setColorFilter(Color.parseColor(comp[1]), PorterDuff.Mode.SRC_IN);
            under2.bringToFront();

            upper3 = findViewById(R.id.upper3);
            upper3.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
            under3 = findViewById(R.id.under3);
            under3.setColorFilter(Color.parseColor(comp[2]), PorterDuff.Mode.SRC_IN);
            under3.bringToFront();

        } else if (choice == 1){
            choice_comb = "1";
            conicon2.setVisibility(View.INVISIBLE);
            upper1 = findViewById(R.id.upper1);
            upper1.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
            under1 = findViewById(R.id.under1);
            under1.setColorFilter(Color.parseColor(tonal[0]), PorterDuff.Mode.SRC_IN);
            under1.bringToFront();

            upper2 = findViewById(R.id.upper2);
            upper2.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
            under2 = findViewById(R.id.under2);
            under2.setColorFilter(Color.parseColor(tonal[1]), PorterDuff.Mode.SRC_IN);
            under2.bringToFront();

            upper3 = findViewById(R.id.upper3);
            upper3.setColorFilter(Color.parseColor(color_value), PorterDuff.Mode.SRC_IN);
            under3 = findViewById(R.id.under3);
            under3.setColorFilter(Color.parseColor(tonal[2]), PorterDuff.Mode.SRC_IN);
            under3.bringToFront();
        }

        //라디오 버튼 설정
        comb1 = (RadioButton) findViewById(R.id.comb1);
        comb1.setOnClickListener(new RadioButton.OnClickListener(){
           @Override
           public void onClick(View view) {
               if (choice == 0) {
                   // 보색 조합으로 연결
                   finalcolor = comp[0];
               } else{
                   finalcolor = tonal[0];
               }
           }
        });

        comb2 = (RadioButton) findViewById(R.id.comb2);
        comb2.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (choice == 0) {
                    // 보색 조합으로 연결
                    finalcolor = comp[1];
                } else{
                    finalcolor = tonal[1];
                }
            }
        });

        comb3 = (RadioButton) findViewById(R.id.comb3);
        comb3.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (choice == 0) {
                    // 보색 조합으로 연결
                    finalcolor = comp[2];
                } else{
                    finalcolor = tonal[2];
                }
            }
        });

        //다음 단계로 이동
        Button finalselect = findViewById(R.id.finalselect);
        finalselect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowResult3.class);
                intent.putExtra("final", finalcolor);
                //추가
                intent.putExtra("comb", choice_comb);
                intent.putExtra("type", type);
                intent.putExtra("path", path);
                //

                /*확인용 msg
                Toast.makeText(getApplicationContext(), path+" :파일경로  "+ type+ " :카테고리  "+choice_comb+ " :선택",
                        Toast.LENGTH_SHORT).show();
                */

                startActivityForResult(intent, AppConstants.FINAL_FIGURE);
            }
        });

    }

}
