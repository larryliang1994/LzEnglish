package com.jiubai.lzenglish.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.R;

/**
 * Created by Droidroid on 2016/4/24.
 */
public class ChatImageView extends android.support.v7.widget.AppCompatImageView {

    private final int DIRECTION_LEFT = 0;
    private final int DIRECTION_RIGHT = 1;
    private final int DEFAULT_COLOR_DRAWABLE_DIMENSION = dp2px(250);

    // 箭头宽度
    private float mArrowWidth;
    // 箭头高度
    private float mArrowHeight;
    // 箭头偏移量
    private float mArrowOffset;
    // 箭头至View顶部的距离
    private float mArrowTop;
    // 圆角半径
    private float mRadius;
    // 箭头朝向方向
    private int mDirection;

    private Paint mPaint;
    private Paint mPaintOutter;

    private Canvas mCanvas;

    public ChatImageView(Context context) {
        this(context, null);
    }

    public ChatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatImageView);
        mArrowHeight = ta.getDimension(R.styleable.ChatImageView_arrow_height, dp2px(20));
        mArrowWidth = ta.getDimension(R.styleable.ChatImageView_arrow_width, mArrowHeight);
        mArrowOffset = ta.getDimension(R.styleable.ChatImageView_offset, mArrowHeight / 2);
        mArrowTop = ta.getDimension(R.styleable.ChatImageView_arrow_top, mArrowHeight);
        mRadius = ta.getDimension(R.styleable.ChatImageView_radius, dp2px(10));
        mDirection = ta.getInteger(R.styleable.ChatImageView_direction, 0);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dp2px(0.5f));
        mPaint.setAntiAlias(true);

        mPaintOutter = new Paint();
        mPaintOutter.setStrokeWidth(dp2px(0.5f));
        mPaintOutter.setAntiAlias(true);
        mPaintOutter.setStyle(Paint.Style.STROKE);

        if (mDirection == DIRECTION_RIGHT) {
            mPaintOutter.setColor(Color.parseColor("#74BE50"));
            mPaint.setColor(Color.parseColor("#A2E75A"));
        } else {
            mPaintOutter.setColor(Color.parseColor("#CCCCCC"));
            mPaint.setColor(Color.WHITE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDirection == DIRECTION_LEFT) {
            canvas.drawPath(drawLeftPath(), mPaint);
            canvas.drawPath(drawLeftPath(), mPaintOutter);
        } else {
            canvas.drawPath(drawRightPath(), mPaint);
            canvas.drawPath(drawRightPath(), mPaintOutter);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private Path drawRightPath() {
        Path path = new Path();
        RectF rectF = new RectF(getPaddingLeft() + dp2px(1), getPaddingTop() + dp2px(1)
                , getRight() - getLeft() - getPaddingRight() - dp2px(1)
                , getBottom() - getTop() - getPaddingBottom() - dp2px(1));
        path.moveTo(rectF.left + mRadius, rectF.top);
        // top line
        path.lineTo(rectF.right - mRadius - mArrowWidth, rectF.top);
        // top right corner
        path.arcTo(new RectF(rectF.right - mArrowWidth - 2 * mRadius, rectF.top
                , rectF.right - mArrowWidth, rectF.top + 2 * mRadius), 270, 90);
        // right arrow
        path.lineTo(rectF.right - mArrowWidth, rectF.top + mArrowTop);
        path.lineTo(rectF.right, rectF.top + mArrowTop + mArrowOffset);
        path.lineTo(rectF.right - mArrowWidth, rectF.top + mArrowTop + mArrowHeight);
        path.lineTo(rectF.right - mArrowWidth, rectF.bottom - mRadius);
        // bottom right corner
        path.arcTo(new RectF(rectF.right - mArrowWidth - 2 * mRadius, rectF.bottom - 2 * mRadius
                , rectF.right - mArrowWidth, rectF.bottom), 0, 90);
        // bottom line
        path.lineTo(rectF.left + mRadius, rectF.bottom);
        //bottom left corner
        path.arcTo(new RectF(rectF.left, rectF.bottom - 2 * mRadius, rectF.left + 2 * mRadius, rectF.bottom), 90, 90);
        // left line
        path.lineTo(rectF.left, rectF.top + mRadius);
        // top left corner
        path.arcTo(new RectF(rectF.left, rectF.top, rectF.left + 2 * mRadius, rectF.top + 2 * mRadius), 180, 90);
        path.close();
        return path;
    }

    private Path drawLeftPath() {
        Path path = new Path();
        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop() + dp2px(1)
                , getRight() - getLeft() - getPaddingRight() - dp2px(1)
                , getBottom() - getTop() - getPaddingBottom() - dp2px(1));
        path.moveTo(rectF.left + mRadius + mArrowWidth, rectF.top);
        // top line
        path.lineTo(rectF.right - mRadius, rectF.top);
        // top right corner
        path.arcTo(new RectF(rectF.right - 2 * mRadius, rectF.top
                , rectF.right, rectF.top + 2 * mRadius), 270, 90);
        // right line
        path.lineTo(rectF.right, rectF.bottom - mRadius);
        // bottom right corner
        path.arcTo(new RectF(rectF.right - 2 * mRadius, rectF.bottom - 2 * mRadius
                , rectF.right, rectF.bottom), 0, 90);
        // bottom line
        path.lineTo(rectF.left + mArrowWidth + mRadius, rectF.bottom);
        // bottom left corner
        path.arcTo(new RectF(rectF.left + mArrowWidth, rectF.bottom - 2 * mRadius
                , rectF.left + mArrowWidth + 2 * mRadius, rectF.bottom), 90, 90);
        // left arrow
        path.lineTo(rectF.left + mArrowWidth, rectF.top + mArrowTop + mArrowHeight);
        path.lineTo(rectF.left, rectF.top + mArrowTop + mArrowOffset);
        path.lineTo(rectF.left + mArrowWidth, rectF.top + mArrowTop);
        path.lineTo(rectF.left + mArrowWidth, rectF.top + mArrowTop - mRadius);
        // top left corner
        path.arcTo(new RectF(rectF.left + mArrowWidth, rectF.top
                , rectF.left + mArrowWidth + 2 * mRadius, rectF.top + 2 * mRadius), 180, 90);
        path.close();
        return path;
    }

    private int dp2px(float dipValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

}
