package com.oslomet.s341843.fragmenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialog extends DialogFragment {
    private String tittel;
    private String beskrivelse;
    private String positiv;
    private String negativ;

    private DialogClickListener callback;


    public AlertDialog(String tittel, String positiv, String negativ){
        this.tittel = tittel;
        this.beskrivelse = "";
        this.positiv = positiv;
        this.negativ = negativ;
    }
    public AlertDialog(String tittel, String beskrivelse, String positiv, String negativ){
        this.tittel = tittel;
        this.beskrivelse = beskrivelse;
        this.positiv = positiv;
        this.negativ = negativ;
    }

    public interface DialogClickListener{
        public void onYesClick();
        public void onNoClick();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            callback = (DialogClickListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException("Mangler DialogClickListener interface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(beskrivelse.equals("")){
            return new android.app.AlertDialog.Builder(getActivity()).setTitle(tittel).setPositiveButton(positiv, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onYesClick();
                }
            }).setNegativeButton(negativ, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onNoClick();
                }
            }).create();
        }else{
            return new android.app.AlertDialog.Builder(getActivity()).setTitle(tittel).setMessage(beskrivelse).setPositiveButton(positiv, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onYesClick();
                }
            }).setNegativeButton(negativ, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onNoClick();
                }
            }).create();
        }
    }
}
