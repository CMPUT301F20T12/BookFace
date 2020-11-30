package com.example.bookface;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BookDescriptionTest {
    // TODO
    // Add Tests

    private Solo solo;

    @Rule
    public ActivityTestRule<BookDescription> rule =
            new ActivityTestRule<>(BookDescription.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkDescriptionActivity() {
        solo.assertCurrentActivity("Wrong activity", BookDescription.class);
    }

    @Test
    public void checkTopButton() {
        solo.clickOnButton("My Books");
//         solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        // TODO
        // get current user and check if that user is owner or not and change the button display accordingly
    }

    @Test
    public void checkEditButton() {
        solo.clickOnButton("Edit");
        // solo.assertCurrentActivity("Wrong Activity", AddEditBook.class);

        // TODO
        // get current user and check if that user is owner or not and hide the button accordingly
    }

    @Test
    public void checkBottomButton() {
        // TODO
        // get current user and check if that user is owner or not and change the button display to accordingly

        solo.clickOnButton("Collect");

        // TODO
        // Else send request
        // Test if the request was received

    }
}