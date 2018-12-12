package com.clarysse.jarne.university_go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    private Switch characterSwitch;
    private ImageView characterimage;
    private Switch raceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        characterimage = findViewById(R.id.settingscharacter);
        raceSwitch = findViewById(R.id.settingsraceswitch2);

        characterSwitch = findViewById(R.id.settingscharacterswitch);
        characterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(raceSwitch.isChecked()){
                        characterimage.setImageResource(R.drawable.female1);

                    }
                    else{
                        characterimage.setImageResource(R.drawable.female2);
                    }
                } else {
                    if(raceSwitch.isChecked()){
                        characterimage.setImageResource(R.drawable.male1);
                    }
                    else{
                        characterimage.setImageResource(R.drawable.male2);
                    }

                }
            }
        });

        raceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(characterSwitch.isChecked()){
                        characterimage.setImageResource(R.drawable.female1);
                    }
                    else{
                        characterimage.setImageResource(R.drawable.male1);
                    }
                } else {
                    if(characterSwitch.isChecked()){
                        characterimage.setImageResource(R.drawable.female2);
                    }
                    else{
                        characterimage.setImageResource(R.drawable.male2);
                    }

                }
            }
        });

    }
}
