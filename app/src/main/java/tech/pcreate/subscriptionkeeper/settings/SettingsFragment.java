package tech.pcreate.subscriptionkeeper.settings;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import tech.pcreate.subscriptionkeeper.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_pref);

    }
}

