package com.janumonster.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.janumonster.mylibrary.SquareImageLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SquareImageLayout squareImageLayout;

    //控制按钮
    //button
    private Button Btn_Origin;
    private Button Btn_black_border;
    private Button Btn_white_border;
    private Button Btn_has_small_border;
    private Button Btn_no_small_border;
    private Button Btn_gaoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        squareImageLayout = findViewById(R.id.square_image_layout);
        squareImageLayout.init(this);

        Btn_Origin=findViewById(R.id.btn_origin);
        Btn_white_border=findViewById(R.id.btn_white_border);
        Btn_black_border=findViewById(R.id.btn_black_border);
        Btn_has_small_border=findViewById(R.id.btn_has_small_border);
        Btn_no_small_border=findViewById(R.id.btn_no_small_border);
        Btn_gaoss=findViewById(R.id.btn_gaos);

        Btn_Origin.setOnClickListener(this);
        Btn_white_border.setOnClickListener(this);
        Btn_black_border.setOnClickListener(this);
        Btn_has_small_border.setOnClickListener(this);
        Btn_no_small_border.setOnClickListener(this);
        Btn_gaoss.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_origin:
                squareImageLayout.setToDefult();
                break;
            case R.id.btn_white_border:
                squareImageLayout.setWhiteBackground();
                break;
            case R.id.btn_black_border:
                squareImageLayout.setBlackBackground();
                break;
            case R.id.btn_has_small_border:
                squareImageLayout.setBorder();
                break;
            case R.id.btn_no_small_border:
                squareImageLayout.removeBorder();
                break;
            case R.id.btn_gaos:
                squareImageLayout.setImageBackground(-1);
                break;
        }
    }
}
