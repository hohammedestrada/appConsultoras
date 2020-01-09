package biz.belcorp.consultoras.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import biz.belcorp.library.log.BelcorpLogger;

/**
 * Created by andres.escobar on 19/05/2017.
 */
public class CommunicationUtils {

    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    public static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";

    public static void enviarSms(Context context, String number, String message) {

        if (number != null && !number.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            Uri uri = Uri.parse("smsto:" + number);
            intent.setData(uri);
            intent.putExtra("sms_body", message);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }


    public static void enviarCorreo(Context context, String to, String message) {
        if (to != null && !to.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            try {
                context.startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            } catch (android.content.ActivityNotFoundException ex) {
                BelcorpLogger.d(ex);
            }
        }

    }

    public static void enviarWhatsapp(Context context, String number, String message) {
        try {
            String smsNumber = number + "@s.whatsapp.net";
            Uri uri = Uri.parse("smsto:" + smsNumber);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.putExtra("jid", smsNumber);
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setPackage(WHATSAPP_PACKAGE_NAME);
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Make Sure you have WhatsApp App Installed on Your Device", Toast.LENGTH_SHORT).show();
        }
    }

    public static void llamar(Activity activity, String phone) throws SecurityException {
        if (!phone.equals("")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            Uri uri = Uri.parse("tel:" + phone);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public static void goToSettings(Context context) throws SecurityException {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void goToSettingsForResult(Fragment fragment, int code) throws SecurityException {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", fragment.getContext().getPackageName(), null);
        intent.setData(uri);
        fragment.startActivityForResult(intent, code);
    }

    public static boolean isWhatsappInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean appInstalled;
        try {
            pm.getPackageInfo(WHATSAPP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    public static void openWhatsappPlayStore(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + WHATSAPP_PACKAGE_NAME)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + WHATSAPP_PACKAGE_NAME)));
        }
    }

    public static void openYoutubePlayStore(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + YOUTUBE_PACKAGE_NAME)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + YOUTUBE_PACKAGE_NAME)));
        }
    }

}
