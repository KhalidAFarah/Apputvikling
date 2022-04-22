package com.oslomet.s341843;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialog extends DialogFragment {
    private DialogClickListener callback;

    private String title;
    private String txt_positiv;
    private String txt_negativ;
    private String description;

    public AlertDialog(String title, String txt_positiv, String txt_negativ){
        this.title = title;
        this.txt_positiv = txt_positiv;
        this.txt_negativ = txt_negativ;
        this.description = "";
    }
    public AlertDialog(String title, String description, String txt_positiv, String txt_negativ){
        this.title = title;
        this.txt_positiv = txt_positiv;
        this.txt_negativ = txt_negativ;
        this.description = description;
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
            throw new ClassCastException("Mangler DialogClickListener interfacet i aktiviteten");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(!description.equals("")) {
            return new android.app.AlertDialog.Builder(getActivity()).setTitle(title).setMessage(description).setPositiveButton(txt_positiv, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onYesClick();
                }
            }).setNegativeButton(txt_negativ, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onNoClick();
                }
            }).create();
        }else{
            return new android.app.AlertDialog.Builder(getActivity()).setTitle(title).setPositiveButton(txt_positiv, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onYesClick();
                }
            }).setNegativeButton(txt_negativ, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onNoClick();
                }
            }).create();
        }
    }
}
