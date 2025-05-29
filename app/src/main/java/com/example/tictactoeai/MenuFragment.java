// MenuFragment.java
package com.example.tictactoeai;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {

    private int selectedDifficulty = 0;
    private Button buttonEasy, buttonHard, buttonExpert;
    private MenuListener listener;

    public interface MenuListener {
        void onStartGame(int difficulty);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MenuListener) {
            listener = (MenuListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MenuListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buttonEasy = view.findViewById(R.id.buttonEasy);
        buttonHard = view.findViewById(R.id.buttonHard);
        buttonExpert = view.findViewById(R.id.buttonExpert);
        Button buttonStart = view.findViewById(R.id.buttonStartGame);

        updateDifficultySelection();

        buttonEasy.setOnClickListener(v -> {
            selectedDifficulty = 0;
            updateDifficultySelection();
        });

        buttonHard.setOnClickListener(v -> {
            selectedDifficulty = 1;
            updateDifficultySelection();
        });

        buttonExpert.setOnClickListener(v -> {
            selectedDifficulty = 2;
            updateDifficultySelection();
        });

        buttonStart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStartGame(selectedDifficulty);
            }
        });
    }

    private void updateDifficultySelection() {
        int defaultColor = getResources().getColor(android.R.color.darker_gray);
        int selectedColor = getResources().getColor(android.R.color.holo_blue_light);

        buttonEasy.setBackgroundColor(defaultColor);
        buttonHard.setBackgroundColor(defaultColor);
        buttonExpert.setBackgroundColor(defaultColor);

        switch (selectedDifficulty) {
            case 0:
                buttonEasy.setBackgroundColor(selectedColor);
                break;
            case 1:
                buttonHard.setBackgroundColor(selectedColor);
                break;
            case 2:
                buttonExpert.setBackgroundColor(selectedColor);
                break;
        }
    }
}
