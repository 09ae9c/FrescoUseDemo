package com.tc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.tc.frescousedemo.R;


/**
 * Created by classTC on 12/22/2015.模拟OVERLAY_COLOR 模式实现圆角图片
 */
public class OverLayRoundView extends ImageView {
    private boolean isCircle;
    private int mCircleRadius;
    private float mRoundRadius;
    private float[] mCornerRadii = new float[8];
    private int mViewSize;
    private Paint mPaint;
    private Path mPath;
    private boolean mIsNonZero;
    private RectF mRoundRecF;
    private Matrix mTransform;
    private int mOverlayColor;

    public OverLayRoundView(Context context) {
        this(context, null);
    }

    public OverLayRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverLayRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mTransform = new Matrix();

        //获取属性
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.OverLayRoundView);
        isCircle = arr.getBoolean(R.styleable.OverLayRoundView_O_isCircle, false);
        mRoundRadius = arr.getDimension(R.styleable.OverLayRoundView_O_round_radius, 0);

        mOverlayColor = arr.getColor(R.styleable.OverLayRoundView_O_overlay_color, 0xffffff);

        if (mRoundRadius != 0) {//如果设置了roundRadius，则Radii失效,否则使用Radii
            setRadii(mRoundRadius, mRoundRadius, mRoundRadius, mRoundRadius);

        } else {
            setRadii(arr.getDimension(R.styleable.OverLayRoundView_O_top_left_radius, 0),
                    arr.getDimension(R.styleable.OverLayRoundView_O_top_right_radius, 0),
                    arr.getDimension(R.styleable.OverLayRoundView_O_bottom_right_radius, 0),
                    arr.getDimension(R.styleable.OverLayRoundView_O_bottom_left_radius, 0));
        }

        arr.recycle();
    }

    private void setRadii(float tl, float tr, float br, float bl) {
        mCornerRadii[0] = mCornerRadii[1] = tl;
        mCornerRadii[2] = mCornerRadii[3] = tr;
        mCornerRadii[4] = mCornerRadii[5] = br;
        mCornerRadii[6] = mCornerRadii[7] = bl;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isCircle) {
            mViewSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mCircleRadius = mViewSize / 2;
            setMeasuredDimension(mViewSize, mViewSize);
        } else {
            mRoundRecF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
        updatePath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            updateNonZero();
            if (!mIsNonZero) {//如果没有设置属性，直接绘制
                super.onDraw(canvas);
                return;
            }
            super.onDraw(canvas);

            mPaint.setColor(mOverlayColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            canvas.drawPath(mPath, mPaint);
        }
    }

    private void updateNonZero() {
        if (isCircle || mRoundRadius > 0) {
            mIsNonZero = true;
        }
        for (int i = 0; i < mCornerRadii.length; i++) {
            if (mCornerRadii[i] > 0) {
                mIsNonZero = true;
            }
        }
    }

    private void updatePath() {
        mPath.reset();
        if (isCircle) {
            mPath.addCircle(mCircleRadius, mCircleRadius, mCircleRadius, Path.Direction.CW);
        } else {
            mPath.addRoundRect(mRoundRecF, mCornerRadii, Path.Direction.CW);
        }
        mPath.setFillType(Path.FillType.EVEN_ODD);
    }
}
