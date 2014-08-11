/*
 * Copyright 2010 the original author or authors.
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
package org.androidpn.client;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.util.SysApplication;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Window;

/**
 * Activity for displaying the notification setting view.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationSettingsActivity extends PreferenceActivity {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationSettingsActivity.class);

	public NotificationSettingsActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TitleBar.title="系统设置";
		Window win = this.getWindow();
		win.requestFeature(Window.FEATURE_CUSTOM_TITLE);// 注意顺序
		super.onCreate(savedInstanceState);
		win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);// 注意顺序
		// 设置标题栏
		TitleBar titleBar = new TitleBar(this);
		// 注册到activity管理类中
		SysApplication.getInstance().addActivity(this);
		setPreferenceScreen(createPreferenceHierarchy());
		setPreferenceDependencies();
		CheckBoxPreference notifyPref = (CheckBoxPreference) getPreferenceManager()
				.findPreference(Constants.SETTINGS_NOTIFICATION_ENABLED);
		if (notifyPref.isChecked()) {
			notifyPref.setTitle("是否接收推送");
		} else {
			notifyPref.setTitle("是否接收推送");
		}
	}

	private PreferenceScreen createPreferenceHierarchy() {
		Log.d(LOGTAG, "createSettingsPreferenceScreen()...");

		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager
				.setSharedPreferencesName(Constants.SHARED_PREFERENCE_NAME);
		preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);

		PreferenceScreen root = preferenceManager.createPreferenceScreen(this);

		// PreferenceCategory prefCat = new PreferenceCategory(this);
		// // inlinePrefCat.setTitle("");
		// root.addPreference(prefCat);

		CheckBoxPreference notifyPref = new CheckBoxPreference(this);
		notifyPref.setKey(Constants.SETTINGS_NOTIFICATION_ENABLED);
		notifyPref.setTitle("是否接收推送");
		notifyPref.setSummaryOn("接收");
		notifyPref.setSummaryOff("拒绝");
		notifyPref.setDefaultValue(Boolean.TRUE);
		notifyPref
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean checked = Boolean.valueOf(newValue.toString());
						if (checked) {
							preference.setTitle("是否接收推送");
						} else {
							preference.setTitle("是否接收推送");
						}
						return true;
					}
				});

		CheckBoxPreference soundPref = new CheckBoxPreference(this);
		soundPref.setKey(Constants.SETTINGS_SOUND_ENABLED);
		soundPref.setTitle("声音提示");
		soundPref.setSummary("开启声音提示");
		soundPref.setDefaultValue(Boolean.TRUE);
		// soundPref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);

		CheckBoxPreference vibratePref = new CheckBoxPreference(this);
		vibratePref.setKey(Constants.SETTINGS_VIBRATE_ENABLED);
		vibratePref.setTitle("震动提示");
		vibratePref.setSummary("开启震动提示");
		vibratePref.setDefaultValue(Boolean.TRUE);
		// vibratePref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);

		root.addPreference(notifyPref);
		root.addPreference(soundPref);
		root.addPreference(vibratePref);

		// prefCat.addPreference(notifyPref);
		// prefCat.addPreference(soundPref);
		// prefCat.addPreference(vibratePref);
		// root.addPreference(prefCat);

		return root;
	}

	private void setPreferenceDependencies() {
		Preference soundPref = getPreferenceManager().findPreference(
				Constants.SETTINGS_SOUND_ENABLED);
		if (soundPref != null) {
			soundPref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
		}
		Preference vibratePref = getPreferenceManager().findPreference(
				Constants.SETTINGS_VIBRATE_ENABLED);
		if (vibratePref != null) {
			vibratePref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
		}
	}

}
