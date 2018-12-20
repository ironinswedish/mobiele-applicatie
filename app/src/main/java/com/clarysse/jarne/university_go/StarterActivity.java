package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class StarterActivity extends AppCompatActivity implements NickNameDialogFragment.NickNameDialogListener {

    Button teacherstarter;
    Button studentstarter;
    Button utilitystarter;
    private Unimon starter;
    private static final String DATABASE_NAME = "movies_db";
    private UnimonDatabase unimonDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        unimonDatabase = Room.databaseBuilder(getApplicationContext(), UnimonDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        teacherstarter = findViewById(R.id.teacherstarter);
        studentstarter = findViewById(R.id.studentstarter);
        utilitystarter = findViewById(R.id.utillitystarter);
        teacherstarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ownerid = 1;
                starter = new Unimon();
                starter.setLevel(5);
                starter.setExp((int) Math.round(20 * Math.pow(1.5, 5)));
                starter.setOwnerid(ownerid);
                starter.setEventid(5);//5
                startNicknamedialog();
                new PutUnimonTask().execute();

            }
        });
        studentstarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ownerid = 1;
                starter = new Unimon();
                starter.setLevel(5);
                starter.setExp((int) Math.round(20 * Math.pow(1.5, 5)));

                starter.setOwnerid(ownerid);
                starter.setEventid(3);//3
                startNicknamedialog();
                new PutUnimonTask().execute();

            }
        });
        utilitystarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ownerid = 1;
                starter = new Unimon();
                starter.setLevel(5);
                starter.setExp((int) Math.round(20 * Math.pow(1.5, 5)));
                starter.setOwnerid(ownerid);
                starter.setEventid(4);//4

                startNicknamedialog();
                Log.e("create unimon", "not wait until dialog finish");
                new PutUnimonTask().execute();

            }
        });

    }

    @Override
    public void applyNickname(String nickname) {
        new UpdateUnimonTask().execute(nickname);
        Log.e("dialog entered", "nickname is " + nickname);
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public class PutUnimonTask extends AsyncTask<String, Void, Integer> {



        @Override
        protected Integer doInBackground(String... strings) {
            int id = unimonDatabase.daoAcces().unimonRowCount(starter.getOwnerid())+1;
            String real_id = starter.getOwnerid() + "-" +id;
            starter.setReal_id(real_id);
            Event event = unimonDatabase.daoAcces().getEventById(starter.getEventid());
            starter.setNickname(event.getNaam());

            starter.setUnimonid((int) unimonDatabase.daoAcces().insertUnimon(starter));
            return 0;
        }
    }

    public void startNicknamedialog(){
        DialogFragment newFragment = new NickNameDialogFragment();
        newFragment.show(getSupportFragmentManager(), "Nickname");
    }

    public class UpdateUnimonTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {

            starter.setNickname(params[0]);
            Log.e("updateUni", starter.getNickname());

            unimonDatabase.daoAcces().updateUnimon(starter);
            return 0;
        }
    }



}


