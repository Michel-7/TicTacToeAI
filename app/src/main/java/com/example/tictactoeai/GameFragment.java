package com.example.tictactoeai;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Random;

public class GameFragment extends Fragment {

    private Button[][] buttons = new Button[3][3];
    private GameLogic gameLogic;
    private int difficulty;

    public static GameFragment newInstance(int difficulty) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt("difficulty", difficulty);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false); // Using same layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        difficulty = getArguments().getInt("difficulty", 0);
        gameLogic = new GameLogic();

        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
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

        Button restartButton = view.findViewById(R.id.buttonRestart);
        restartButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new MenuFragment())
                    .commit();
        });
    }

    private void playerMove(int row, int col) {
        if (buttons[row][col].getText().toString().isEmpty() && !gameLogic.isGameOver()) {
            buttons[row][col].setText("O");
            gameLogic.updateGameState(row, col);

            if (gameLogic.isGameOver()) {
                showGameResult();
            } else {
                switch (difficulty) {
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
            Toast.makeText(getContext(), "Draw!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Winner: " + results.winner, Toast.LENGTH_LONG).show();
            highlightWinningCombination(results.winningCombination);
        }
    }

    private void highlightWinningCombination(List<Pair<Integer, Integer>> winningCombination) {
        for (Pair<Integer, Integer> position : winningCombination) {
            int row = position.first;
            int col = position.second;
            buttons[row][col].setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
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
