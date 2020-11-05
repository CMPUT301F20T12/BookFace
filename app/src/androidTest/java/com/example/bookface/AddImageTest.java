package com.example.bookface;

import android.view.View;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class AddImageTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<AddImage> rule =
            new ActivityTestRule<>(AddImage.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkAddImageActivity() {
        solo.assertCurrentActivity("Wrong Activity", AddImage.class);
    }

    @Test
    public void checkCamera() {
        View camera = solo.getView(R.id.cameraFloatingBtn);
        solo.clickOnView(camera);
//        Toast.makeText(getApplicationContext(), "Camera opens", Toast.LENGTH_LONG).show();
    }
}
