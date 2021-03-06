package com.janumonster.mylibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 *  @author zzy
 *  @email zzy8@meitu.com
 *
 *  继承了FramLayout，在中部有两个叠加的正方形的ImageView，前置图片采用CENTER_INSTIDE填充，后置图片采用CENTER_CORP填充
 *  细边的出现采用ScaleAnimation实现
 *  背景渐变采用
 *
 *  白色背景（默认）：setWhiteBackground()；
 *  黑色背景: setBlackground();
 *  图片背景: setGaussBackground(Drawable);
 *  设置图片：setImageResource(int);
 *  设置正方形与图片的比例：setTimes(float)，times的采用 105% --> 105 比例换算，输入105
 *  设置细边：setBorder()
 *  移除细边：removeBorder()
 *  设置动画时长：setAnimationTime(int)
 *  设置layout宽度：setLayoutWidth(float)
 *  设置回初始状态：setToDefult()
 *  置背景渐变动画时长：setTransitionTime(int)
 *
 */

public class SquareImageLayout extends FrameLayout {

    private static final String TAG = "TEST";

    //两张纯色背景图片
    private Drawable whiteDrawable = getResources().getDrawable(R.drawable.white);
    private Drawable blackDrawable = getResources().getDrawable(R.drawable.black);
    private Drawable guassDrawable = getResources().getDrawable(R.drawable.transparents);


    //渐变动画时长
    private int transitionTime = 150;

    //图片的drawable
    private Drawable imageDrawable = getResources().getDrawable(R.drawable.transparents);

    //自定义控件的view
    private View mView;

    //两个imageview
    private ImageView mIvBack;
    private ImageView mIvForward;

    private float mLayoutWidth;
    //用于计算细边的宽度，100% == 100,105% == 105
    private float times = 105;
    //细边的宽度
    private float thinEdge;

    //判断是否已经缩小
    private boolean isSmall = false;

    //是否是正方形
    private boolean isSquare = false;

    //缩放比例
    private float scaling;
    //缩放动画
    private ScaleAnimation scaleToBig;
    private ScaleAnimation scaleToSmall;
    //缩放动画时长
    private int animationTime = 200;


    public SquareImageLayout(@NonNull Context context) {
        super(context,null);
    }

    public SquareImageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public SquareImageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){

        mView = LayoutInflater.from(context).inflate(R.layout.square_image_view, this,true);

        //实例化imageview
        mIvForward = mView.findViewById(R.id.image_forward);
        mIvBack = mView.findViewById(R.id.image_back);

        //获得屏幕宽度，狂赌一开始默认为屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        //将layout的默认宽度设为屏幕宽度
        mLayoutWidth = Math.min(displayMetrics.widthPixels,displayMetrics.heightPixels);

        //初始化conten和缩放动画
        initContentView();

        loadImage();
    }

    /**
     * 载入图片，并判断是否是正方形
     */
    private void loadImage() {

        //设置前置图片
        mIvForward.setImageDrawable(imageDrawable);
        guassDrawable = imageDrawable;

        //获得图片的宽高
        mIvForward.measure(0,0);
        int imageWidth = mIvForward.getMeasuredWidth();
        int imageHight = mIvForward.getMeasuredHeight();

        //如果是正方形，一开始就加细边
        if (imageHight == imageWidth){
            setBorder();
            isSmall = true;
            isSquare = true;
        }else {
            isSquare = false;
            isSmall = false;
        }

    }


    /**
     * 初始化正方形
     */
    private void initContentView(){

        //计算细边的宽度
        thinEdge = mLayoutWidth/times*(times - 100)/2;
        Log.d(TAG, "initialImage: "+thinEdge);

        //计算出缩放比例
        scaling = (mLayoutWidth - thinEdge*2)/mLayoutWidth;

        initiScaleAnimation();

        //初始化后置图片宽高
        ViewGroup.LayoutParams imgBack = mIvBack.getLayoutParams();
        imgBack.width = (int) mLayoutWidth;
        imgBack.height = (int) mLayoutWidth;
        mIvBack.setLayoutParams(imgBack);

        //初始化前置图片宽高
        ViewGroup.LayoutParams imgForward = mIvForward.getLayoutParams();
        imgForward.width = (int) mLayoutWidth;
        imgForward.height = (int) mLayoutWidth;
        mIvForward.setLayoutParams(imgForward);

        //设置图片的ScaleType
        mIvBack.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvForward.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //默认背景为白色
        mIvBack.setImageDrawable(whiteDrawable);
    }

    /**
     * 初始化缩放动画
     */
    private void initiScaleAnimation() {

        //设置无边框到有边框的动画
        scaleToSmall = new ScaleAnimation(1.0f, scaling,1.0f, scaling, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleToSmall.setDuration(animationTime);
        scaleToSmall.setFillAfter(true);

        //设置有边框到无边框的动画
        scaleToBig = new ScaleAnimation(scaling,1.0f,scaling,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleToBig.setDuration(animationTime);
        scaleToBig.setFillAfter(true);

    }

    /**
     * 设置白色背景
     */
    public void setWhiteBackground(){

        beginTransition(mIvBack.getDrawable(),whiteDrawable);

    }

    /**
     * 设置背景为黑色，为了保证能将图片去掉，将图片透明度设为0
     */
    public void setBlackBackground(){

        beginTransition(mIvBack.getDrawable(),blackDrawable);
    }

    /**
     * 设置背景image图片
     */
    public void setGuassBackground(){

        //测试语句,不执行
//        imageDrawable = getResources().getDrawable(imageID);

        beginTransition(mIvBack.getDrawable(),guassDrawable);

    }

    /**
     * 进行渐变动画
     * @param oldDrawable
     * @param newDrawable
     */
    public void beginTransition(Drawable oldDrawable,Drawable newDrawable){

        TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldDrawable,newDrawable});
        mIvBack.setImageDrawable(td);
        td.startTransition(transitionTime);
    }

    /**
     * 设置细边的显示
     * 如果已经有细边，就不执行动画
     */
    public void setBorder(){
        if (!isSmall){
            mIvForward.startAnimation(scaleToSmall);
            isSmall = true;
        }
    }

    /**
     * 移除细边
     * 如果已经没有细边，不执行动画
     * 如果是正方形，不执行动画
     */
    public void removeBorder(){
        if (isSmall){
            if (!isSquare){
                mIvForward.startAnimation(scaleToBig);
                isSmall = false;
            }
        }
    }

    /**
     * 设置前置图片
     * @param imageDrawable
     */
    public void setImageDrawable(Drawable imageDrawable) {

        this.imageDrawable = imageDrawable;

        loadImage();
    }

    /**
     * 根据设置的控件宽度生成正方形
     * @param mLayoutWidth
     */
    public void setmLayoutWidth(float mLayoutWidth) {
        this.mLayoutWidth = mLayoutWidth;
        initContentView();
    }


    /**
     * 设置背景渐变动画时长 单位：ms
     * @param transitionTime
     */
    public void setTransitionTime(int transitionTime) {
        this.transitionTime = transitionTime;
    }

    /**
     * 设置动画时间 单位：ms
     * @param animationTime
     */
    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    /**
     * 设置正方形的百分比值
     * 百分数扩大100倍后的值
     * e.g.：105% --> 105
     * @param times
     */
    public void setTimes(float times) {

        this.times = times;
        //新的倍数，重置动画效果
        initContentView();

    }

    /**
     * 将状态设置回初始状态
     */
    public void setToDefult(){

        setWhiteBackground();
        if (!isSquare){
            removeBorder();
        }
    }
}
