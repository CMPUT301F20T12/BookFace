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

public class ZoomActivityTest {
    // TODO
    // Add Tests

    private Solo solo;

    @Rule
    public ActivityTestRule<ZoomActivity> rule =
            new ActivityTestRule<>(ZoomActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkActivity() {
        solo.assertCurrentActivity("Wrong activity", ZoomActivity.class);
    }

    @Test
    public void checkImage() {
        solo.assertCurrentActivity("Wrong activity", ZoomActivity.class);
        View img = solo.getView("enlarged_image");
        solo.clickOnView(img);
    }
}