package com.example.textcolorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by welive on 2016/10/20.
 *
 * 1.撰写自定义的TextColorView 作用：使TextView的字体颜色实现渐变的功能
 *
 * 2.自己绘制斜体的字符串
 *
 * 3.绘制梯形背景图
 *
 */
public class TextColorView extends TextView {


    private LinearGradient linearGradient;

    //默认绘制梯形的背景颜色
    private String DEFINE_COLOR = "#ff6c2b";

    private Paint paint;

    private String mTvText;
    //旋转角度
    private int rotateAngle;
    private int scalingParent;
    private int scalingChild;
    private Rect rect = new Rect();
    private int colorStart,colorEnd,angle;

    public TextColorView(Context context) {
        super(context);
    }

    private Context m_context;
    public TextColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.m_context = context;
        //获取配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.textColorView);

        //字体颜色渐变的初始颜色  默认白色
        colorStart = typedArray.getColor(R.styleable.textColorView_startColor, Color.WHITE);

        ////字体颜色渐变的末尾颜色  默认白色
        colorEnd = typedArray.getColor(R.styleable.textColorView_endColor, Color.WHITE);

        //渲染角度
        angle = typedArray.getInt(R.styleable.textColorView_angle, 0);

        //旋转角度，默认45度
        rotateAngle = typedArray.getInt(R.styleable.textColorView_rotateAngle,45);

        //绘制梯形
        scalingParent = typedArray.getInt(R.styleable.textColorView_scalingParent, 6);

        scalingChild = typedArray.getInt(R.styleable.textColorView_scalingChild,4);

        //获取相应的字符串
        mTvText = typedArray.getString(R.styleable.textColorView_text);
        typedArray.recycle();
    }


    /*
    设置字符串
     */
    public void setTvText(String tvText){
        this.mTvText = tvText;
    }

    /*
    设置字符串Id
     */
    public void setTvText(int tvTextID){
        this.mTvText = getResources().getString(tvTextID);
    }

    /*
    设置字体开始时的颜色
     */
    public void setTextStartColor(int startColorID){
        colorStart = startColorID;
    }

    /*
    代码中设置相应的字体结束时的颜色
     */
    public void setTextColorEnd(int endColorID){

        colorEnd  = endColorID;
    }

    public void setTextAngle(int mangle){
        angle = mangle;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDefineTextView(canvas);
    }


    /**
     * 根据画布的大小位置，我们自己 使用代码绘制一个梯形图片，并且绘制字体
     */
    public void drawDefineTextView(Canvas canvas){
        //声明画笔
        Paint paintDrawPath = new Paint();
        //设置画笔的颜色，默认颜色为橙色
        paintDrawPath.setColor(Color.parseColor(DEFINE_COLOR));
        //实心显示
        paintDrawPath.setStyle(Paint.Style.FILL);
//        去掉锯齿
        paintDrawPath.setAntiAlias(true);
//        绘制路径声明
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getMeasuredWidth() / scalingParent * scalingChild, 0);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight() -(getMeasuredWidth() / scalingParent *scalingChild ));
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path.close();
//        实现绘制图形
        canvas.drawPath(path, paintDrawPath);
//        绘制文字
        paint =getPaint();//画笔
        paint.getTextBounds(mTvText, 0, mTvText.length(), rect);
        //字体颜色渐变
        setAngle();
        paint.setShader(linearGradient);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //计算真的很折磨人啊
        int baseline = (int)(((getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top)-getMeasuredHeight()/(scalingParent*4)*scalingChild);
        canvas.translate(getMeasuredWidth() / (scalingParent)/2, -getMeasuredWidth() / (scalingParent)/2);
        canvas.rotate(rotateAngle, (getMeasuredWidth() / 2), (getMeasuredHeight() / 2));
        canvas.drawText(mTvText, (getMeasuredWidth() / 2 - rect.width() / 2) + getMeasuredWidth() / (scalingParent * 4) * scalingChild, getMeasuredHeight()/2, paint);

    }

    /**
     * 借助UI给出的图形，之后再进行旋转以及平移的转化，且自定义我们的textView
     * @param canvas
     */
    public void drawTextView(Canvas canvas){
        paint =getPaint();//画笔
        paint.getTextBounds(mTvText, 0, mTvText.length(), rect);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //字体颜色渐变
        setAngle();
        paint.setShader(linearGradient);
        int baseline = (int) ((getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);

//       先进行平移转化
        canvas.translate(getMeasuredWidth() / (scalingParent*4)*scalingChild, -getMeasuredWidth() / (scalingParent * 4) * scalingChild);
        //然后进行旋转显示
        canvas.rotate(45, getMeasuredWidth() / 2, getMeasuredWidth() / 2);
        canvas.drawText(mTvText, getMeasuredWidth() / 2 - rect.width() / 2, baseline, paint);
    }



    /**
     * 对角度进行判断的选择显示，逆时针旋转
     */
    private void setAngle(){


        switch (angle){
            case 0://由左向右进行渲染

                linearGradient = new LinearGradient(0,0,getMeasuredWidth(),0,new int[]{colorStart,colorEnd},null, Shader.TileMode.CLAMP);
                break;
            case 45://由左上角进行渲染

                linearGradient = new LinearGradient(0,0,getMeasuredWidth(),getMeasuredHeight(),new int[]{colorStart,colorEnd},null, Shader.TileMode.CLAMP);

                break;
            case 90:

                linearGradient = new LinearGradient(0,0,0,getMeasuredHeight(),new int[]{colorStart,colorEnd},null, Shader.TileMode.REPEAT);

                break;
            case 135://由右上角开始渲染
                linearGradient = new LinearGradient(getMeasuredWidth(),0,0,getMeasuredHeight(),new int[]{colorStart,colorEnd},null, Shader.TileMode.REPEAT);

                break;
            case 180://由右向左开始渲染
                linearGradient = new LinearGradient(getMeasuredWidth(),0,0,0,new int[]{colorStart,colorEnd},null, Shader.TileMode.REPEAT);

                break;
            case 225://由右下角开始渲染
                linearGradient = new LinearGradient(getMeasuredWidth(),getMeasuredHeight(),0,0,new int[]{colorStart,colorEnd},null, Shader.TileMode.REPEAT);

                break;
            case 270://由下向上渲染

                linearGradient = new LinearGradient(0,getMeasuredHeight(),0,0,new int[]{colorStart,colorEnd},null, Shader.TileMode.REPEAT);

                break;
            case 315://由左下角开始渲染
                linearGradient = new LinearGradient(0,getMeasuredHeight(),getMeasuredWidth(),0,new int[]{colorStart,colorEnd},null, Shader.TileMode.REPEAT);
                break;

            case 360://由左向右进行渲染
                linearGradient = new LinearGradient(0,0,getMeasuredWidth(),0,new int[]{colorStart,colorEnd},null, Shader.TileMode.CLAMP);
                break;

        }
    }
}
