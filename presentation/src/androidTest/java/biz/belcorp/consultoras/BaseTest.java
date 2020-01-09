package biz.belcorp.consultoras;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.web.model.Evaluation;
import android.support.test.espresso.web.sugar.Web;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.microsoft.appcenter.espresso.Factory;
import com.microsoft.appcenter.espresso.ReportHelper;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.model.Atoms.script;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static biz.belcorp.consultoras.util.Util.childAtPosition;
import static biz.belcorp.consultoras.util.Util.getActivityInstance;
import static biz.belcorp.consultoras.util.Util.withCountryIso;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
abstract class BaseTest {

    protected static final String COUNTRY = "PE";
    protected static final String USERNAME = "011871852";
    protected static final String PASSWORD = "22996666";
    protected static final String PRODUCTO_CODE = "00006";
    protected static final int TIMEOUT_WEB = 1000;
    protected static final int TIMEOUT_ASYNC = 3500;

    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_CONTACTS,
        android.Manifest.permission.READ_CONTACTS
    );

    void nextOpenActivityIs(Class<?> clazz) {
        intended(IntentMatchers.hasComponent(clazz.getName()));
    }

    boolean isActivity(Class<?> clazz) {
        Activity activity = getActivityInstance();
        return clazz.isInstance(activity);
    }

    ViewInteraction click(@IdRes int viewId) {
        return onView(withId(viewId))
            .perform(ViewActions.click());
    }

    ViewInteraction click(@IdRes int viewId, String value) {
        return onView(allOf(withId(viewId), withText(value)))
            .perform(ViewActions.click());
    }

    ViewInteraction longClick(@IdRes int viewId) {
        return onView(withId(viewId))
            .perform(ViewActions.longClick());
    }

    Web.WebInteraction<Evaluation> webClickById(String value) {
        return onWebView()
            .withElement(findElement(Locator.ID, value))
            .perform(DriverAtoms.webClick());
    }

    Web.WebInteraction<Evaluation> webClickByClassName(String value) {
        return onWebView()
            .withElement(findElement(Locator.CLASS_NAME, value))
            .perform(DriverAtoms.webClick());
    }

    Web.WebInteraction<Evaluation> execScript(String value) {
        return onWebView()
            .perform(script(value));
    }

    ViewInteraction setValue(@IdRes int viewId, String value) {
        return onView(withId(viewId))
            .perform(ViewActions.replaceText(value), ViewActions.pressImeActionButton());
    }

    ViewInteraction matchText(@IdRes int viewId, @StringRes int stringResource) {
        return onView(withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withText(stringResource)));
    }

    ViewInteraction matchText(@IdRes int viewId, String value) {
        return onView(withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withText(value)));
    }

    ViewInteraction matchVisible(@IdRes int viewId) {
        return onView(withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    ViewInteraction matchVisibleAndText(@StringRes int stringResource) {
        return onView(ViewMatchers.withText(stringResource))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    ViewInteraction matchVisibleAndText(String value) {
        return onView(ViewMatchers.withText(value))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    void selectedCountry(Class<?> clazz) {
        onData(allOf(is(instanceOf(clazz)), withCountryIso(COUNTRY))).perform(ViewActions.click());
    }

    void checkAnimation() {
        try {
            matchVisible(R.id.material_target_prompt_view);

            onView(
                Matchers.allOf(withId(R.id.llt_item_lateral),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.mlt_menu),
                            0),
                        0),
                    isDisplayed()))
                .perform(ViewActions.click());

        } catch (Exception ignored) {
            // Empty
        }
    }

    void goToPage(int pos) {
        ViewInteraction menu = onView(allOf(withId(R.id.rvw_menu), isDisplayed()));
        menu.perform(actionOnItemAtPosition(pos, ViewActions.click()));
    }

    void goToMenu(int pos) {

        onView(
            Matchers.allOf(withId(R.id.llt_item_lateral),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mlt_menu),
                        pos),
                    0),
                isDisplayed()))
            .perform(ViewActions.click());
    }

    void goToBack() {
        Espresso.pressBack();
    }

    void screenshot(String name) {
        reportHelper.label(name);
    }
}
