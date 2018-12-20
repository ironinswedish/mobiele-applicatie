package com.clarysse.jarne.university_go;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NickNameDialogFragment extends DialogFragment {
    private String message = "Change nickname of unimon?";
    private EditText nicknameField;
    private NickNameDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.nicknamelayout, null);
        nicknameField = (EditText) view.findViewById(R.id.nicknamefield);
        builder.setView(view)
                .setMessage(message)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nickname = nicknameField.getText().toString();
                        if(!nickname.equals("")) {
                            listener.applyNickname(nickname);
                        }
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NickNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExempleDialogListener");
        }
    }

    public interface NickNameDialogListener{
        void applyNickname(String nickname);
    }
}
