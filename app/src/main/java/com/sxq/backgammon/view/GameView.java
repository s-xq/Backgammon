package com.sxq.backgammon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sxq.backgammon.R;
import com.sxq.backgammon.util.ServicesLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by SXQ on 2017/5/28.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * 默认列数
     */
    private static final int DEFAULT_CLOUMN_COUNT = 10;
    /**
     * 默认行数
     */
    private static final int DEFAULT_ROW_COUNT = 10;
    /**
     * 默认棋子半径长度占方格边长的百分比
     */
    private static final float DEFAULT_RADIUS_PERCENT = 0.25f;
    /**
     * 默认棋子红点标志占棋子半径的百分比
     */
    private static final float DEFAULT_FLAG_RADIUS_PERCENT = 0.25F;

    /**
     * 默认边界padding
     * 单位：dp
     */
    private static final float DEFAULT_BOARD_PADDING = 100;
    /**
     * 默认线的宽度
     * 单位：dp
     */
    private static final float DEFAULT_LINE_WIDTH = 5;

    /**
     * 默认背景
     */
    private static final int DEFAULT_BOARD_BACKGROUND = R.drawable.default_gameview_background;
    /**
     * 默认线条颜色
     * 黑色，ARGB
     */
    private static final int DEFAULT_LINE_COLOR = 0xFF000000;
    /**
     * 默认棋子中心的标志颜色
     * 红色，ARGB
     */
    private static final int DEFAULT_CHESS_CENTER_COLOR = 0xFFFF0000;

    private int mCloumnCount = DEFAULT_CLOUMN_COUNT;
    private int mRowCount = DEFAULT_ROW_COUNT;
    private float mRadiusPercent = DEFAULT_RADIUS_PERCENT;
    private float mFlagRadiusPercent = DEFAULT_FLAG_RADIUS_PERCENT;
    private float mBoardPadding = DEFAULT_BOARD_PADDING;
    private float mLineWidth = DEFAULT_LINE_WIDTH;
    private int mBoardBackGround = DEFAULT_BOARD_BACKGROUND;
    private int mLineColor = DEFAULT_LINE_COLOR;
    private int mChessCenterColor = DEFAULT_CHESS_CENTER_COLOR;

    private Bitmap mBlackChessBitmap = null;
    private Bitmap mWhiteChessBitmap = null;
    private Bitmap mBackgroundBitmap = null ;
    private Paint mLinePaint;
    private Paint mFlagPaint;

    private float mGridWidth;
    private float mGridHeight;
    private float mChessRadius;
    private float[] mHorizontalLines;
    private float[] mVerticalLines;
    private int[][] mBoard = new int[DEFAULT_ROW_COUNT][DEFAULT_CLOUMN_COUNT];
    private int mLastChessCloumn = -1;
    private int mLastChessRow = -1;
    private boolean mIsNextWhite = false;


    private OnPutChessListener mOnPutChessListener = null;

    private UpdateViewThread mUpdateViewThread = null;
    private SurfaceHolder mSurfaceHolder = null;


    public GameView(Context context) {
        this(context, null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ServicesLog.d("onMeasure");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        /**
         * 设置为正方形棋盘
         */
        setMeasuredDimension(width, width);
        calculate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
//                ServicesLog.d("onTouchEvent MotionEvent.ACTION_DOWN");
                break;
            }

            case MotionEvent.ACTION_MOVE: {
//                ServicesLog.d("onTouchEvent MotionEvent.ACTION_MOVE");
                break;
            }

            case MotionEvent.ACTION_UP: {
                ServicesLog.d("onTouchEvent MotionEvent.ACTION_UP");
                float x = event.getX();
                float y = event.getY();
                int cloumn = Math.round(Math.abs(x - mBoardPadding) / mGridWidth);
                int row = Math.round(Math.abs(y - mBoardPadding) / mGridHeight);
                if (cloumn >= mCloumnCount) {
                    cloumn = mCloumnCount - 1;
                }
                if (row >= mRowCount) {
                    row = mRowCount - 1;
                }

                Status status = Status.getStatus(mBoard[row][cloumn]);
                if (status.isEmptyField()) {
                    if (mLastChessCloumn != -1 && mLastChessRow != -1) {
                        if (Status.getStatus(mBoard[mLastChessRow][mLastChessCloumn]) == Status.LAST_BLACK) {
                            mBoard[mLastChessRow][mLastChessCloumn] = Status.BLACK.ordinal();
                        } else if (Status.getStatus(mBoard[mLastChessRow][mLastChessCloumn]) == Status.LAST_WHITE) {
                            mBoard[mLastChessRow][mLastChessCloumn] = Status.WHITE.ordinal();
                        }
                    }
                    if (mIsNextWhite) {
                        mBoard[row][cloumn] = Status.LAST_WHITE.ordinal();
                    } else {
                        mBoard[row][cloumn] = Status.LAST_BLACK.ordinal();
                    }
                    mLastChessCloumn = cloumn;
                    mLastChessRow = row;
                    mIsNextWhite = !mIsNextWhite;
                    mUpdateViewThread.notifyUpdate();
                } else {
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ServicesLog.d("surfaceCreated");
        mSurfaceHolder = holder;
        if (mUpdateViewThread == null) {
            mUpdateViewThread = new UpdateViewThread();
            mUpdateViewThread.start();
            ServicesLog.d("开启线程");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ServicesLog.d("surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ServicesLog.d("surfaceDestroyed");
        if (mUpdateViewThread != null) {
            mUpdateViewThread.requestExit();
            /**
             * Surface销毁之后，重建，重新开启新线程
             */
            mUpdateViewThread = null;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        ServicesLog.d("onSaveInstanceState");
        return super.onSaveInstanceState();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
/*        drawLines(canvas);
        drawChess(canvas , mBoard);*/
    }

    public void draw(int[][] board) {
        mBoard = board;
        mRowCount = mBoard.length;
        mCloumnCount = mBoard[0].length;
        for (int i = 0; i < mRowCount; i++) {
            for (int j = 0; j < mCloumnCount; j++) {
                Status status = Status.getStatus(mBoard[i][j]);
                if (status == Status.LAST_BLACK || status == Status.LAST_WHITE) {
                    mLastChessRow = i;
                    mLastChessCloumn = j;
                    if (status == Status.LAST_BLACK) {
                        mIsNextWhite = true;
                    } else {
                        mIsNextWhite = false;
                    }
                }
            }
        }
        calculate();
        if (mUpdateViewThread != null) {
            mUpdateViewThread.notifyUpdate();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        ServicesLog.d("初始化");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameView);
        mLineWidth = typedArray.getDimension(R.styleable.GameView_line_width, DEFAULT_LINE_WIDTH);
        mLineColor = typedArray.getColor(R.styleable.GameView_line_color, DEFAULT_LINE_COLOR);
        mBoardBackGround = typedArray.getResourceId(R.styleable.GameView_board_background, DEFAULT_BOARD_BACKGROUND);
        mCloumnCount = typedArray.getInteger(R.styleable.GameView_cloumn, DEFAULT_CLOUMN_COUNT);
        mRowCount = typedArray.getInteger(R.styleable.GameView_row, DEFAULT_ROW_COUNT);
        mBoardPadding = typedArray.getDimension(R.styleable.GameView_board_padding, DEFAULT_BOARD_PADDING);
        mRadiusPercent = typedArray.getFraction(R.styleable.GameView_radius_percent, 1, 1, DEFAULT_RADIUS_PERCENT);
        mFlagRadiusPercent = typedArray.getFraction(R.styleable.GameView_flag_radius_percent, 1, 1, DEFAULT_FLAG_RADIUS_PERCENT);
        mChessCenterColor = typedArray.getColor(R.styleable.GameView_chess_center_color, DEFAULT_CHESS_CENTER_COLOR);
        typedArray.recycle();


        mBlackChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_black);
        mWhiteChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_white);
        mBackgroundBitmap = BitmapFactory.decodeResource(context.getResources() , mBoardBackGround);
        /**
         * ERROR 设置背景图将导致在surface异步线程中绘制的视图被该背景图覆盖，正确做法是在异步线程中绘制背景图，然后再绘制前景视图
         */
//        setBackgroundResource(mBoardBackGround);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);

        mFlagPaint = new Paint();
        mFlagPaint.setAntiAlias(true);
        mFlagPaint.setColor(mChessCenterColor);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        /**
         * ERROR 默认为false，此时onTouchEvent只会监听到MotionEvent.ACTION_DOWN
         */
        setClickable(true);
    }

    /**
     * 设置更新完成的监听
     *
     * @param listener
     */
    public void setOnPutChessListener(OnPutChessListener listener) {
        this.mOnPutChessListener = listener;
    }

    /**
     * 画线
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        ServicesLog.d("画线");
        canvas.drawLines(mHorizontalLines, mLinePaint);
        canvas.drawLines(mVerticalLines, mLinePaint);
    }

    /**
     * 画棋子
     *
     * @param canvas
     * @param board  棋盘格局，值为Status
     */
    private void drawChess(Canvas canvas, int[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int cloumn = 0; cloumn < board[0].length; cloumn++) {
                Status status = Status.getStatus(board[row][cloumn]);
                float centerR = row * mGridHeight + mBoardPadding;
                float centerC = cloumn * mGridWidth + mBoardPadding;
                float top = centerR - mChessRadius;
                float left = centerC - mChessRadius;
                float right = centerC + mChessRadius;
                float bottom = centerR + mChessRadius;
                RectF rectF = new RectF(left, top, right, bottom);
                if (status == Status.BLACK || status == Status.LAST_BLACK) {
                    canvas.drawBitmap(mBlackChessBitmap, null, rectF, null);
//                    ServicesLog.d(String.format("画棋子，row=%d,cloumn=%d，矩形=%s", row, cloumn, rectF.toShortString()));
                } else if (status == Status.WHITE || status == Status.LAST_WHITE) {
                    canvas.drawBitmap(mWhiteChessBitmap, null, rectF, null);
//                    ServicesLog.d(String.format("画棋子，row=%d,cloumn=%d，矩形=%s", row, cloumn, rectF.toShortString()));
                }

                if (status == Status.LAST_BLACK || status == Status.LAST_WHITE) {
                    canvas.drawCircle(centerC, centerR, mChessRadius * mFlagRadiusPercent, mFlagPaint);
//                    ServicesLog.d(String.format("画棋子，row=%d,cloumn=%d，矩形=%s", row, cloumn, rectF.toShortString()));
                }
            }
        }
    }

    /**
     * 绘制背景图
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas){
        canvas.drawBitmap(mBackgroundBitmap , null , new RectF(0 , 0 , getWidth() , getHeight()) ,null );
    }
    private void calculate() {
        ServicesLog.d("重新计算");
        mGridWidth = (getMeasuredWidth() - mBoardPadding * 2) / (mCloumnCount - 1);
        mGridHeight = (getMeasuredHeight() - mBoardPadding * 2) / (mRowCount - 1);
        mChessRadius = mRadiusPercent * mGridWidth;

        mHorizontalLines = new float[mRowCount * 4];
        for (int i = 0; i < mRowCount * 4; i += 4) {
            mHorizontalLines[i] = mBoardPadding;
            mHorizontalLines[i + 1] = i / 4 * mGridHeight + mBoardPadding;
            mHorizontalLines[i + 2] = mBoardPadding + (mCloumnCount - 1) * mGridWidth;
            mHorizontalLines[i + 3] = i / 4 * mGridHeight + mBoardPadding;
//            ServicesLog.d(String.format("mHorizontalLines{i=%d,x0=%f,y0=%f,x1=%f,y1=%f}", i, mHorizontalLines[i], mHorizontalLines[i + 1], mHorizontalLines[i + 2], mHorizontalLines[i + 3]));
        }

        mVerticalLines = new float[mCloumnCount * 4];
        for (int i = 0; i < mCloumnCount * 4; i += 4) {
            mVerticalLines[i] = i / 4 * mGridWidth + mBoardPadding;
            mVerticalLines[i + 1] = mBoardPadding;
            mVerticalLines[i + 2] = i / 4 * mGridWidth + mBoardPadding;
            mVerticalLines[i + 3] = mBoardPadding + (mRowCount - 1) * mGridHeight;
//            ServicesLog.d(String.format("mVerticalLines{i=%d,x0=%f,y0=%f,x1=%f,y1=%f}", i, mVerticalLines[i], mVerticalLines[i + 1], mVerticalLines[i + 2], mVerticalLines[i + 3]));
        }
    }

    /**
     * 棋盘更新的监听
     * 主线程
     */
    public interface OnPutChessListener {
        /**
         * @param board 当前棋子格局
         */
        void onPutChess(int[][] board);
    }

    public enum Status {
        /**
         * 无棋子
         */
        NO_CHESS,
        /**
         * 无棋子，有黑点
         */
        NO_CHESS_BLACK_POINT,
        /**
         * 普通黑子
         */
        BLACK,
        /**
         * 普通白子
         */
        WHITE,
        /**
         * 刚下完的黑子，中心带标志
         */
        LAST_BLACK,
        /**
         * 刚下完的白子，中心带标志
         */
        LAST_WHITE;

        public static Status getStatus(int index) {
            for (Status status : Status.values()) {
                if (status.ordinal() == index) {
                    return status;
                }
            }
            return null;
        }

        public boolean isEmptyField() {
            return this == Status.NO_CHESS || this == Status.NO_CHESS_BLACK_POINT;
        }
    }

    class UpdateViewThread extends Thread {
        private boolean mIsDone = false;
        private volatile boolean mShouldWaitUpdate = false;

        public UpdateViewThread() {
            super();
            ServicesLog.d("UpdateViewThread初始化");
        }

        @Override
        public void run() {
            ServicesLog.d("UpdateViewThread开始运行");

            try {
                while (!mIsDone && !Thread.interrupted()) {
                    synchronized (this) {
                        while (mShouldWaitUpdate) {
                            ServicesLog.d("等待更新视图");
                            wait();
                        }
                    }
                    update();
                    mShouldWaitUpdate = true;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                mIsDone = true;
            }
        }

        private void update() {
            ServicesLog.d("更新视图");
            Canvas canvas = mSurfaceHolder.lockCanvas();
            GameView.this.drawBackground(canvas);
            GameView.this.drawLines(canvas);
            GameView.this.drawChess(canvas, GameView.this.mBoard);
            mSurfaceHolder.unlockCanvasAndPost(canvas);

            GameView.this.post(new Runnable() {
                @Override
                public void run() {
                    if (GameView.this.mOnPutChessListener != null) {
                        GameView.this.mOnPutChessListener.onPutChess(GameView.this.mBoard);
                    }
                }
            });
        }

        /**
         * 把这个线程标记为完成，并合并到主程序线程
         */
        public void requestExit() {
            mIsDone = true;
            try {
                join();
            } catch (InterruptedException ex) {

            }
        }

        /**
         * 通知线程更新SurfaceView
         */
        public void notifyUpdate() {
            if (!mIsDone) {
                synchronized (UpdateViewThread.this) {
                    this.mShouldWaitUpdate = false;
                    /**
                     * ERROR 线程notifyAll()时需要先加锁synchronized
                     */
                    UpdateViewThread.this.notifyAll();
                }
            }
        }

    }
}
