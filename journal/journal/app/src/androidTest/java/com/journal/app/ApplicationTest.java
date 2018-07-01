package com.journal.app;

import android.app.Application;
import android.support.test.espresso.action.TypeTextAction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.journal.app.activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onView;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
/*public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}*/
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest  extends ApplicationTestCase<Application>{
    public ApplicationTest() {
        super(Application.class);}
    private String mStringToBetyped;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);
    @Before



    @Test
   /* public void selectBySpinnerText() throws Exception {
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("minutes"))).perform(click());
    }*/
    public void listGoesOverTheFold() {
        onView(withId(R.id.normalsign_in_button)).perform(click());
        onView(withId(R.id.edit)).perform(click());
        onView(withId(R.id.input_task_desc)).perform(clearText(),typeText("hydration"), click());
        onView(withId(R.id.input_task_notes)).perform(typeText("I need to drink half a cup of" +
                " " + "water every 15 minutes to stay hydrated"),click());
        onView(withId(R.id.toDoRG)).perform(click());
        onView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.input_task_time)).perform(typeText("15"),click());
        //spinner class
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("minutes"))).perform(click());
        //
        onView(withId(R.id.btn_save)).perform(click());
       // onView(withId(R.id.cancel_button)).perform(click());
        //onView(withId(R.id.delete)).perform(click());
        onView(withId(R.id.imageButton)).perform(click());


    }
}
