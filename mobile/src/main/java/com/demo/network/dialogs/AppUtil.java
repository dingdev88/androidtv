package com.demo.network.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.demo.network.common.DebugHelper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {

    private static final String INT_DECIMAL = "%02d";

    private static final String LONG_DECIMAL = "%02l";

    final static String TAG = "AppUtil";

    public static final long KB = 1024;
    public static final long MB = KB * KB;
    public static final long GB = MB * KB;
    public static final long TB = GB * KB;

    public static final String FULL_DATE_FORMAT = "HH:mm:ss MMM dd, yyyy Z";
    public static final String BASIC_DATE_FORMAT = "MMM dd, yyyy";

    public static boolean isEmptyString(String string) {
        return (string == null) || (string.length() == 0) || (string.equals("null")) || (string.equals("(null)"));
    }

    public static String verifyTextFieldData(final Context context, final EditText field, final boolean trimSpace,
                                             final boolean canEmpty, final boolean showError, final int lenght) {
        String userText = null;
        String errorString = null;
        Resources resource = null;

        if (field != null) {
            if (context != null) {
                resource = context.getResources();
            }
            userText = field.getText().toString();
            if (userText == null || userText.length() <= 0) {
                userText = null;
                if (!canEmpty) {
                    errorString = "Required Filed";
                }
            } else {
                if (trimSpace) {
                    userText = userText.trim();
                    if (userText == null || userText.length() <= 0) {
                        userText = null;
                        if (!canEmpty) {
                            errorString = "Required Filed";
                        }

                    } else {
                        if (lenght > -1 && userText.length() < lenght) {
                            errorString = "Required Filed";
                        }
                    }
                }
            }
            if (showError) {
                field.setError(errorString);
            }
        }
        return userText;
    }

    public static String verifyTextFieldData(final Context context, final EditText field) {
        return AppUtil.verifyTextFieldData(context, field, true, false, true, -1);
    }

    public static void hideKeyBoard(Activity activity) {
        try {
            hideKeyBoard(activity, activity.getCurrentFocus().getWindowToken());
        } catch (Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    public static void hideKeyBoard(Context context, EditText editText) {
        try {
            hideKeyBoard(context, editText.getWindowToken());
        } catch (Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    private static void hideKeyBoard(Context context, IBinder windowToken) {
        try {
            if (context != null && windowToken != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    public static void showDefultDialogMessage(final Context context, final String message) {

        try {
            if (context != null) {
                final AlertDialog alert = new AlertDialog.Builder(context).setMessage(message).setPositiveButton("Ok", null)
                        .create();
                alert.show();

            }
        } catch (final Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    public static void showDialog(final Context context, final Bundle bundle, final AppDialogClickListiner handler) {

        if (bundle != null) {
            final String keyHeder = EMessageKeys.DIALOG_HEADER.getKey();
            final String keyMessage = EMessageKeys.DIALOG_BODY.getKey();
            final String txtHeader = bundle.getString(keyHeder);
            final String txtMsg = bundle.getString(keyMessage);
            AppUtil.showDialog(context, txtHeader, txtMsg, null, null, handler);
        }
    }

    public static void showDialog(final Context context, final String message) {

        AppUtil.showDialog(context, "Error", message, null);
    }

    public static void showDialog(final Context context, final String title, final String message) {

        AppUtil.showDialog(context, title, message, null);
    }

    public static void showDialog(final Context context, final String title, final String message,
                                  final AppDialogClickListiner hanlder) {
        showDialog(context, title, message, null, null, hanlder);
    }

    public static void showDialog(final Context context, final String title, final String message, String positiveBtn,
                                  String cancelButton, final AppDialogClickListiner hanlder) {

        try {
            if (context != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message).setTitle(title);
                if (positiveBtn != null) {
                    builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (hanlder != null) {
                                hanlder.onDialogClick(AppDialogUserActions.OK);
                            }
                        }
                    });
                }
                if (cancelButton != null) {
                    builder.setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (hanlder != null) {
                                hanlder.onDialogClick(AppDialogUserActions.CANCEL);
                            }
                        }
                    });
                }
                builder.create().show();
            }
        } catch (final Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+([A-Z0-9.-])*+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        try {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            if (matcher.matches()) {
                isValid = true;
            }
        } catch (NullPointerException exception) {
            DebugHelper.printException(exception);
        }
        return isValid;
    }

    public static void deleteFile(String path) {
        if (path != null) {
            File file = new File(path);
            file.delete();
        }
    }

    public static boolean isAlphaNumeric(String string) {
        if (isMinEightCharactors(string)) {
            final Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
            final boolean alpha = pattern.matcher(string).matches();
            final boolean numeric = string.matches(".*\\d.*");
            return alpha && numeric;
        } else {
            return false;
        }
    }

    public static boolean isMinEightCharactors(String string) {
        return string != null && string.length() >= 8;
    }

    public static boolean containOneCharactor(String string) {
        if (string != null) {
            final Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
            return pattern.matcher(string).matches();
        }
        return false;
    }
}
