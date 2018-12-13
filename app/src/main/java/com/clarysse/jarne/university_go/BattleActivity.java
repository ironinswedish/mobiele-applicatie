package com.clarysse.jarne.university_go;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class BattleActivity extends AppCompatActivity {

    private TextView nicknamefoe;
    private ProgressBar progressBarfoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        Intent intent = getIntent();
        String encounterstring = intent.getStringExtra("encounter");
        Encounter encounter = new Gson().fromJson(encounterstring,Encounter.class);

        nicknamefoe = findViewById(R.id.nicknamefoe);
        nicknamefoe.setText(encounter.getEvent().getNaam());
        progressBarfoe = findViewById(R.id.progressbarfoe);
        progressBarfoe.setProgress(encounter.getEvent().getBase_health() * encounter.getUnimon().getLevel() / 50);


    }
    
}
