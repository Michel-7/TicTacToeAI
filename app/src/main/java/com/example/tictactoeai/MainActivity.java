package com.example.tictactoeai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

    private final Button[][] buttons = new Button[3][3];
    private GameLogic gameLogic;
    private View menuView;
    private View gameView;
    private int currentDifficulty = 0; // 0 = Easy, 1 = Hard, 2 = Expert
    private Button buttonEasy, buttonHard, buttonExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameLogic = new GameLogic();
        setupViews();
        setupMenuButtons();
    }

    private void setupViews() {
        // Inflate menu and game views
        menuView = getLayoutInflater().inflate(R.layout.menu_layout, findViewById(android.R.id.content), false);
        gameView = findViewById(R.id.gameContainer);

        // Set initial visibility
        menuView.setVisibility(View.VISIBLE);
        gameView.setVisibility(View.GONE);

        // Add menu to layout
        setContentView(menuView);
    }

    private void setupMenuButtons() {
        // Initialize buttons
        buttonEasy = menuView.findViewById(R.id.buttonEasy);
        buttonHard = menuView.findViewById(R.id.buttonHard);
        buttonExpert = menuView.findViewById(R.id.buttonExpert);
        Button buttonStartGame = menuView.findViewById(R.id.buttonStartGame);

        // Set default selection
        updateDifficultySelection();

        // Set click listeners
        buttonEasy.setOnClickListener(v -> {
            currentDifficulty = 0;
            updateDifficultySelection();
        });

        buttonHard.setOnClickListener(v -> {
            currentDifficulty = 1;
            updateDifficultySelection();
        });

        buttonExpert.setOnClickListener(v -> {
            currentDifficulty = 2;
            updateDifficultySelection();
        });

        buttonStartGame.setOnClickListener(v -> startGame());
    }

    private void updateDifficultySelection() {
        // Reset all buttons to default style
        buttonEasy.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        buttonHard.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        buttonExpert.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));

        // Highlight selected button
        switch (currentDifficulty) {
            case 0:
                buttonEasy.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
                break;
            case 1:
                buttonHard.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
                break;
            case 2:
                buttonExpert.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
                break;
        }
    }

    private void startGame() {
        // Switch to game view
        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameContainer);

        // Initialize game grid
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int index = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = (Button) gridLayout.getChildAt(index);
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> playerMove(row, col));
                index++;
            }
        }

        // Setup restart button
        Button restartButton = findViewById(R.id.buttonRestart);
        restartButton.setOnClickListener(v -> {
            gameLogic = new GameLogic();
            setupViews();
            setupMenuButtons();
        });
    }

    private void playerMove(int row, int col) {
        if (buttons[row][col].getText().toString().isEmpty() && !gameLogic.isGameOver()) {
            buttons[row][col].setText("O");
            gameLogic.updateGameState(row, col);

            if (gameLogic.isGameOver()) {
                showGameResult();
            } else {
                // Choose AI move based on difficulty
                switch (currentDifficulty) {
                    case 0:
                        aiMoveRandom();
                        break;
                    case 1:
                        aiMoveMiniMax();
                        break;
                    case 2:
                        aiMoveAlphaBeta();
                        break;
                }
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
            buttons[row][col].setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
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

    private void aiMoveAlphaBeta() {
        new Handler().postDelayed(() -> {
            Pair<Integer, Integer> move = gameLogic.getBestMoveAlphaBeta();

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

