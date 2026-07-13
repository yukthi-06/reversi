package com.vypeensoft.reversi.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.vypeensoft.reversi.R;
import com.vypeensoft.reversi.storage.SettingsManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsManager settings = SettingsManager.getInstance();

        Switch switch3dCoins = findViewById(R.id.switch3dCoins);
        switch3dCoins.setChecked(settings.use3dCoins);
        switch3dCoins.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settings.use3dCoins = isChecked;
        });

        Button btnSave = findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(v -> {
            settings.save();
            finish();
        });
    }
}
