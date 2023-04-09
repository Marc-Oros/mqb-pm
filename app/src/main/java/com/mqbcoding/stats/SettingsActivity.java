package com.mqbcoding.stats;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.github.martoreto.aauto.vex.CarStatsClient;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private static final int REQUEST_PERMISSIONS = 0;
    private static final int REQUEST_AUTHORIZATION = 1;
    static final String EXTRA_AUTHORIZATION_INTENT = "authorizationRequest";

    private static final String PERMISSION_CAR_VENDOR_EXTENSION = "com.google.android.gms.permission.CAR_VENDOR_EXTENSION";

    private Intent mCurrentAuthorizationIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        App app = (App) getApplication();

        handleIntent();

        showNotificationServiceConfirmDialogIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void openNotificationAccess() {
          startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }
    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private void showNotificationServiceConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Please enable notification access in settings")
                .setTitle("Notification Access")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openNotificationAccess();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }
    private void showNotificationServiceConfirmDialogIfNeeded() {
        if (!isNotificationServiceEnabled()) {
            showNotificationServiceConfirmDialog();
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_AUTHORIZATION_INTENT) && mCurrentAuthorizationIntent == null) {
            mCurrentAuthorizationIntent = intent.getParcelableExtra(EXTRA_AUTHORIZATION_INTENT);
            startActivityForResult(mCurrentAuthorizationIntent, REQUEST_AUTHORIZATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> permissionsToRequest = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, PERMISSION_CAR_VENDOR_EXTENSION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(PERMISSION_CAR_VENDOR_EXTENSION);
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS);
            return;
        }

        CarStatsClient.requestPermissions(this);
    }

}
