package hogeschoolrotterdam.com.currencyconverter;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    // region fields

    // endregion

    // region: constructor

    // endregion

    // region: methods

    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference btn = findPreference("btn_reset");
        btn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm resetting settings")
                        .setMessage("Do you really want to reset all settings?")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences sharedPref = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor edit = sharedPref.edit();
                                edit.clear();
                                edit.commit();
                                Toast.makeText(getContext(), "Settings reset!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
        Preference btnResetHistory = findPreference("btn_deleteHistory");
        btnResetHistory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm deleting history")
                        .setMessage("Do you really want to delete the history?")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                HistoryJSONReader.clearHistory(getContext());
                                Toast.makeText(getContext(), "History deleted!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    // endregion

}
