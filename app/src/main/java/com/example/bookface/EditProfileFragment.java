package com.example.bookface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This class is used to Edit the profile
 */
public class EditProfileFragment extends DialogFragment {
    // Declare variables
    String contact;
    EditText contactEdit;
    private OnFragmentInteractionListener listener;

    /**
     * This is the constructor
     * @param number
     * This is the contact number of the user
     */
    public EditProfileFragment(String number) { this.contact = number; }

    /**
     * Implement the interface method
     */
    public interface OnFragmentInteractionListener {
        void onEditProfileConfirmPressed(String number);
    }

    /**
     * This method does the input validation for Edit text input in dialog box
     * @param num
     *
     * @return
     *
     */
    public boolean validateInput(String num){
            if(num.contains("+")){
                num = num.substring(2, num.length());
            }
            return (num.length() == 10) && num.matches("\\d+");
    }

    /**
     * Override the onAttach method
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * This method creates a dialog box, sets the current field of User object, accepts input, validates it and updates the User Object.
     * @param savedInstanceState
     * @return
     * The created dialog box
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Get Views
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_fragment_layout, null);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_fragment_layout, null);
        contactEdit = view.findViewById(R.id.contact_editText);
        contactEdit.setText(contact);

        //Create a Dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Change Phone No.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Accept Input
                        String contactNo = contactEdit.getText().toString();

                        if(validateInput(contactNo)){
                            listener.onEditProfileConfirmPressed(contactNo);
                        }
                        else{
                            Toast.makeText(getActivity(), "INVALID PHONE NO.",Toast.LENGTH_LONG).show();
                        }
                    }}).create();
    }
}
