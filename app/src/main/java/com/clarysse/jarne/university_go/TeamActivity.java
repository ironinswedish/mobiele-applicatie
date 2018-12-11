package com.clarysse.jarne.university_go;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class TeamActivity extends AppCompatActivity {

    int userTeamSize = 10;
    private ListView teamlist;
    private CustomAdapter customAdapter;
    private UnidexAdapter unidexAdapter;
    private Switch unidexSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamlist = findViewById(R.id.teamlist);

        customAdapter = new CustomAdapter();
        unidexAdapter = new UnidexAdapter();

        unidexSwitch = findViewById(R.id.unidexswitch);
        unidexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    teamlist.setAdapter(unidexAdapter);
                } else {
                    teamlist.setAdapter(customAdapter);
                }
            }
        });
        teamlist.setAdapter(customAdapter);


    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userTeamSize;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.teamentry, null);
            ImageView imageView = convertView.findViewById(R.id.teamsprite);
            /*TextView nickname = convertView.findViewById(R.id.entrynumber);
            TextView level = convertView.findViewById(R.id.realname);
            TextView totalhpvalue = convertView.findViewById(R.id.catchamount);
            TextView type = convertView.findViewById(R.id.type);
            TextView experience = convertView.findViewById(R.id.experience);*/
            final Intent intent = new Intent(convertView.getContext(), UniDexPageActivity.class);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("list", "image clicked");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private class UnidexAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userTeamSize;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.unidexentry, null);
            ImageView imageView = convertView.findViewById(R.id.teamsprite);
            /*TextView nickname = convertView.findViewById(R.id.entrynumber);
            TextView level = convertView.findViewById(R.id.realname);
            TextView totalhpvalue = convertView.findViewById(R.id.catchamount);
            TextView type = convertView.findViewById(R.id.type);
            TextView experience = convertView.findViewById(R.id.experience);*/
            final Intent intent = new Intent(convertView.getContext(), UniDexPageActivity.class);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("list", "image clicked");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}
