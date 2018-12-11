package com.clarysse.jarne.university_go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UniDexPageActivity extends AppCompatActivity {

    private TextView unidexpagelabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_dex_page);

        double random = Math.random()*50;
        unidexpagelabel = findViewById(R.id.unidexpagelabel);
        unidexpagelabel.setText(String.valueOf(random));

    }
}
