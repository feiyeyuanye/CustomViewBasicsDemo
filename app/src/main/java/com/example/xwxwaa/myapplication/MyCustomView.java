package com.example.xwxwaa.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by xwxwaa on 2019/4/8.
 */

public class MyCustomView extends View{

    private int defaultSize;
    private int defaultColor;
    private Paint paint ;

    /**
     * 需要两个构造参数
     * @param mContext
     */
    public MyCustomView(Context mContext){
        super(mContext);
        init();
    }

    public MyCustomView(Context mContext, AttributeSet attributeSet){
        super(mContext,attributeSet);
        // 通过它，取出在xml中，由命名空间定义的属性值
        // 第二个参数就是我们在styles.xml文件中的<declare-styleable>标签
        // 即属性集合的标签，在R文件中名称为R.styleable+name
        TypedArray a = mContext.obtainStyledAttributes(attributeSet, R.styleable.MyCustomView);

        //第一个参数为属性集合里面的属性，R文件名称：R.styleable+属性集合名称+下划线+属性名称
        //第二个参数为，如果没有设置这个属性，则设置的默认的值
        defaultSize = a.getDimensionPixelSize(R.styleable.MyCustomView_default_size, 100);
        defaultColor = a.getColor(R.styleable.MyCustomView_default_color,Color.BLUE);

        //最后将TypedArray对象回收
        a.recycle();

        init();
    }
    private void init(){
        // 初始化Paint
        paint = new Paint();
        paint.setColor(defaultColor);
        paint.setStyle(Paint.Style.STROKE);//设置圆为空心
        paint.setStrokeWidth(3.0f);//设置线宽
    }
    /**
     * 测量
     * @param widthMeasureSpec 包含测量模式和宽度信息
     * @param heightMeasureSpec 包含测量模式和高度信息
     * int型数据占32个bit。其中前2个bit为测量模式。后30个bit为测量数据(尺寸大小)。
     * 这里测量出的尺寸大小，并不是View的最终大小，而是父View提供的参考大小。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 定义宽高尺寸
        int width = getSize(widthMeasureSpec);
        int height = getSize(heightMeasureSpec);

        // 实现一个正方形,取小值
        int sideLength =Math.min(width,height);

        // 设置View宽高
        setMeasuredDimension(sideLength,sideLength);
    }

    private int getSize(int measureSpec){
        int mySize = defaultSize;

        // 可通过下面的方法，来获取测量模式和尺寸大小。
        // 注意这里的specSize值单位是px，而我们xml中一般为dp。
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode){
            case MeasureSpec.UNSPECIFIED:
                // 父容器不对View有任何限制，这种情况一般用于系统内部，表示一种测量的状态。
                // 一般也不需要我们处理。。可以看ScrollView或列表相关组件。
                Log.e("TAG","测量模式；MeasureSpec.UNSPECIFIED");
                break;
            case MeasureSpec.EXACTLY:
                // 父容器已检测出View所需要的精确大小，就是SpecSize所指定的值。
                // 当xml中，宽或高指定为match_parent或具体数值，会走这里。
                mySize = specSize;
                Log.e("TAG","测量模式；MeasureSpec.EXACTLY");
                break;
            case MeasureSpec.AT_MOST:
                // View的尺寸大小，不能大于父View指定的SpecSize。
                // 当xml中，宽或高指定为wrap_content时，会走这里。
                mySize = specSize/2;
                Log.e("TAG","测量模式；MeasureSpec.AT_MOST");
                break;
            default:
                break;
        }
        return mySize;
    }
    /**
     * 绘制
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 接下来绘制一个正圆。
        // 需要知道，圆的半径，和圆点坐标。
        int r = getMeasuredWidth() ;
        int centerX ;
        int centerY ;

        int paddingL = getPaddingLeft();
        int paddingT = getPaddingTop();
        int paddingR = getPaddingRight();
        int paddingB = getPaddingBottom();

        // 计算View减去padding后的可用宽高
        int canUsedWidth = r - paddingL - paddingR;
        int canUsedHeight = r - paddingT - paddingB;

        // 圆心坐标
        centerX = canUsedWidth / 2 + paddingL;
        centerY = canUsedHeight / 2 + paddingT;
        // 取两者最小值作为圆的直径
        int minSize = Math.min(canUsedWidth, canUsedHeight);
        // 绘制一个圆
        canvas.drawColor(Color.WHITE);//设置画布颜色
        canvas.drawCircle(centerX,centerY,minSize / 2,paint);
    }
    /**
     * 布局
     * @param changed 当前View的大小和位置改变了
     * @param left 左部位置（相对于父视图）
     * @param top 顶部位置（相对于父视图）
     * @param right 右部位置（相对于父视图）
     * @param bottom 底部位置（相对于父视图）
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 一般在自定义ViewGroup时使用，来定义子View的位置。

    }
}
