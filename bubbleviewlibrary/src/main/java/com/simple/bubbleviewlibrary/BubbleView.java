package com.simple.bubbleviewlibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BubbleView extends View {

    private static final String TAG = "BubbleView";
    //贝塞尔曲线的path
    private Path mPath = new Path();
    //圆的画笔
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文字的画笔
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //开始的圆
    private Circle startCircle;
    //结束的圆
    private Circle endCircle;
    //两个圆连接的点
    private Point startCircleA = new Point();
    private Point startCircleB = new Point();
    private Point endCircleC = new Point();
    private Point endCircleD = new Point();
    //控制贝塞尔曲线的点
    private Point quadControlE = new Point();
    //圆心相距的距离
    private float circleCenterDistan = 0;
    //是否能画path
    private boolean canDrawPath = true;
    //默认的文本
    private String text = "";
    //圆的颜色
    private int circle_color;
    //文字的颜色
    private int text_color;
    //文字的大小
    private float text_size;
    //粒子的集合
    private List<Particle> particleList = new ArrayList<>();
    //是否画粒子
    private boolean canDrawParticle = false;
    //粒子的画笔
    private Paint particlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //生成粒子的bitmap
    private Bitmap bitmap;
    //
    private float lastDistan;
    //动画结束的监听
    private OnAnimationEndListener mOnAnimationEndListener;
    private int mWidth;
    private int mHeight;

    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BezierView);
        text = ta.getString(R.styleable.BezierView_text);
        circle_color = ta.getColor(R.styleable.BezierView_circle_color, Color.RED);
        text_color = ta.getColor(R.styleable.BezierView_text_color, Color.WHITE);
        text_size = ta.getDimension(R.styleable.BezierView_text_size, Density.dp2px(getContext(), 15));
        ta.recycle();
        init();
    }

    private void init() {
        circlePaint.setColor(circle_color);
        textPaint.setColor(text_color);
        textPaint.setTextSize(text_size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width_size = MeasureSpec.getSize(widthMeasureSpec);
        if (width_mode == MeasureSpec.EXACTLY) {
            widthMeasureSpec = width_size;
        } else {
            widthMeasureSpec = Density.dp2px(getContext(), 15);
        }
        int height_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height_size = MeasureSpec.getSize(heightMeasureSpec);
        if (height_mode == MeasureSpec.EXACTLY) {
            heightMeasureSpec = height_size;
        } else {
            heightMeasureSpec = Density.dp2px(getContext(), 15);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        startCircle = new Circle(w / 2, w / 2, h / 2);
        endCircle = new Circle(w / 2, w / 2, h / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canDrawPath) {
            drawPath(canvas);
            drawCircle(canvas, startCircle);
        }
        if (canDrawParticle) {
            drawParticle(canvas, particleList);
            return;
        }
        drawCircle(canvas, endCircle);
        drawText(canvas, endCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bitmap = createBitmap();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                endCircle.set(x, y);
                computePath();
                invalidate();
                lastDistan = circleCenterDistan;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                if (!canDrawPath) {//
                    generateParticles(bitmap);
                    startAnimation();
//                    invalidate();
                } else {
                    endCircle.set(startCircle.getX(), startCircle.getY());
                    startCircle.setRadius(endCircle.getRadius());
                    computePath();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (canDrawPath) {
                    endCircle.set(startCircle.getX(), startCircle.getY());
                    startCircle.setRadius(endCircle.getRadius());
                    computePath();
                    invalidate();
                }
                break;
        }
        return true;
    }


    /**
     * 生成粒子
     */
    private void generateParticles(Bitmap bitmap) {
        canDrawParticle = true;
        float particleRadius = endCircle.getRadius() * 2 / Particle.particleCount / 2;
        int bitmap_w = bitmap.getWidth();
        int bitmap_h = bitmap.getHeight();
        for (int i = 0; i < Particle.particleCount; i++) {
            for (int j = 0; j < Particle.particleCount; j++) {
                float width = endCircle.getX() - endCircle.getRadius() + i * particleRadius * 2;
                float height = endCircle.getY() - endCircle.getRadius() + j * particleRadius * 2;
                int color = bitmap.getPixel(bitmap_w / Particle.particleCount * i,
                        bitmap_h / Particle.particleCount * j);
                Particle particle = new Particle(width, height, particleRadius, color);
                particleList.add(particle);
            }
        }
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (Particle particle : particleList) {
                    particle.broken((Float) animation.getAnimatedValue(), getMeasuredWidth(),
                            getMeasuredHeight());
                }
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                clearStatus();
                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener.onEnd(BubbleView.this);
                    Log.d(TAG, "onAnimationEnd");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    /**
     * 生成粒子需要的bitmap
     */
    private Bitmap createBitmap() {
        int w_h = (int) (endCircle.getRadius() * 2);
        Bitmap bitmap = Bitmap.createBitmap(w_h, w_h, Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            draw(canvas);
            invalidate();
        }
        return bitmap;
    }

    /**
     * 计算path
     */
    private void computePath() {
        float startX = startCircle.x;
        float startY = startCircle.y;
        float endX = endCircle.x;
        float endY = endCircle.y;
        //计算圆心的距离
        circleCenterDistan = (float) Math.sqrt(Math.pow(startX - endX, 2)
                + Math.pow(startY - endY, 2));
        //如果圆心的距离大于半径的5倍或者起点圆半径小于等于原来半径的1/5
        if (circleCenterDistan > endCircle.getRadius() * 5
                || startCircle.getRadius() <= endCircle.getRadius() / 5) {
            canDrawPath = false;
            return;
        }
//        startCircle.setRadius(endCircle.getRadius() - startCircleRadius * 5 / endCircle.getRadius());
        //计算起点圆的半径
        if (circleCenterDistan > endCircle.getRadius()) {
            startCircle.setRadius(startCircle.getRadius()
                    - (circleCenterDistan - lastDistan) / 5);
        }
        float cos = (endY - startY) / circleCenterDistan;
        float sin = (endX - startX) / circleCenterDistan;

        float xA = startX - startCircle.getRadius() * cos;
        float yA = startY + startCircle.getRadius() * sin;
        startCircleA.set(xA, yA);
        float xB = startX + startCircle.getRadius() * cos;
        float yB = startY - startCircle.getRadius() * sin;
        startCircleB.set(xB, yB);
        float xC = endX + endCircle.getRadius() * cos;
        float yC = endY - endCircle.getRadius() * sin;
        endCircleC.set(xC, yC);
        float xD = endX - endCircle.getRadius() * cos;
        float yD = endY + endCircle.getRadius() * sin;
        endCircleD.set(xD, yD);

        float xE = startX - (startX - endX) / 2;
        float yE = startY + (endY - startY) / 2;
        quadControlE.set(xE, yE);
    }

    /**
     * 画路径
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(startCircleA.x, startCircleA.y);
        mPath.lineTo(startCircleB.x, startCircleB.y);
        mPath.quadTo(quadControlE.x, quadControlE.y, endCircleC.x, endCircleC.y);
        mPath.lineTo(endCircleD.x, endCircleD.y);
        mPath.quadTo(quadControlE.x, quadControlE.y, startCircleA.x, startCircleA.y);
        canvas.drawPath(mPath, circlePaint);
    }

    /**
     * 画圆
     *
     * @param canvas
     * @param circle
     */
    private void drawCircle(Canvas canvas, Circle circle) {
        canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), circlePaint);
    }

    /**
     * 画字
     *
     * @param canvas
     * @param endCircle
     */
    private void drawText(Canvas canvas, Circle endCircle) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        float textWidth = textPaint.measureText(text);
        float x = endCircle.x - textWidth / 2;
        float y = endCircle.y + (rect.bottom - rect.top) / 2;
        canvas.drawText(text, x, y, textPaint);
    }

    /**
     * 画粒子
     *
     * @param canvas
     * @param particleList
     */
    private void drawParticle(Canvas canvas, List<Particle> particleList) {
        for (Particle particle : particleList) {
            particlePaint.setColor(particle.color);
            canvas.drawCircle(particle.cx, particle.cy, particle.radius, particlePaint);
        }
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    /**
     * 设置文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
        invalidate();
    }

    /**
     * 设置圆的颜色
     *
     * @param circleColor
     */
    public void setCircleColor(int circleColor) {
        circlePaint.setColor(circleColor);
        invalidate();
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        textPaint.setTextSize(size);
    }

    /**
     * 刷新状态
     */
    public void clearStatus() {
        mPath.reset();
        canDrawPath = true;
        canDrawParticle = false;
        endCircle.set(mWidth / 2, mHeight / 2);
        startCircle.set(mWidth / 2, mHeight / 2);
        startCircle.setRadius(mWidth / 2);
        circleCenterDistan = 0;
        computePath();
        invalidate();
    }

    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener) {
        this.mOnAnimationEndListener = onAnimationEndListener;
    }

    public interface OnAnimationEndListener {
        void onEnd(BubbleView bubbleView);
    }

}
