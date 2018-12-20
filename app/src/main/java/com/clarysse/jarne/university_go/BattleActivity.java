package com.clarysse.jarne.university_go;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

public class BattleActivity extends AppCompatActivity implements SwitchUnimonDialogFragment.SwitchDialogListener, NickNameDialogFragment.NickNameDialogListener {

    private TextView foelevel;
    private TextView userlevel;
    private TextView nicknamefoe;
    private ProgressBar progressBarfoe;
    private int maxhpfoe;
    private int maxhpuser;
    private int currenthpuser;
    private int currenthpfoe;
    private Event usermonEvent;
    private Event foemonEvent;
    private Unimon usermon;
    private Unimon foemon;
    private TextView nicknameuser;
    private ProgressBar progressBarUser;
    private TextView hpvalue;
    private static final String DATABASE_NAME = "movies_db";
    private UnimonDatabase unimonDatabase;
    private List<Move> moves;
    private Button move1;
    private Button move2;
    private Button move3;
    private Button move4;
    private ArrayList<Move> userMoves = new ArrayList<>();
    private ArrayList<Move> foeMoves = new ArrayList<>();
    private ImageView foeImage;
    private ImageView userImage;
    private boolean defenseup = false;
    private Button catchbutton;
    private Button switchbutton;
    private List<Unimon> unimonList;
    private List<Event> eventList;
    private Button runbutton;
    private Unimon defeatedUsermon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        Intent intent = getIntent();
        String encounterstring = intent.getStringExtra("encounter");
        Encounter encounter = new Gson().fromJson(encounterstring, Encounter.class);
        unimonDatabase = Room.databaseBuilder(getApplicationContext(), UnimonDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        nicknamefoe = findViewById(R.id.nicknamefoe);
        foemon = encounter.getUnimon();
        foemonEvent = encounter.getEvent();
        nicknamefoe.setText(foemonEvent.getNaam());
        progressBarfoe = findViewById(R.id.progressbarfoe);
        progressBarUser = findViewById(R.id.progressbaruser);
        hpvalue = findViewById(R.id.hpvalue);
        maxhpfoe = foemonEvent.getBase_health() * encounter.getUnimon().getLevel() / 50;
        currenthpfoe = maxhpfoe;
        updateHp(maxhpfoe, true);
        userlevel = findViewById(R.id.userlevel);
        foelevel = findViewById(R.id.foelevel);
        foelevel.setText("lv:" + foemon.getLevel());
        foeImage = findViewById(R.id.foeImage);
        userImage = findViewById(R.id.userImage);
        runbutton = findViewById(R.id.leavebutton);
        runbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nicknameuser = findViewById(R.id.nicknameuser);
        move1 = findViewById(R.id.move1);
        move2 = findViewById(R.id.move2);
        move3 = findViewById(R.id.move3);
        move4 = findViewById(R.id.move4);
        new getUnimon().execute();

        catchbutton = findViewById(R.id.catchbutton);
        catchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double hpprogresdouble = ((double) currenthpfoe / (double) maxhpfoe);
                int catchrate = (int) Math.round(hpprogresdouble * 100);
                Random rand = new Random();

                int catchscore = rand.nextInt(catchrate);
                Log.e("catch", catchrate + " " + catchscore);
                if (catchscore < 10) {
                    catchToast("You Caught it");
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                                startNicknamedialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                    new PutUnimonTask().execute();
                    Log.e("catch", "unimon caught");
                } else {
                    catchToast("Shoot I missed");
                    Log.e("catch", "darn i missed");
                    enemyturn();
                }
            }
        });
        switchbutton = findViewById(R.id.switchbutton);
        switchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSwitchdialog(0);
            }
        });

    }

    public void catchToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


    }

    public void startNicknamedialog() {
        DialogFragment newFragment = new NickNameDialogFragment();
        newFragment.show(getSupportFragmentManager(), "Nickname");
    }



    @Override
    public void switchUser(Unimon unimon, Event event, int faint) {
        int maximumhpuser = event.getBase_health()*unimon.getLevel()/50;
        usermon = unimon;
        usermonEvent = event;
        currenthpuser = maximumhpuser;
        maxhpuser = maximumhpuser;

        updateHp(maximumhpuser, false);
        nicknameuser.setText(unimon.getNickname());
        hpvalue.setText(maximumhpuser + "/" + maximumhpuser);
        userlevel.setText("lv:" + unimon.getLevel());
        setmoves();
        setClicklistener(1);
        setClicklistener(2);
        setClicklistener(3);
        setClicklistener(4);
        if (faint == 0) {
            enemyturn();
        }
    }

    @Override
    public void run() {
        finish();
    }

    @Override
    public void applyNickname(String nickname) {
        new UpdateUnimonTask().execute(nickname);
        finish();
    }

    private class getUnimon extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            Random random = new Random();
            int id = random.nextInt(unimonDatabase.daoAcces().unimonRowCount(1)) + 1;
            String real_id = 1 + "-" + id;
            usermon = unimonDatabase.daoAcces().getUnimonByRealId(real_id);
            usermonEvent = unimonDatabase.daoAcces().getEventById(usermon.getEventid());
            moves = unimonDatabase.daoAcces().getMoves();
            Log.e("getunimon", moves.get(0).getNaam());
            maxhpuser = usermonEvent.getBase_health() * usermon.getLevel() / 50;
            currenthpuser = maxhpuser;
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            switchUser(usermon,usermonEvent,-1);
        }
    }

    @SuppressLint("NewApi")
    private void updateHp(int currenthp, boolean foe) {
        if (foe) {

            double hpprogresdouble = ((double) currenthp / (double) maxhpfoe);
            int hpprogres = (int) Math.round(hpprogresdouble * 100);

            progressBarfoe.setProgress(hpprogres, true);
        } else {
            double hpprogresdouble = ((double) currenthp / (double) maxhpuser);
            int hpprogres = (int) Math.round(hpprogresdouble * 100);
            hpvalue.setText(currenthp+"/"+maxhpuser);
            progressBarUser.setProgress(hpprogres, true);
        }

    }

    private void setmoves() {
        String[] movesString = usermonEvent.getMoveset().split("-");
        //instellen moves voor usermon
        Move move = moves.get(Integer.parseInt(movesString[0]) - 1);
        userMoves.add(move);
        move1.setText(move.getNaam());


        move = moves.get(Integer.parseInt(movesString[1]) - 1);
        userMoves.add(move);
        move2.setText(move.getNaam());

        move = moves.get(Integer.parseInt(movesString[2]) - 1);
        userMoves.add(move);
        move3.setText(move.getNaam());

        move = moves.get(Integer.parseInt(movesString[3]) - 1);
        userMoves.add(move);
        move4.setText(move.getNaam());

        movesString = foemonEvent.getMoveset().split("-");
        //voor foe ook moves instellen
        move = moves.get(Integer.parseInt(movesString[0]) - 1);
        foeMoves.add(move);
        move = moves.get(Integer.parseInt(movesString[1]) - 1);
        foeMoves.add(move);
        move = moves.get(Integer.parseInt(movesString[2]) - 1);
        foeMoves.add(move);
        move = moves.get(Integer.parseInt(movesString[3]) - 1);
        foeMoves.add(move);

    }

    public void setClicklistener(int move) {
        if (move == 1) {
            move1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("move1", "move pressed");
                    Move move = userMoves.get(0);
                    doMove(move);
                }
            });
        } else if (move == 2) {
            move2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("move2", "move pressed");
                    Move move = userMoves.get(1);
                    doMove(move);
                }
            });
        } else if (move == 3) {
            move3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("move3", "move pressed");
                    Move move = userMoves.get(2);
                    doMove(move);
                }
            });
        } else if (move == 4) {
            move4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("move4", "move pressed");
                    Move move = userMoves.get(3);
                    doMove(move);
                }
            });
        }
    }

    public void doMove(Move move) {
        Toast.makeText(this, usermon.getNickname()+" did "+ move.getNaam(), Toast.LENGTH_SHORT).show();
        int damage = damageCalc(move, usermon, foemon);
        currenthpfoe = currenthpfoe - damage;
        Log.e("move4", "" + damage + " hp " + currenthpfoe + " of " + maxhpfoe);

        if (move.getSpecial_effect().equals("Dx2")) {
            Toast.makeText(this, "Your Defense Up", Toast.LENGTH_SHORT).show();
            defenseup = true;
        }
        if (currenthpfoe < 0) {
            currenthpfoe = 0;
            updateHp(currenthpfoe, true);
            doVictory();
        } else {
            updateHp(currenthpfoe, true);
            enemyturn();

        }

    }

    public int damageCalc(Move move, Unimon attacker, Unimon defender) {
        int damagedone = move.getBase_damage() * attacker.getLevel() / (defender.getLevel());
        if (doubledamage(move)) {
            damagedone = damagedone * 2;
            Toast.makeText(this, "Attack did double damage", Toast.LENGTH_SHORT).show();
        }
        if (defenseup) {
            damagedone = damagedone / 2;
            defenseup = false;
        }
        return damagedone;
    }

    public boolean doubledamage(Move move) {
        if (move.getTags() != null) {
            if (move.getTags().equals(foemonEvent.getTags())) {
                return true;
            }
        }
        return false;

    }

    public void doVictory() {
        Toast.makeText(this, "You Won!", Toast.LENGTH_SHORT).show();
        int expReceived = 20 * foemon.getLevel();
        int expleft = usermon.getExp() - expReceived;
        checklevel(expleft);
        Log.e("victory", "you won");
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(6000); // As I am using LENGTH_LONG in Toast
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    public void doDefeat(){
        Toast.makeText(this, "Unimon Defeated", Toast.LENGTH_SHORT).show();
        defeatedUsermon = usermon;
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(6000); // As I am using LENGTH_LONG in Toast
                    startSwitchdialog(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void checklevel(int expleft) {
        if (expleft <= 0) {
            int level = usermon.getLevel();
            if (level < 100) {
                int nextLevel = level +1;
                Toast.makeText(this, "Level up, your level is now "+nextLevel, Toast.LENGTH_SHORT).show();
                usermon.setLevel(nextLevel);
                int exp = (int) Math.round(20 * Math.pow(1.5, level));
                checklevel(exp + expleft);
            }
        } else {
            usermon.setExp(expleft);
            Toast.makeText(this, "exp needed for next level "+expleft, Toast.LENGTH_SHORT).show();
            new UpdateUsermon().execute();
        }
    }

    public void enemyturn() {
        Random random = new Random();
        Move move = foeMoves.get(random.nextInt(foeMoves.size()));
        Toast.makeText(this, "Foe does "+move.getNaam(), Toast.LENGTH_SHORT).show();
        int damage = damageCalc(move, foemon, usermon);
        currenthpuser = currenthpuser - damage;
        Log.e("move4", "" + damage + " hp " + currenthpuser + " of " + maxhpuser);

        if (move.getSpecial_effect().equals("Dx2")) {
            defenseup = true;
            Toast.makeText(this, "Foe's Defense Up", Toast.LENGTH_SHORT).show();
        }
        if (currenthpuser < 0) {
            currenthpuser = 0;
            updateHp(currenthpuser, false);
            doDefeat();
        } else {
            updateHp(currenthpuser, false);
        }
    }


    public void startSwitchdialog(int faint){
        new GetUnimonsTask().execute(faint+"");

    }

    public class GetUnimonsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            unimonList = unimonDatabase.daoAcces().getOwnUnimons(1);
            Unimon toRemove = new Unimon();
            for (Unimon uni : unimonList) {
                if (uni.getReal_id().equals(defeatedUsermon.getReal_id())) {
                    toRemove = uni;
                }
            }

            unimonList.remove(toRemove);
            eventList = unimonDatabase.daoAcces().getEvents();
            Log.e("bl",""+ unimonList.size()+" "+eventList.size());
            return Integer.parseInt(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            System.out.println(unimonList == null);
            DialogFragment newFragment = SwitchUnimonDialogFragment.newInstance(unimonList,eventList,integer);
            newFragment.show(getSupportFragmentManager(), "switch");
        }
    }

    public class UpdateUnimonTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {

            foemon.setNickname(params[0]);
            Log.e("updateUni", foemon.getNickname());

            unimonDatabase.daoAcces().updateUnimon(foemon);
            return 0;
        }
    }

    public class PutUnimonTask extends AsyncTask<String, Void, Integer> {



        @Override
        protected Integer doInBackground(String... strings) {
            int id = unimonDatabase.daoAcces().unimonRowCount(foemon.getOwnerid())+1;
            String real_id = foemon.getOwnerid() + "-" +id;
            foemon.setReal_id(real_id);
            foemon.setUnimonid((int) unimonDatabase.daoAcces().insertUnimon(foemon));
            return 0;
        }
    }

    public class UpdateUsermon extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            System.out.println(usermon.getUnimonid());
            unimonDatabase.daoAcces().updateUnimon(usermon);
            return 0;
        }
    }


}
