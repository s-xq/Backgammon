package com.sxq.backgammon.model;

/**
 * Created by SXQ on 2017/5/28.
 */

public class Point {

    private int mColumn;
    private int mRow;

    public Point(int row, int column) {
        mColumn = column;
        mRow = row;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int column) {
        mColumn = column;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        mRow = row;
    }


}
