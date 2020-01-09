package biz.belcorp.consultoras.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.Collections;

import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel;

public class FacebookUtil {
    private static final String APP_PERMISIONS = "public_profile,email";
    private static final String FACEBOOK_MESSENGER_PACKAGE_NAME = "com.facebook.orca";

    private CallbackManager mCallbackManager;
    private OnFacebookListener mListener;

    public FacebookUtil(OnFacebookListener listener) {
        mListener = listener;
        buildCallBackManager();
    }

    private void buildCallBackManager() {
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (!AccessToken.getCurrentAccessToken().getDeclinedPermissions().isEmpty())
                    mListener.onLoginPermissionDeclined();
                else
                    requestProfile(loginResult.getAccessToken(), 0);
            }

            @Override
            public void onCancel() {
                mListener.onLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                mListener.onLoginFailure(error);
            }
        });
    }

    private void requestProfile(AccessToken accessToken, final int type) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {

            if (object == null) {
                mListener.onLoginFailure(new FacebookException("No se pudo obtener la información del usuario."));
                return;
            }

            FacebookProfileModel model = new FacebookProfileModel();

            model.setId(object.optString("id"));
            model.setName(object.optString("name"));
            model.setEmail(object.optString("email"));
            model.setImage(getImageUrl(model.getId()));

            model.setFirstName(object.optString("first_name"));
            model.setLastName(object.optString("last_name"));
            model.setLinkProfile(getProfileUrl(object.optString("id")));
            model.setBirthday(object.optString("birthday"));
            model.setGender(object.optString("gender"));
            model.setLocation(object.optString("location"));

            if (type == 0)
                mListener.onLoginSuccess(model);
            else
                mListener.onRenderData(model);
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "birthday,email,first_name,gender,hometown,id,last_name,link,location,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void login(Activity activity) {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singletonList(APP_PERMISIONS));
    }

    public void retryPermission(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity,
            Arrays.asList("public_profile", "email"));
    }

    public void logOutFacebook() {
        if (LoginManager.getInstance() != null)
            LoginManager.getInstance().logOut();
    }

    public CallbackManager getCallBackManager() {
        return mCallbackManager;
    }

    public AccessToken getAccesToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public interface OnFacebookListener {
        void onLoginSuccess(FacebookProfileModel model);

        void onLoginFailure(FacebookException exception);

        void onLoginCancel();

        void onLoginPermissionDeclined();

        void onRenderData(FacebookProfileModel model);
    }

    private static String getImageUrl(String id) {
        return "https://graph.facebook.com/" + id + "/picture?type=large";
    }

    private static String getProfileUrl(String id) {
        return "https://www.facebook.com/" + id;
    }

    public static boolean isFacebookMessengerInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(FACEBOOK_MESSENGER_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static void openFacebookMessengerPlayStore(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + FACEBOOK_MESSENGER_PACKAGE_NAME)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + FACEBOOK_MESSENGER_PACKAGE_NAME)));
        }
    }

}
/*
public  boolean isFacebookMessengerInstalled(Context context, app String) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(app, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
 */
