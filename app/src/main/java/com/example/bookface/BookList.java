package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class BookList extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context context;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    public BookList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }

    /**
     * The method to do the searching
     * @param searchTerm - the term entered to search bar
     * @return arraylist - an array list of searched books
     */
    public ArrayList<Book> searchForBooks(String searchTerm) {
        searchTerm = searchTerm.toLowerCase(Locale.getDefault());
        ArrayList<Book> arraylist = new ArrayList<Book>();
        if (searchTerm.length() == 0) {
            arraylist.addAll(books);
        } else {
            for (Book book : books) {
                if (book.getTitle().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getAuthor().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getISBN().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getDescription().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getOwnerUsername().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getBorrowerUsername().toLowerCase(Locale.getDefault()).contains(searchTerm)) { // search through the fields of a book
                    arraylist.add(book);
                }
            }
        }
        notifyDataSetChanged();
        return arraylist;
    }

//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        animalNamesList.clear();
//        if (charText.length() == 0) {
//            animalNamesList.addAll(arraylist);
//        } else {
//            for (AnimalNames wp : arraylist) {
//                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    animalNamesList.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;


        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book, parent,false);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();
        if (userInstance != null){
            String userName = userInstance.getDisplayName();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docPath = "users/"+userName;
            DocumentReference docRef = db.document(docPath);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                private static final String TAG = "BookListMessage";

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            Map userData = document.getData();
//                            DocumentSnapshot document = task.getResult();
//                            if(userData != null){
                            List<String> booksOwned = (List<String>) document.get("booksOwned");
                            String bookISBN = booksOwned.get(position);
//                            TextView isbn = findViewById();
//                            isbn.setText(bookISBN);
                            Log.d(TAG, "onComplete: "+bookISBN);

//                                TextView nameView = findViewById(R.id.user_name);
//                                TextView emailView = findViewById(R.id.user_email);
//                                TextView contactView = findViewById(R.id.user_contact);
//
//                                nameView.setText(username);
//                                emailView.setText(email);
//                                contactView.setText(contact);

                            }
                        else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        return view;
    }
}
