package com.example.tictactoeai;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements MenuFragment.MenuListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMenuFragment();
    }

    private void showMenuFragment() {
        replaceFragment(new MenuFragment());
    }

    @Override
    public void onStartGame(int difficulty) {
        replaceFragment(GameFragment.newInstance(difficulty));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }
}
