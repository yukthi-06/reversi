package com.vypeensoft.reversi.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.vypeensoft.reversi.R;
import com.vypeensoft.reversi.storage.SettingsManager;

public class ModeSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);

        findViewById(R.id.btnHumanVsHuman).setOnClickListener(v -> startGame(false));
        findViewById(R.id.btnHumanVsComputer).setOnClickListener(v -> startGame(true));
    }

    private void startGame(boolean vsComputer) {
        SettingsManager settings = SettingsManager.getInstance();
        settings.aiEnabled = vsComputer;
        settings.save();
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }
}
