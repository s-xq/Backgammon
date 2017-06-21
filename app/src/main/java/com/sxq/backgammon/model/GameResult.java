package com.sxq.backgammon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SXQ on 2017/6/21.
 */

public class GameResult {

    /**
     * 尚未分出胜负
     */
    public static final int HAS_NO_RESULT = 0;

    /**
     * 黑子胜出
     */
    public static final int BLACK_WIN = 1;

    /**
     * 白子胜出
     */
    public static final int WHITE_WIN = 2;

    private int mResult = HAS_NO_RESULT;

    private List<Point> winLine = new ArrayList<>();


    public int getResult() {
        return mResult;
    }

    public void setResult(int result) {
        mResult = result;
    }

    public List<Point> getWinLine() {
        return winLine;
    }

    public void setWinLine(List<Point> winLine) {
        this.winLine = winLine;
    }
}
