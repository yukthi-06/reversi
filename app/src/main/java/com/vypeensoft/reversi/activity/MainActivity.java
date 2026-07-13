package com.vypeensoft.reversi.activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import com.vypeensoft.reversi.R;
import com.vypeensoft.reversi.storage.SettingsManager;
public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        
        SettingsManager.getInstance().load();
        
        findViewById(R.id.btnNew).setOnClickListener(v -> startActivity(new Intent(this, ModeSelectionActivity.class)));
        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("load", true);
            startActivity(i);
        });
        findViewById(R.id.btnSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
    }
}
