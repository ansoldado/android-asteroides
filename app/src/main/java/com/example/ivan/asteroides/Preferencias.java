package com.example.ivan.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Ivan on 4/10/16.
 */

public class Preferencias extends PreferenceActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciasFragment()) .commit();
    }
}