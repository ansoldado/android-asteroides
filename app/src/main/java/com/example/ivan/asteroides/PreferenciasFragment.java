package com.example.ivan.asteroides;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Ivan on 8/10/16.
 */

public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}