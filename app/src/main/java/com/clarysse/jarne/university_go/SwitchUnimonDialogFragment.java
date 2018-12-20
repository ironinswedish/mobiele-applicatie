package com.clarysse.jarne.university_go;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SwitchUnimonDialogFragment extends DialogFragment {
    private RecyclerView teamlist;
    private String message = "Switch Unimon";
    private static final String DATABASE_NAME = "movies_db";
    private UnimonDatabase unimonDatabase;
    private List<Unimon> unimonList;
    private List<Event> eventList;
    private SwitchDialogListener listener;

    public static SwitchUnimonDialogFragment newInstance(List<Unimon> unimonlist, List<Event> eventList, int faint) {
        SwitchUnimonDialogFragment dialog = new SwitchUnimonDialogFragment();
        System.out.println(unimonlist == null);;
        Gson gson = new Gson();
        Bundle args = new Bundle();
        Type collectionType = new TypeToken<Collection<Unimon>>(){}.getType();
        Log.e("de", gson.toJson(unimonlist,collectionType));
        args.putString("unimons", gson.toJson(unimonlist,collectionType));
        args.putString("events", gson.toJson(eventList));
        args.putInt("faint",faint);

        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.teamlist, null);
        int faint = -1;
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Unimon>>(){}.getType();
        if (getArguments() != null) {
            unimonList = gson.fromJson(getArguments().getString("unimons", ""),collectionType);
            Log.e("unimonlist", "" + unimonList.size());
            collectionType = new TypeToken<Collection<Event>>(){}.getType();
            eventList = gson.fromJson(getArguments().getString("events", ""), collectionType);
            faint = getArguments().getInt("faint");

            teamlist = view.findViewById(R.id.teamlist2);
            CustomAdapter customAdapter = new CustomAdapter(unimonList,eventList,faint);
            teamlist.setAdapter(customAdapter);
            teamlist.setLayoutManager((new LinearLayoutManager((getContext()))));


        }


        builder.setView(view)
                .setMessage(message);
        if(faint==0) {
            builder.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        } else if(faint == 1){
            builder.setPositiveButton("Run", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.run();
                }
            });

        }
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SwitchUnimonDialogFragment.SwitchDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExempleDialogListener");
        }
    }

    public interface SwitchDialogListener{
        void switchUser(Unimon unimon, Event event,int faint);

        void run();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private List<Unimon> unimonList2;
        private List<Event> eventList2;
        private int faint;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameField;
            public Button switcherButton;

            public MyViewHolder(View itemView) {
                super(itemView);

                nameField = (TextView) itemView.findViewById(R.id.namefield);
                switcherButton = (Button) itemView.findViewById(R.id.switcherbutton);
            }
        }


        public CustomAdapter(List<Unimon> unimons, List<Event> events, int faint) {
            unimonList2 = unimons;
            eventList2 = events;
            this.faint = faint;

        }
        @NonNull
        @Override
        public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View unimonView = inflater.inflate(R.layout.switchentry, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(unimonView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder myViewHolder, int position) {
            Unimon unimon = unimonList2.get(position);
            final int pos = position;

            Button switchunimonButton = myViewHolder.switcherButton;
            TextView nickname = myViewHolder.nameField;
            Log.e("teamlist", ""+unimon.getNickname());
            nickname.setText(unimon.getNickname());
            switchunimonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Unimon unimon = unimonList2.get(pos);
                    Event event = eventList2.get(unimon.getEventid()-1);
                    listener.switchUser(unimon, event,faint);
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return unimonList2.size();
        }


    }

}
