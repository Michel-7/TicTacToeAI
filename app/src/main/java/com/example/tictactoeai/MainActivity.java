package com.example.tictactoeai;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private GameLogic gameLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameLogic = new GameLogic();

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int index = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = (Button) gridLayout.getChildAt(index);
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerMove(row, col);
                    }
                });
                index++;
            }
        }
    }

    private void playerMove(int row, int col) {
        if (buttons[row][col].getText().toString().isEmpty() && !gameLogic.isGameOver()) {
            buttons[row][col].setText("O");
            gameLogic.updateGameState(row, col);

            if (gameLogic.isGameOver()) {
                showGameResult();
            } else {
                aiMoveMiniMax();
            }
        }
    }
    private void showGameResult() {
        GameLogic.GameResults results = gameLogic.getGameResults();

        if (results.winner == ' ') {
            Toast.makeText(this, "Draw!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Winner: " + results.winner, Toast.LENGTH_LONG).show();
            highlightWinningCombination(results.winningCombination);
        }
    }

    private void highlightWinningCombination(List<Pair<Integer, Integer>> winningCombination) {
        for (Pair<Integer, Integer> position : winningCombination) {
            int row = position.first;
            int col = position.second;
            buttons[row][col].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }

    private void aiMoveRandom() {
        new Handler().postDelayed(() -> {
            List<Pair<Integer, Integer>> availableMoves = gameLogic.getAvailableMoves();

            if (!availableMoves.isEmpty()) {
                Random random = new Random();
                Pair<Integer, Integer> move = availableMoves.get(random.nextInt(availableMoves.size()));

                int row = move.first;
                int col = move.second;

                buttons[row][col].setText("X");
                gameLogic.updateGameState(row, col);

                if (gameLogic.isGameOver()) {
                    showGameResult();
                }
            }
        }, 500);
    }

    private void aiMoveMiniMax() {
        new Handler().postDelayed(() -> {
            Pair<Integer, Integer> move = gameLogic.getBestMove();

            if (move != null) {
                int row = move.first;
                int col = move.second;

                buttons[row][col].setText("X");
                gameLogic.updateGameState(row, col);

                if (gameLogic.isGameOver()) {
                    showGameResult();
                }
            }
        }, 500);
    }

}

