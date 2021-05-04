package com.study.hometrainingkotlin.view.Setting

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.util.Theme.ThemeUtil

class Theme: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.themepreference)

        var key: ListPreference? = findPreference("themeSelect")

        if (key != null){
            key.setOnPreferenceChangeListener(
                Preference.OnPreferenceChangeListener
            { preference, newValue ->
                // 선택지를 String 값으로 받음
                var themeOption = newValue as String
                ThemeUtil.applyTheme(themeOption)
                true
            })
        }
    }
}