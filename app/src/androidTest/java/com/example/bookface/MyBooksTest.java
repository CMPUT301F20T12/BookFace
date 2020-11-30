package com.example.bookface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Test class for UserProfileActivity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class MyBooksTest{
    private Solo solo;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MyBooks> rule =
            new ActivityTestRule<>(MyBooks.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    // checks if user redirected to AddEditBookActivity on clicking ADD
    @Test
    public void addButtonRedirect(){
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong activity", MyBooks.class);

        TextView addButton = (TextView) solo.getView("add_book");
        solo.clickOnView(addButton);
        assertTrue(solo.waitForActivity(AddEditBookActivity.class,2000));
    }


    // checks if user number of owned books of current user equal to the number of books displayed
    @Test
    public void checkBookList() throws Exception {
        solo.sleep(2000);
        final RecyclerView myBookList = (RecyclerView) solo.getView(R.id.recycle_view);
        final int numBooks = myBookList.getAdapter().getItemCount();
        String username = firebaseAuth.getCurrentUser().getDisplayName();
        db.collection("users").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map userData = document.getData();
                        if (userData != null) {
                            ArrayList<DocumentReference> userBookRefList = (ArrayList<DocumentReference>) userData.get("booksOwned");
                            assertEquals ( userBookRefList.size() , numBooks);
                        }
                    }
                }
            }
        });
    }


}