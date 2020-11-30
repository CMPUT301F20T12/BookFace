package com.example.bookface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

/**
 * Displays the user profile of the presented username
 */
public class UserProfileFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        TextView userName = view.findViewById(R.id.userame_heading);
        TextView userEmail = view.findViewById(R.id.user_email);
        TextView userContact = view.findViewById(R.id.user_contact);

        Bundle bundle = getArguments();
        if(bundle != null){
            String ownerName = getArguments().getString("USERNAME");
            String email = getArguments().getString("USER_EMAIL");
            String contact = getArguments().getString("USER_CONTACT");

            userName.setText(ownerName);
            userEmail.setText(email);
            userContact.setText(contact);


        }
        return  view;
    }
}
