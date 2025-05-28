package com.example.tictactoeai;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private char playerTurn;
    private char[][] gameState;
    private GameResults gameResults;

    public GameLogic()
    {
        playerTurn = 'O';
        gameState = new char[3][3];
    }

    public static final List<List<Pair<Integer, Integer>>> WINNING_COMBINATIONS = List.of(
            List.of(new Pair<>(0, 0), new Pair<>(0, 1), new Pair<>(0, 2)), // Top row
            List.of(new Pair<>(1, 0), new Pair<>(1, 1), new Pair<>(1, 2)), // Middle row
            List.of(new Pair<>(2, 0), new Pair<>(2, 1), new Pair<>(2, 2)), // Bottom row
            List.of(new Pair<>(0, 0), new Pair<>(1, 0), new Pair<>(2, 0)), // Left column
            List.of(new Pair<>(0, 1), new Pair<>(1, 1), new Pair<>(2, 1)), // Middle column
            List.of(new Pair<>(0, 2), new Pair<>(1, 2), new Pair<>(2, 2)), // Right column
            List.of(new Pair<>(0, 0), new Pair<>(1, 1), new Pair<>(2, 2)), // Diagonal
            List.of(new Pair<>(0, 2), new Pair<>(1, 1), new Pair<>(2, 0))  // Diagonal
    );

    private boolean isDraw()
    {
        for(int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if((int)gameState[i][j] == 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean testEndGame() {
        for (List<Pair<Integer, Integer>> combination : WINNING_COMBINATIONS) {
            char char1 = gameState[combination.get(0).first][combination.get(0).second];
            char char2 = gameState[combination.get(1).first][combination.get(1).second];
            char char3 = gameState[combination.get(2).first][combination.get(2).second];

            if ((int)char1 != 0 && char1 == char2 && char1 == char3) {
                gameResults = new GameResults(playerTurn, combination);
                return true;
            }
        }
        if(isDraw())
        {
            gameResults = new GameResults(' ', null);
            return true;
        }
        return false;
    }

    public void updateGameState(int row, int col)
    {
        gameState[row][col] = playerTurn;
        testEndGame();
        playerTurn = (playerTurn == 'X') ? 'O' : 'X';
    }

    public GameResults getGameResults() {
        return gameResults;
    }

    public boolean isGameOver() {
        return gameResults != null;
    }

    public List<Pair<Integer, Integer>> getAvailableMoves() {
        List<Pair<Integer, Integer>> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((int) gameState[i][j] == 0) {
                    availableMoves.add(new Pair<>(i, j));
                }
            }
        }
        return availableMoves;
    }

    private Character checkWinner() {
        for (List<Pair<Integer, Integer>> combination : WINNING_COMBINATIONS) {
            char c1 = gameState[combination.get(0).first][combination.get(0).second];
            char c2 = gameState[combination.get(1).first][combination.get(1).second];
            char c3 = gameState[combination.get(2).first][combination.get(2).second];

            if (c1 != 0 && c1 == c2 && c1 == c3) {
                return c1; // Return 'X' or 'O'
            }
        }

        if (isDraw()) {
            return ' '; // Draw
        }

        return null; // Game is still going
    }

    public Pair<Integer, Integer> getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Pair<Integer, Integer> bestMove = null;

        for (Pair<Integer, Integer> move : getAvailableMoves()) {
            gameState[move.first][move.second] = 'X'; // AI makes move
            int score = minimax(false);
            gameState[move.first][move.second] = 0; // Undo move

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(boolean isMaximizing) {
        Character result = checkWinner();
        if (result != null) {
            if (result == 'X') return 1;
            else if (result == 'O') return -1;
            else return 0; // Draw
        }
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Pair<Integer, Integer> move : getAvailableMoves()) {
            gameState[move.first][move.second] = isMaximizing ? 'X' : 'O';
            int score = minimax(!isMaximizing);
            gameState[move.first][move.second] = 0; // Undo move

            if (isMaximizing) {
                bestScore = Math.max(score, bestScore);
            } else {
                bestScore = Math.min(score, bestScore);
            }
        }

        return bestScore;
    }

    /**
     * Determines the best move for the AI using the Alpha-Beta Pruning algorithm.
     * This is an optimization of the Minimax algorithm.
     *
     * @return A Pair<Integer, Integer> representing the best (row, col) move for the AI.
     */
    public Pair<Integer, Integer> getBestMoveAlphaBeta() {
        int bestScore = Integer.MIN_VALUE;
        Pair<Integer, Integer> bestMove = null;

        // Alpha-beta initial values
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Iterate through all available moves
        for (Pair<Integer, Integer> move : getAvailableMoves()) {
            gameState[move.first][move.second] = 'X'; // Make the AI's hypothetical move ('X' is AI)
            // Call alphabeta for the opponent (minimizing player)
            int score = alphabeta(false, alpha, beta);
            gameState[move.first][move.second] = 0; // Undo the move (backtrack)

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
            alpha = Math.max(alpha, bestScore); // Update alpha for the maximizing player
        }
        return bestMove;
    }

    /**
     * The Alpha-Beta Pruning algorithm implementation.
     *
     * @param isMaximizing True if it's the maximizing player's turn (AI), false if it's the minimizing player's turn (human).
     * @param alpha The alpha value (best score found so far for the maximizing player).
     * @param beta The beta value (best score found so far for the minimizing player).
     * @return The score of the current game state.
     */
    private int alphabeta(boolean isMaximizing, int alpha, int beta) {
        Character result = checkWinner(); // Check if the game has ended
        if (result != null) {
            if (result == 'X') return 1; // AI wins
            else if (result == 'O') return -1; // Human wins
            else return 0; // Draw
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (Pair<Integer, Integer> move : getAvailableMoves()) {
                gameState[move.first][move.second] = 'X'; // Make hypothetical move for AI
                bestScore = Math.max(bestScore, alphabeta(false, alpha, beta)); // Recurse for opponent
                gameState[move.first][move.second] = 0; // Undo the move

                alpha = Math.max(alpha, bestScore); // Update alpha
                if (beta <= alpha) { // Beta cut-off
                    break;
                }
            }
            return bestScore;
        } else { // Minimizing player (human)
            int bestScore = Integer.MAX_VALUE;
            for (Pair<Integer, Integer> move : getAvailableMoves()) {
                gameState[move.first][move.second] = 'O'; // Make hypothetical move for human
                bestScore = Math.min(bestScore, alphabeta(true, alpha, beta)); // Recurse for AI
                gameState[move.first][move.second] = 0; // Undo the move

                beta = Math.min(beta, bestScore); // Update beta
                if (beta <= alpha) { // Alpha cut-off
                    break;
                }
            }
            return bestScore;
        }
    }

    public static class GameResults
    {
        char winner;
        List<Pair<Integer, Integer>> winningCombination;

        public GameResults(char winner, List<Pair<Integer, Integer>> winningCombination)
        {
            this.winner = winner;
            this.winningCombination = winningCombination;
        }
    }

}
