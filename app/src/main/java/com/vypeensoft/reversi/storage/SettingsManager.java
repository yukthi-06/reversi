package com.vypeensoft.reversi.storage;
import android.os.Environment;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
public class SettingsManager {
    public boolean soundEnabled = true;
    public boolean animationsEnabled = true;
    public String difficulty = "MEDIUM";
    public boolean aiEnabled = true;
    public String firstPlayer = "BLACK";
    public String theme = "GREEN";
    public boolean vibrationEnabled = true;
    public boolean boardHighlight = true;
    public int searchDepth = 5;
    public boolean use3dCoins = false;
    private static SettingsManager instance;
    public static SettingsManager getInstance() {
        if(instance == null) instance = new SettingsManager();
        return instance;
    }
    public void load() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "Vypeensoft/Reversi/settings");
            dir.mkdirs();
            File file = new File(dir, "settings.json");
            if(file.exists()) {
                SettingsManager loaded = new Gson().fromJson(new FileReader(file), SettingsManager.class);
                if(loaded != null) {
                    this.soundEnabled = loaded.soundEnabled;
                    this.animationsEnabled = loaded.animationsEnabled;
                    this.difficulty = loaded.difficulty;
                    this.aiEnabled = loaded.aiEnabled;
                    this.firstPlayer = loaded.firstPlayer;
                    this.theme = loaded.theme;
                    this.vibrationEnabled = loaded.vibrationEnabled;
                    this.boardHighlight = loaded.boardHighlight;
                    this.searchDepth = loaded.searchDepth;
                }
            }
        } catch(Exception ignored) {}
    }
    public void save() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "Vypeensoft/Reversi/settings");
            dir.mkdirs();
            File file = new File(dir, "settings.json");
            FileWriter writer = new FileWriter(file);
            new Gson().toJson(this, writer);
            writer.close();
        } catch(Exception ignored) {}
    }
}
