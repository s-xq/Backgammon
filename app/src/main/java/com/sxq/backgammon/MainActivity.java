package com.sxq.backgammon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sxq.backgammon.util.ServicesLog;
import com.sxq.backgammon.view.GameView;

public class MainActivity extends AppCompatActivity {


    GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            ServicesLog.d("MainActivity onCreate:" + savedInstanceState.toString());
        } else {
            ServicesLog.d("MainActivity onCreate:");
        }
        mGameView = (GameView) findViewById(R.id.gameView);
        mGameView.setOnPutChessListener(new GameView.OnPutChessListener() {
            @Override
            public void onPutChess(int[][] board) {
//                ServicesLog.d(board);
                ServicesLog.d("视图更新回调");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ServicesLog.d("onSaveInstanceState:" + outState.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ServicesLog.d("onRestoreInstanceState:" + savedInstanceState.toString());
        super.onRestoreInstanceState(savedInstanceState);
    }
}
