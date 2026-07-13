package com.vypeensoft.reversi.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import com.vypeensoft.reversi.R;
import com.vypeensoft.reversi.storage.SettingsManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsManager settings = SettingsManager.getInstance();

        Spinner spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        String[] difficulties = new String[]{"EASY", "MEDIUM", "HARD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
        
        for(int i=0; i<difficulties.length; i++) {
            if(difficulties[i].equals(settings.difficulty)) {
                spinnerDifficulty.setSelection(i);
                break;
            }
        }
        
        spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                settings.difficulty = difficulties[position];
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

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
