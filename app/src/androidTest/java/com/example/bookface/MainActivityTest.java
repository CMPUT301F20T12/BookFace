package com.example.bookface;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class MainActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void emptyFieldCheck(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        Button CTButton = (Button) solo.getView("button");
        solo.clickOnView(CTButton);

        // Check for empty email field
        assertTrue(solo.waitForText("Enter email", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "poulomi@ualberta.ca");
        solo.clickOnView(CTButton);

        // Check for empty password field
        assertTrue(solo.waitForText("Enter password", 1, 2000));
    }

    @Test
    public void wrongPassword(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "poulomi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextNumberPassword), "wrongPassword");
        Button CTButton = (Button) solo.getView("button");
        solo.clickOnView(CTButton);

        assertTrue(solo.waitForText("Login Unsuccessful", 1, 2000));
    }

    @Test
    public void wrongEmail(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "poulomii@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextNumberPassword), "12345678");
        Button CTButton = (Button) solo.getView("button");
        solo.clickOnView(CTButton);

        assertTrue(solo.waitForText("Login Unsuccessful", 1, 2000));
    }

    @Test
    public void signupRedirect(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        TextView CTButton = (TextView) solo.getView("textView");
        solo.clickOnView(CTButton);

        assertTrue(solo.waitForActivity(SignupActivity.class, 2000));


    }

    @Test
    public void successfulLogin(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "poulomi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextNumberPassword), "12345678");
        Button CTButton = (Button) solo.getView("button");
        solo.clickOnView(CTButton);


        assertTrue(solo.waitForText("Login Successful", 1, 2000));

        // Sign out after test
        Button CTButton2 = (Button) solo.getView("logout");
        solo.clickOnView(CTButton2);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
