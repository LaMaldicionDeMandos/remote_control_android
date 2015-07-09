package org.pasut.android.remotecontrol.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import org.pasut.android.remotecontrol.activities.SettingsActivity;

import static android.widget.Toast.LENGTH_LONG;
import static org.pasut.android.remotecontrol.R.string.fail_server_dialog_message;
import static org.pasut.android.remotecontrol.R.string.fail_server_dialog_title;
import static org.pasut.android.remotecontrol.R.string.fail_server_message;
import static org.pasut.android.remotecontrol.R.string.no;
import static org.pasut.android.remotecontrol.R.string.yes;

/**
 * Created by marcelo on 09/07/15.
 */
public class ActivityUtils {
    public static <T extends Activity> void configurarionErrorMessage(final Activity context, final int request) {
        Toast.makeText(context, fail_server_message, LENGTH_LONG).show();
        new AlertDialog.Builder(context)
                .setTitle(fail_server_dialog_title)
                .setMessage(fail_server_dialog_message)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                })
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivityForResult(new Intent(context, SettingsActivity.class), request);
                    }
                }).show();
    }
}
