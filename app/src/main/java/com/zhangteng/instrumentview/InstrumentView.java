package com.zhangteng.instrumentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by swing on 2018/4/9.
 */
public class InstrumentView extends View {
    private Context myContext;

    private Paint backgroundPaint;
    private Paint centerCirclePaint;
    private Paint firstRingPaint;
    private Paint secondRingPaint;
    private Paint progressBackgroundPaint;
    private Paint progressPaint;
    private Paint tikePaint;
    private Paint tikeTextPaint;
    private Paint textPaint;
    private int backgroundRadius;
    private int backgroundColor;
    private int minCircleRadius;
    private int minCircleColor;
    private int firstRingRadius;
    private int firstRingColor;
    private int secondRingRadius;
    private int secondRingColor;
    private int progressRadius;
    private int progressBackgroundColor;
    private int progressStartColor;
    private int progressEndColor;
    private float percent;
    private int longLine;
    private int shortLine;
    private int lineColor;
    private int tikeGroup;
    private int tikeTextSize;
    private int textSize;
    private int textColor;
    private String textStr;
    private String centerStr;
    private CharSequence[] tikeStr;
    private int mWidth;
    private int mHeight;


    public InstrumentView(Context context) {
        this(context, null);
    }

    public InstrumentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstrumentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.myContext = context;
        initAttrs(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        minCircleRadius = Math.min(mHeight, mWidth) / 48 == 0 ? dp2px(4) : Math.min(mHeight, mWidth) / 48;
        minCircleColor = getResources().getColor(R.color.shadow);
        firstRingRadius = minCircleRadius * 3;
        firstRingColor = getResources().getColor(R.color.outsideBlue);
        secondRingRadius = minCircleRadius * 7;
        secondRingColor = getResources().getColor(R.color.insideBlue);
        progressRadius = minCircleRadius * 21;
        progressBackgroundColor = getResources().getColor(R.color.shadow);
        progressStartColor = getResources().getColor(R.color.start);
        progressEndColor = getResources().getColor(R.color.end);
        backgroundRadius = minCircleRadius * 24;
        backgroundColor = getResources().getColor(R.color.background);
        longLine = minCircleRadius * 3;
        shortLine = minCircleRadius * 2;
        tikeGroup = 5;
        lineColor = getResources().getColor(R.color.PointerColor);
        tikeStr = new String[]{"100KB", "400KB", "700KB", "1M", "1.3M", "1.6M", "1.9M"};
        tikeTextSize = sp2px(10);
        textSize = sp2px(16);
        textColor = getResources().getColor(R.color.textColor);
        centerStr = "当前速度";
        textStr = "0.0m/s";
        percent = 0.1f;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InstrumentView, defStyleAttr, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.InstrumentView_backgroundColor:
                    backgroundColor = typedArray.getColor(R.styleable.InstrumentView_backgroundColor, getResources().getColor(R.color.background));
                    break;
                case R.styleable.InstrumentView_minCircleRadius:
                    minCircleRadius = (int) typedArray.getDimension(R.styleable.InstrumentView_minCircleRadius, dp2px(4));
                    firstRingRadius = minCircleRadius * 3;
                    secondRingRadius = minCircleRadius * 7;
                    progressRadius = minCircleRadius * 21;
                    backgroundRadius = minCircleRadius * 24;
                    longLine = minCircleRadius;
                    shortLine = minCircleRadius / 2;
                    break;
                case R.styleable.InstrumentView_minCircleColor:
                    minCircleColor = typedArray.getColor(R.styleable.InstrumentView_minCircleColor, getResources().getColor(R.color.shadow));
                    break;
                case R.styleable.InstrumentView_firstRingColor:
                    firstRingColor = typedArray.getColor(R.styleable.InstrumentView_firstRingColor, getResources().getColor(R.color.outsideBlue));
                    break;
                case R.styleable.InstrumentView_secondRingColor:
                    secondRingColor = typedArray.getColor(R.styleable.InstrumentView_secondRingColor, getResources().getColor(R.color.insideBlue));
                    break;
                case R.styleable.InstrumentView_progressBackgroundColor:
                    progressBackgroundColor = typedArray.getColor(R.styleable.InstrumentView_progressBackgroundColor, getResources().getColor(R.color.shadow));
                    break;
                case R.styleable.InstrumentView_progressStartColor:
                    progressStartColor = typedArray.getColor(R.styleable.InstrumentView_progressStartColor, getResources().getColor(R.color.start));
                    break;
                case R.styleable.InstrumentView_progressEndColor:
                    progressEndColor = typedArray.getColor(R.styleable.InstrumentView_progressEndColor, getResources().getColor(R.color.end));
                    break;
                case R.styleable.InstrumentView_percent:
                    percent = typedArray.getFloat(R.styleable.InstrumentView_percent, 0.1f);
                    break;
                case R.styleable.InstrumentView_pointerColor:
                    lineColor = typedArray.getColor(R.styleable.InstrumentView_pointerColor, getResources().getColor(R.color.PointerColor));
                    break;
                case R.styleable.InstrumentView_tikeGroup:
                    tikeGroup = typedArray.getInteger(R.styleable.InstrumentView_tikeGroup, 5);
                    break;
                case R.styleable.InstrumentView_tikeStr:
                    tikeStr = typedArray.getTextArray(R.styleable.InstrumentView_tikeStr);
                    break;
                case R.styleable.InstrumentView_tikeTextSize:
                    tikeTextSize = (int) typedArray.getDimension(R.styleable.InstrumentView_tikeTextSize, sp2px(10));
                    break;
                case R.styleable.InstrumentView_textSize:
                    textSize = (int) typedArray.getDimension(R.styleable.InstrumentView_textSize, sp2px(16));
                    break;
                case R.styleable.InstrumentView_textColor:
                    textColor = typedArray.getColor(R.styleable.InstrumentView_textColor, getResources().getColor(R.color.textColor));
                    break;
                case R.styleable.InstrumentView_centerStr:
                    centerStr = typedArray.getString(R.styleable.InstrumentView_centerStr);
                    break;
                case R.styleable.InstrumentView_textStr:
                    textStr = typedArray.getString(R.styleable.InstrumentView_textStr);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        backgroundPaint = new Paint();
        backgroundPaint.setDither(true);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(2);
        backgroundPaint.setColor(backgroundColor);

        centerCirclePaint = new Paint();
        centerCirclePaint.setDither(true);
        centerCirclePaint.setAntiAlias(true);
        centerCirclePaint.setStyle(Paint.Style.FILL);
        centerCirclePaint.setStrokeWidth(2);
        centerCirclePaint.setColor(minCircleColor);

        firstRingPaint = new Paint();
        firstRingPaint.setAntiAlias(true);
        firstRingPaint.setDither(true);
        firstRingPaint.setStyle(Paint.Style.STROKE);
        firstRingPaint.setStrokeWidth(minCircleRadius * 2);
        firstRingPaint.setColor(firstRingColor);

        secondRingPaint = new Paint();
        secondRingPaint.setAntiAlias(true);
        secondRingPaint.setDither(true);
        secondRingPaint.setStyle(Paint.Style.STROKE);
        secondRingPaint.setStrokeWidth(firstRingRadius);
        secondRingPaint.setColor(secondRingColor);

        progressBackgroundPaint = new Paint();
        progressBackgroundPaint.setAntiAlias(true);
        progressBackgroundPaint.setDither(true);
        progressBackgroundPaint.setStyle(Paint.Style.STROKE);
        progressBackgroundPaint.setStrokeWidth(minCircleRadius * 2);
        progressBackgroundPaint.setColor(progressBackgroundColor);
        progressBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(minCircleRadius * 2);
        progressPaint.setColor(progressStartColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        tikePaint = new Paint();
        tikePaint.setAntiAlias(true);
        tikePaint.setDither(true);
        tikePaint.setStyle(Paint.Style.FILL);
        tikePaint.setStrokeWidth(10);
        tikePaint.setColor(lineColor);

        tikeTextPaint = new Paint();
        tikeTextPaint.setTextAlign(Paint.Align.LEFT);
        tikeTextPaint.setAntiAlias(true);
        tikeTextPaint.setTextSize(tikeTextSize);
        tikeTextPaint.setColor(progressStartColor);

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidthOrHeight(widthMeasureSpec);
        int height = measureWidthOrHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * @param measureSpec 测量细则 包含mode size
     *                    mode 父控件给予的大小权限（exactly 父控件已获取固定大小,atmost 已给出最大值, unspecified 任意大小）
     *                    size 本控件具体大小
     */
    private int measureWidthOrHeight(int measureSpec) {
        if (MeasureSpec.getMode(measureSpec) == MeasureSpec.EXACTLY) {
            return MeasureSpec.getSize(measureSpec);
        } else {
            return dp2px(200);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);
        //绘制背景圆
        canvas.drawCircle(0, 0, Math.min(backgroundRadius, Math.min(mHeight, mWidth) / 2), backgroundPaint);
        //绘制中心圆
        canvas.drawCircle(0, 0, minCircleRadius, centerCirclePaint);
        //绘制第一个圆环
        canvas.drawCircle(0, 0, firstRingRadius, firstRingPaint);
        // 绘制第二个圆环
        canvas.drawCircle(0, 0, secondRingRadius, secondRingPaint);
        //绘制进度背景
        RectF rectF = new RectF(-progressRadius, -progressRadius, progressRadius, progressRadius);
        canvas.drawArc(rectF, 135, 270, false, progressBackgroundPaint);
        //绘制进度
        if (percent > 1.0f) {
            percent = 1.0f;
        } else if (percent < 0.0f) {
            percent = 0.0f;
        }
        canvas.drawArc(rectF, 135, percent * 360, false, progressPaint);
        //绘制刻度
        for (int i = 0; i < tikeStr.length; i++) {
            canvas.save();
            int degrees = 45 - 270 * i / (tikeStr.length - 1);
            canvas.rotate(degrees);
            canvas.translate(minCircleRadius * 18, 0);
            canvas.drawLine(0, 0, longLine, 0, tikePaint);
            canvas.translate(-minCircleRadius * 2, 0);
            canvas.rotate(90);
            Rect bound = new Rect();
            tikeTextPaint.getTextBounds(String.valueOf(tikeStr[i]), 0, tikeStr[i].length(), bound);
            canvas.drawText(String.valueOf(tikeStr[i]), -(bound.left + bound.right) / 2, 0, tikeTextPaint);
            canvas.restore();
            for (int j = 0; j < tikeGroup && i < tikeStr.length - 1; j++) {
                canvas.save();
                canvas.rotate(degrees - 270 * j / ((tikeStr.length - 1) * tikeGroup));
                canvas.translate(minCircleRadius * 19, 0);
                canvas.drawLine(0, 0, shortLine, 0, tikePaint);
                canvas.restore();
            }
        }
        //绘制中心字体
        Rect textBound = new Rect();
        textPaint.getTextBounds(textStr, 0, textStr.length(), textBound);
        canvas.drawText(textStr, -(textBound.left + textBound.right) / 2, minCircleRadius * 18, textPaint);
        Rect centerBound = new Rect();
        textPaint.getTextBounds(centerStr, 0, centerStr.length(), centerBound);
        canvas.drawText(centerStr, -(centerBound.left + centerBound.right) / 2, minCircleRadius * 14, textPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, myContext.getResources().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, myContext.getResources().getDisplayMetrics());
    }
}
