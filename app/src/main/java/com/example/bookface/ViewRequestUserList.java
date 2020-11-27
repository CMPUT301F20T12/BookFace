package com.example.bookface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ViewRequestUserList extends ArrayAdapter<User> {

    private ArrayList<DocumentReference> requesterList;
    private Context context;

    public ViewRequestUserList(Context context, ArrayList<DocumentReference> requesterList){
        super(context,0, requesterList);
        this.requesterList = requesterList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.view_request_user, parent,false);
        }

        final User requester = requesterList.get(position);

        TextView requesterView = view.findViewById(R.id.requesterName);

        requesterView.setText(requester.getUsername());

        return view;
    }

    public int getCount() {
        return requesterList.size();
    }

}

