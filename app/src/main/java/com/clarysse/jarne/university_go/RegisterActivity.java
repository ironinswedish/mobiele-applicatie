package com.clarysse.jarne.university_go;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class RegisterActivity extends AppCompatActivity {

    private Switch characterSwitch;
    private ImageView characterImage;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        characterImage = findViewById(R.id.character);
        characterSwitch = findViewById(R.id.characterswitch);
        characterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    characterImage.setImageResource(R.drawable.female_sprite);
                } else {
                    characterImage.setImageResource(R.drawable.male_sprite);
                }
            }
        });
        final Intent intent = new Intent(this, MainMenuActivity.class);
        registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}
