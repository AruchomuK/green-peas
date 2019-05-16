package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.*;
import android.view.MenuItem;
import android.widget.Toast;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.MessageUtils;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.baseactivity.BaseActivity;
import rx.Observable;
import rx.schedulers.Schedulers;

public class SettingsActivity extends BaseActivity
{
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String stringValue = value.toString();

            if ( preference instanceof ListPreference )
            {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[ index ]
                                : null);
            }
            else
            {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static boolean isXLargeTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID()
    {
        return R.id.nav_settings;
    }

    protected boolean isValidFragment(String fragmentName)
    {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            bindPreferenceSummaryToValue(findPreference(SettingsKeys.CURRENCY));

            SwitchPreference checkboxSettings = (SwitchPreference) findPreference(SettingsKeys.CHECKBOX_POSITION_PREF);
            checkboxSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    if ( !checkboxSettings.isChecked() )
                    {
                        MessageUtils.showToast(getActivity(), R.string.pref_checkbox_toast_on, Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        MessageUtils.showToast(getActivity(), R.string.pref_checkbox_toast_off, Toast.LENGTH_SHORT);
                    }
                    return true;
                }
            });
            checkboxSettings.setSwitchTextOn(R.string.pref_checkbox_right_display_text);
            checkboxSettings.setSwitchTextOff(R.string.pref_checkbox_left_display_text);

            SwitchPreference moveProductsPref = (SwitchPreference) findPreference(SettingsKeys.MOVE_PRODUCTS_PREF);
            moveProductsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    if ( !moveProductsPref.isChecked() )
                    {
                        MessageUtils.showToast(getActivity(), R.string.pref_move_products_toast_on, Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        MessageUtils.showToast(getActivity(), R.string.pref_move_products_toast_off, Toast.LENGTH_SHORT);
                    }
                    return true;
                }
            });


            SwitchPreference notificationsSetting = (SwitchPreference) findPreference(SettingsKeys.NOTIFICATIONS_ENABLED);
            notificationsSetting.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    if ( !notificationsSetting.isChecked() )
                    {
                        MessageUtils.showToast(getActivity(), R.string.pref_notifications_toast_on, Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        MessageUtils.showToast(getActivity(), R.string.pref_notifications_toast_off, Toast.LENGTH_SHORT);
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if ( id == android.R.id.home )
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
