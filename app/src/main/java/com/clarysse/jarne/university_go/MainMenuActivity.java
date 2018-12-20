package com.clarysse.jarne.university_go;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainMenuActivity extends AppCompatActivity {

    private Button battleButton;
    private Button teamButton;
    private Button settingsButton;
    private Button mapButton;
    private ImageView player_character;
    private Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        final Intent intent = new Intent(this, BattleActivity.class);
        battleButton = findViewById(R.id.battlebutton);
        battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        final Intent intent2 = new Intent(this, TeamActivity.class);
        teamButton = findViewById(R.id.teambutton);
        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });
        final Intent intent3 = new Intent(this, SettingActivity.class);
        final Intent intent4 = new Intent(this, MapsActivity.class);
        mapButton = findViewById(R.id.mapbutton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent4);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        logoutButton = findViewById(R.id.battlebutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.battlebutton:
                        signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                }
            }
        });
        player_character = findViewById(R.id.imageView);
        SharedPreferences sp;
        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sprite = sp.getString("sprite", "");
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("1", R.drawable.unimon_2);
        editor.putInt("2", R.drawable.unimon_1);
        editor.putInt("3", R.drawable.unimon_3);
        editor.putInt("4", R.drawable.unimon_4);
        editor.putInt("5", R.drawable.unimon_5);
        editor.commit();
        Log.e("mainmenu", "sprite"+sprite);
        if(sprite.equals("male1")) {
            player_character.setImageResource(R.drawable.male1);
        } else if (sprite.equals("male2")) {
            player_character.setImageResource(R.drawable.male2);
        } else if (sprite.equals("female1")) {
            player_character.setImageResource(R.drawable.female1);
        } else if (sprite.equals("female2")) {
            player_character.setImageResource(R.drawable.female2);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
