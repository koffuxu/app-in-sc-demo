/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.preferenceactivitytest;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class PreferenceActivityTest extends PreferenceActivity 
        implements OnSharedPreferenceChangeListener {
    static final String TAG = "PreferenceActivity";
    public static final String LIST_KEY = "list_key";
//    private EditTextPreference mEdit;
    private ListPreference mList;
 //   private CheckBoxPreference mCheckbox;


    @Override
    protected void onCreate(Bundle icycle) {
        super.onCreate(icycle);

        // load layout xml file
//        setContentView(R.layout.activity_main);
        addPreferencesFromResource(R.layout.preferences);
        initPreference();
    }

    private void initPreference() {
        mList = (ListPreference)findPreference(LIST_KEY);
    } 

    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        mList.setSummary(sp.getString(LIST_KEY, ""));

        sp.registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences mSp, String key) {
        if (key.equals(LIST_KEY)) {
            mList.setSummary(mSp.getString(key, ""));
        }
    }
}
