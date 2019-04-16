package com.example.xwxwaa.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xwxwaa on 2019/4/10.
 */

public class MyCustomViewGroup extends ViewGroup{

    // 内边距
    private int paddingL ;
    private int paddingT ;
    private int paddingR ;
    private int paddingB ;
    // 外边距
    private int marginL;
    private int marginT;
    private int marginR;
    private int marginB;

    public MyCustomViewGroup (Context mContext){
        super(mContext);

    }

    public MyCustomViewGroup(Context mContext, AttributeSet attributeSet){
        super(mContext,attributeSet);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //  宽高的测量模式和尺寸
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 获取内边距
        paddingL = getPaddingLeft();
        paddingT = getPaddingTop();
        paddingR = getPaddingRight();
        paddingB = getPaddingBottom();
        // 初始化外边距，因为测量不止一次。
        marginL = 0;
        marginT = 0;
        marginR = 0;
        marginB = 0;

        // 测量所有子View的宽高。它会触发每个子View的onMeasure()。
        // measureChildren(widthMeasureSpec,heightMeasureSpec);

        // measureChild是对单个View进行测量。
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            marginL = Math.max(0,lp.leftMargin);//在本例中找出最大的左边距
            marginT += lp.topMargin;//在本例中求出所有的上边距之和
            marginR = Math.max(0,lp.rightMargin);//在本例中找出最大的右边距
            marginB += lp.bottomMargin;//在本例中求出所有的下边距之和
        }

        if (childCount == 0){
            // 没有子View
            setMeasuredDimension(0,0);
        }else {
            // 最大宽度，加上内外边距
            int viewGroupWidth = paddingL + getChildMaxWidth() + paddingR +marginL+marginR;
            // 高度之和，加上内外边距
            int viewGroupHeight = paddingT + getChildTotalHeight() + paddingB+marginT+marginB;
            // 选小值
            int  resultWidth = Math.min(viewGroupWidth, widthSize);
            int  resultHeight = Math.min(viewGroupHeight, heightSize);

            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
                // 如果父布局宽高都是wrap_content，只会走这个方法。
                // 宽高都是包裹内容,用于处理ViewGroup的wrap_content情况
                setMeasuredDimension(resultWidth,resultHeight);
            }else if (widthMode == MeasureSpec.AT_MOST){
                // 宽度是包裹内容
                setMeasuredDimension(resultWidth,heightSize);
            }else if (heightMode == MeasureSpec.AT_MOST){
                // 高度是包裹内容
                setMeasuredDimension(widthSize,resultHeight);
            }
            // 这里如果没进上面的条件判断中，super.onMeasure()会调用setMeasuredDimension()的，默认占满剩余可用空间。
        }
    }

    /**
     * 获取所有子View的最大宽度
     * @return
     */
    private int getChildMaxWidth(){
        int count = getChildCount();
        int maxWidth = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getMeasuredWidth() > maxWidth){
                maxWidth = child.getMeasuredWidth();
            }
        }
        return maxWidth;
    }

    /**
     * 获取所有子View的高度之和
     * @return
     */
    private int getChildTotalHeight(){
        int count = getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            totalHeight += view.getMeasuredHeight();
        }
        return totalHeight;
    }


    @Override
    protected void onLayout(boolean c, int l, int t, int r, int b) {
        int count = getChildCount();
        int coordHeight = paddingT;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int coordWidth = paddingL+ lp.leftMargin;
            coordHeight += lp.topMargin;
            child.layout(coordWidth,coordHeight,coordWidth+width,coordHeight+height);
            coordHeight+=height+lp.bottomMargin;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
