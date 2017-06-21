package com.sxq.backgammon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sxq.backgammon.model.GameResult;
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
            public void onPutChess(int[][] board, GameResult result) {
                ServicesLog.d("视图更新回调");
                if (result.getResult() != GameResult.HAS_NO_RESULT) {
                    if (result.getResult() == GameResult.BLACK_WIN) {
                        Toast.makeText(MainActivity.this, "黑子赢", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "白子赢", Toast.LENGTH_SHORT).show();
                    }
                }
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
