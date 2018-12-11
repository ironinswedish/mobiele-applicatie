package com.clarysse.jarne.university_go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    private Switch characterSwitch;
    private ImageView characterimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        characterSwitch = findViewById(R.id.settingscharacterswitch);
        characterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    characterimage.setImageResource(R.drawable.female_sprite);
                } else {
                    characterimage.setImageResource(R.drawable.male_sprite);
                }
            }
        });


    }
}
