package com.mqbcoding.stats;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.martoreto.aauto.vex.CarStatsClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "PreferenceFragment";

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Log.d(TAG, "Pereference change: " + preference.getKey());

            String stringValue = value == null ? "" : value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } if (preference instanceof TemperaturePreference) {
                return true;
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getAll().get(preference.getKey()));
    }

    private List<File> findLogs() throws IOException {
        File logDir = CarStatsLogger.getLogsDir();
        List<File> files = new ArrayList<>();
        for (File f: logDir.listFiles()) {
            if (f.getName().endsWith(".log.gz")) {
                files.add(f);
            }
        }
        Collections.sort(files);
        return files;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        findPreference("listProviders").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.pref_providers);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1);
                PackageManager pm = getActivity().getPackageManager();
                for (ResolveInfo i: CarStatsClient.getProviderInfos(getActivity())) {
                    adapter.add(i.serviceInfo.loadLabel(pm));
                }
                builder.setAdapter(adapter, null);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        bindPreferenceSummaryToValue(findPreference("oilTempThreshold"));
        bindPreferenceSummaryToValue(findPreference("fueltanksize"));
        bindPreferenceSummaryToValue(findPreference("performanceTitle1"));
        bindPreferenceSummaryToValue(findPreference("performanceTitle2"));
        bindPreferenceSummaryToValue(findPreference("performanceTitle3"));
        bindPreferenceSummaryToValue(findPreference("engineSpeedSoundUpToGear"));
        bindPreferenceSummaryToValue(findPreference("engineSpeedESInform"));
        bindPreferenceSummaryToValue(findPreference("engineSpeedESHint"));
        bindPreferenceSummaryToValue(findPreference("engineSpeedESWarn"));

        Preference statsLoggerPref = findPreference(CarStatsLogger.PREF_ENABLED);
        try {
            statsLoggerPref.setSummary(
                    getString(R.string.pref_stats_logging_summary, CarStatsLogger.getLogsDir()));
            statsLoggerPref.setEnabled(true);
        } catch (IOException e) {
            statsLoggerPref.setSummary(
                    getString(R.string.pref_stats_logging_not_available, e.toString()));
            statsLoggerPref.setEnabled(false);
        }
    }
}
