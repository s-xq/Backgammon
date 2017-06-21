package com.sxq.backgammon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.sxq.backgammon.util.ServicesLog;
import com.sxq.backgammon.view.GameView;

public class MainActivity extends AppCompatActivity {


    GameView mGameView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServicesLog.d("MainActivity onCreate");
        mGameView = (GameView)findViewById(R.id.gameView);
        mGameView.setOnPutChessListener(new GameView.OnPutChessListener() {
            @Override
            public void onPutChess(int[][] board) {
                ServicesLog.d(board);
            }
        });
    }

}
