package com.sxq.backgammon.util;

import com.sxq.backgammon.model.GameResult;
import com.sxq.backgammon.model.Point;
import com.sxq.backgammon.view.GameView.Status;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by SXQ on 2017/6/21.
 */

public class Judge {


    public static GameResult judge(int[][] board) {
        GameResult result = new GameResult();

        final int[][] directions = {
                {0, 1},
                {1, 0},
                {1, 1},
                {1, -1}
        };

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                for (int dir = 0; dir < directions.length; dir++) {
                    Status curStatus = Status.getStatus(board[row][column]);
                    if (curStatus.isEmptyField()) {
                        continue;
                    }
                    int max = 1;
                    result.getWinLine().add(new Point(row, column));
                    int dRow = directions[dir][0];
                    int dColumn = directions[dir][1];
                    while (true) {
                        int nextRow = row + dRow;
                        int nextColumn = column + dColumn;
                        if (nextRow < 0 || nextRow >= board.length || nextColumn < 0 || nextColumn >= board[0].length || max >= 5) {
                            break;
                        }
                        Status nextStatus = Status.getStatus(board[nextRow][nextColumn]);
                        if ((curStatus.isBlack() && nextStatus.isBlack()) || (curStatus.isWhite() && nextStatus.isWhite())) {
                            max++;
                            result.getWinLine().add(new Point(nextRow, nextColumn));
                        } else {
                            break;
                        }
                        dRow += directions[dir][0];
                        dColumn += directions[dir][1];
                    }

                    //反方向
                    dRow = -directions[dir][0];
                    dColumn = -directions[dir][1];
                    while (true) {
                        int nextRow = row + dRow;
                        int nextColumn = column + dColumn;
                        if (nextRow < 0 || nextRow >= board.length || nextColumn < 0 || nextColumn >= board[0].length || max >= 5) {
                            break;
                        }
                        Status nextStatus = Status.getStatus(board[nextRow][nextColumn]);
                        if ((curStatus.isBlack() && nextStatus.isBlack()) || (curStatus.isWhite() && nextStatus.isWhite())) {
                            max++;
                            result.getWinLine().add(new Point(nextRow, nextColumn));
                        } else {
                            break;
                        }
                        dRow -= directions[dir][0];
                        dColumn -= directions[dir][1];
                    }

                    if (max >= 5) {
                        if (curStatus.isBlack()) {
                            result.setResult(GameResult.BLACK_WIN);
                        } else {
                            result.setResult(GameResult.WHITE_WIN);
                        }
                        Collections.sort(result.getWinLine(), new Comparator<Point>() {
                            @Override
                            public int compare(Point o1, Point o2) {
                                if (o1.getRow() > o2.getRow()) {
                                    return -1;
                                } else if (o1.getRow() < o2.getRow()) {
                                    return 1;
                                } else {
                                    if (o1.getColumn() > o2.getColumn()) {
                                        return -1;
                                    } else if (o1.getColumn() < o2.getColumn()) {
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                }
                            }
                        });
                        return result;
                    } else {
                        result.setResult(GameResult.HAS_NO_RESULT);
                        result.getWinLine().clear();
                    }
                }
            }
        }
        return result;
    }

}
