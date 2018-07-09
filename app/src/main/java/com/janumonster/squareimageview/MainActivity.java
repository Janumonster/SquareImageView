package com.janumonster.squareimageview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TEST";

    //两个ImageView
    private ImageView imageViewBack;
    private ImageView imageViewForward;

    //图片ID
    private int imageID = R.drawable.square;

    //屏幕的宽度
    private float screenWidth;

    //用于计算细边的宽度
    private float times = 105;

    //西边的宽度
    private float thinEdge;

    //是否是正方形
    private boolean isSquare = false;

    //非正方形是否需要加细边
    private boolean border = false;

    //控制按钮
    //button
    private Button Btn_Origin;
    private Button Btn_black_border;
    private Button Btn_white_border;
    private Button Btn_has_small_border;
    private Button Btn_no_small_border;
    private Button Btn_gaoss;

    //缩放效果
    private float scaling ;

    //判断是否已经缩小
    private boolean isSmall = false;

    //缩放动画
    private ScaleAnimation small_to_big;
    private ScaleAnimation big_to_small;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewBack = findViewById(R.id.image_back);
        imageViewForward = findViewById(R.id.image_forward);

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

        screenWidth = getScreenWidth();

        initialImage(screenWidth);

    }


    /**
     * 初始化正方形image
     */
    private void initialImage(float width) {

        //初始化后置图片宽高
        ViewGroup.LayoutParams imgBack = imageViewBack.getLayoutParams();
        imgBack.width = (int) width;
        imgBack.height = (int) width;
        imageViewBack.setLayoutParams(imgBack);

        //初始化前置图片宽高
        ViewGroup.LayoutParams imgForward = imageViewForward.getLayoutParams();
        imgForward.width = (int) width;
        imgForward.height = (int) width;
        imageViewForward.setLayoutParams(imgForward);

        setImageViewForward(imageID);

        imageViewBack.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewForward.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

    }

    /**
     * 将状态设置回初始状态
     */
    public void setToDefult(){

        imageViewBack.setImageAlpha(0);
        imageViewBack.setBackgroundColor(Color.WHITE);
        if (!isSquare){
            removeBorder();
        }
    }


    /**
     * 设置背景image图片
     * @param backImageID
     */
    public void setImageViewBack(int backImageID){
        imageViewBack.setImageAlpha(255);
        imageViewBack.setImageResource(backImageID);
    }

    /**
     * 设置前景image图片，获得thinEdge数据
     * 判断是否是正方形，是，直接加thinEdge
     * @param forwardImageID
     */
    public void setImageViewForward(int forwardImageID){

        imageViewForward.setImageResource(forwardImageID);

        //获得图片的宽高
        imageViewForward.measure(0,0);
        int imageWidth = imageViewForward.getMeasuredWidth();
        int imageHight = imageViewForward.getMeasuredHeight();

        Log.d(TAG, "setImageViewForward: Width: "+String.valueOf(imageWidth)+" Hight:"+String.valueOf(imageHight));

        thinEdge = screenWidth/times*(times - 100)/2;

        Log.d(TAG, "thinEdge : "+String.valueOf(thinEdge));
        initialScaleAnimation();

        if (imageHight == imageWidth){
            setBorder();
            isSquare = true;
        }

    }

    /**
     * 初始化缩放动画
     */
    private void initialScaleAnimation() {

        scaling = (screenWidth - thinEdge*2)/screenWidth;

        big_to_small = new ScaleAnimation(1.0f, scaling,1.0f, scaling, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        big_to_small.setDuration(500);
        big_to_small.setFillAfter(true);

        small_to_big = new ScaleAnimation(scaling,1.0f,scaling,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        small_to_big.setDuration(500);
        small_to_big.setFillAfter(true);

    }

    /**
     * 获得屏幕宽度
     * @return 屏幕宽度 int
     */
    public int getScreenWidth(){
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }


    /**
     * 设置细边的显示
     * @param
     */
    public void setBorder(){
        if (!isSmall){
            imageViewForward.startAnimation(big_to_small);
            isSmall = true;
        }
    }

    public void removeBorder(){
        if (isSmall){
            if (!isSquare){
                imageViewForward.startAnimation(small_to_big);
                isSmall = false;
            }
        }

    }

    public void setWhiteBack(){
        imageViewBack.setImageAlpha(0);
        imageViewBack.setBackgroundColor(Color.WHITE);
    }

    public void setBlackBack(){
        imageViewBack.setImageAlpha(0);
        imageViewBack.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_origin:
                setToDefult();
                break;
            case R.id.btn_white_border:
                setWhiteBack();
                break;
            case R.id.btn_black_border:
                setBlackBack();
                break;
            case R.id.btn_has_small_border:
                setBorder();
                break;
            case R.id.btn_no_small_border:
                removeBorder();
                break;
            case R.id.btn_gaos:
                setImageViewBack(imageID);
                break;
        }
    }
}
