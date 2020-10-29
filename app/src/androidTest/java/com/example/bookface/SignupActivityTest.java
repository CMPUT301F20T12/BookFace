package com.example.bookface;

import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class SignupActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<SignupActivity> rule =
            new ActivityTestRule<>(SignupActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void correctActivity(){
        solo.assertCurrentActivity("Wrong activity", SignupActivity.class);
    }

    @Test
    public void emptyFieldsCheck(){
        solo.assertCurrentActivity("Wrong activity", SignupActivity.class);

        Button CTButton = (Button) solo.getView("button");

        solo.clickOnView(CTButton);
        assertTrue(solo.waitForText("Please fill all fields", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "pganguly.6733@gmail.com");
        solo.clickOnView(CTButton);
        assertTrue(solo.waitForText("Please fill all fields", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.editTextNumberPassword), "12345678");
        solo.clickOnView(CTButton);
        assertTrue(solo.waitForText("Please fill all fields", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.editTextPhone), "9999999999");
        solo.clickOnView(CTButton);
        assertTrue(solo.waitForText("Please fill all fields", 1, 2000));

    }

    @Test
    public void successfulSignup(){
        // Note: Only passes if the email does not exist in users database!

        solo.assertCurrentActivity("Wrong activity", SignupActivity.class);

        Button CTButton = (Button) solo.getView("button");

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "example@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editTextNumberPassword), "12345678");
        solo.enterText((EditText) solo.getView(R.id.editTextPhone), "9999999999");
        solo.enterText((EditText) solo.getView(R.id.username), "exampleUser");

        solo.clickOnView(CTButton);

        assertTrue(solo.waitForText("Login Successful", 1, 2000));

        // Sign out after test
        Button CTButton2 = (Button) solo.getView("signOut");
        solo.clickOnView(CTButton2);
    }

    @Test
    public void existingEmail(){
        solo.assertCurrentActivity("Wrong activity", SignupActivity.class);

        Button CTButton = (Button) solo.getView("button");

        solo.enterText((EditText) solo.getView(R.id.editTextTextEmailAddress), "poulomi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextNumberPassword), "123456789");
        solo.enterText((EditText) solo.getView(R.id.editTextPhone), "9999999999");
        solo.enterText((EditText) solo.getView(R.id.username), "exampleUser");

        solo.clickOnView(CTButton);

        assertTrue(solo.waitForText("SignupActivity failed", 1, 2000));
    }


    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
