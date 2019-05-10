package hogeschoolrotterdam.com.currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * The type Settings activity.
 */
public class SettingsActivity extends AppCompatActivity {

    // region fields

    /**
     * The constant PREF_EDITTXT_DECIMALS.
     */
    public static final String PREF_EDITTXT_DECIMALS = "edit_txt_decimals";
    /**
     * The constant PREF_SWITCH_AUTOUPDATE.
     */
    public static final String PREF_SWITCH_AUTOUPDATE = "switch_auto_update";
    /**
     * The constant PREF_SWITCH_GPS.
     */
    public static final String PREF_SWITCH_GPS = "switch_gps";

    // endregion

    // region: methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (!TextUtils.isDigitsOnly(sharedPref.getString(SettingsActivity.PREF_EDITTXT_DECIMALS, "2"))) {
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.putString(SettingsActivity.PREF_EDITTXT_DECIMALS, "2");
                    edit.apply();
                    Toast.makeText(getApplicationContext(), "Invalid number entered! Please ensure that you only type NUMBERS!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // endregion

}


