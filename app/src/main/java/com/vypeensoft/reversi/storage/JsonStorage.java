package com.vypeensoft.reversi.storage;
import android.os.Environment;
import com.google.gson.Gson;
import com.vypeensoft.reversi.model.GameState;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
public class JsonStorage {
    public static void saveGame(GameState state) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "Vypeensoft/Reversi");
            dir.mkdirs();
            File file = new File(dir, "savegame.json");
            FileWriter writer = new FileWriter(file);
            new Gson().toJson(state, writer);
            writer.close();
        } catch(Exception ignored) {}
    }
    public static GameState loadGame() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Vypeensoft/Reversi/savegame.json");
            if(file.exists()) return new Gson().fromJson(new FileReader(file), GameState.class);
        } catch(Exception ignored) {}
        return null;
    }
}
