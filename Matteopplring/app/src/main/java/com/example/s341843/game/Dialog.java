package com.example.s341843.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.s341843.R;

public class Dialog extends DialogFragment {

    private DialogClickListener callback;


    public interface DialogClickListener{
        public void onYesClick();
        public void onNoClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            callback = (DialogClickListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException("Mangler DialogClickListener interfacet");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.avslutt_title).setMessage(R.string.avslutt_desc).setPositiveButton(R.string.avslutt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onYesClick();
                    }
                }).setNegativeButton(R.string.avbryt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onNoClick();
                    }
                }).create();
    }
}
