package com.example.bookface;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Test class for UserProfileActivity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest{
    private Solo solo;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Rule
    public ActivityTestRule<UserProfileActivity> rule =
            new ActivityTestRule<>(UserProfileActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    // checks if correct username is displayed
    @Test
    public void wrongUsername(){
        solo .sleep (1000);
        solo.assertCurrentActivity("Wrong activity", UserProfileActivity.class);
        TextView view = null ;

        String correctUsername = firebaseAuth.getCurrentUser().getDisplayName();
        view = (TextView) solo .getView ( "user_name" );
        assertEquals ( correctUsername , view.getText());
    }

    // checks if correct user email is displayed
    @Test
    public void wrongEmail(){
        solo .sleep (1000);
        solo.assertCurrentActivity("Wrong activity", UserProfileActivity.class);
        TextView view = null ;

        String correctEmail = firebaseAuth.getCurrentUser().getEmail();
        view = (TextView) solo .getView ( "user_email" );
        assertEquals ( correctEmail , view.getText());
    }

    // checks if correct user contact is displayed
    @Test
    public void wrongContact(){
        solo .sleep (1000);
        solo.assertCurrentActivity("Wrong activity", UserProfileActivity.class);

        String username = firebaseAuth.getCurrentUser().getDisplayName();
        db.collection("users").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map userData = document.getData();
                        if (userData != null) {
                            String correctContact = userData.get("contactNo").toString();
                            TextView view = (TextView) solo .getView ( "user_contact" );
                            assertEquals ( correctContact , view.getText());

                        }
                    }
                }
            }
        });
    }

    @Test
    public void checkEditButtonRedirect(){
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong activity", UserProfileActivity.class);

        TextView editButton = (TextView) solo.getView("edit_profile");
        solo.clickOnView(editButton);
        assertTrue(solo.waitForFragmentByTag("Edit_Profile"));
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
    }
}