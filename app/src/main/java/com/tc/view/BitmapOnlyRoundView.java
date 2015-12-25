package com.tc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tc.frescousedemo.R;


/**
 * Created by classTC on 12/22/2015. 模拟 BITMAP_ONLY 模式实现圆角图片
 */
public class BitmapOnlyRoundView extends ImageView {
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

    public BitmapOnlyRoundView(Context context) {
        this(context, null);
    }

    public BitmapOnlyRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapOnlyRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mTransform = new Matrix();

        //获取属性
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.BitmapOnlyRoundView);
        isCircle = arr.getBoolean(R.styleable.BitmapOnlyRoundView_isCircle, false);
        mRoundRadius = arr.getDimension(R.styleable.BitmapOnlyRoundView_round_radius, 0);

        if (mRoundRadius != 0) {//如果设置了roundRadius，则Radii失效,否则使用Radii
            setRadii(mRoundRadius, mRoundRadius, mRoundRadius, mRoundRadius);

        } else {
            setRadii(arr.getDimension(R.styleable.BitmapOnlyRoundView_top_left_radius, 0),
                    arr.getDimension(R.styleable.BitmapOnlyRoundView_top_right_radius, 0),
                    arr.getDimension(R.styleable.BitmapOnlyRoundView_bottom_right_radius, 0),
                    arr.getDimension(R.styleable.BitmapOnlyRoundView_bottom_left_radius, 0));
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
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            updateNonZero();
            if (!mIsNonZero) {//如果没有设置属性，直接绘制
                super.onDraw(canvas);
                return;
            }
            updatePath();
            updatePaint();

            canvas.drawPath(mPath, mPaint);
        }
    }

    private void updateTransform(Bitmap bmp) {

        float scale;
        if (isCircle) {
            int bmpSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mViewSize * 1.0f / bmpSize;
        } else {
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
        }
        mTransform.setScale(scale, scale);
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

    private void updatePaint() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bmp = ((BitmapDrawable) getDrawable()).getBitmap();
            BitmapShader mShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            updateTransform(bmp);

            mShader.setLocalMatrix(mTransform);

            mPaint.setShader(mShader);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isCircle) {
            mRoundRecF = new RectF(0, 0, w, h);
        }
    }
}
