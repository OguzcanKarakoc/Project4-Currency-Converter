package hogeschoolrotterdam.com.currencyconverter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hogeschoolrotterdam.com.currencyconverter.API.Country;
import hogeschoolrotterdam.com.currencyconverter.API.CurrencyConversionRequest;
import hogeschoolrotterdam.com.currencyconverter.API.CurrencyCountryList;
import hogeschoolrotterdam.com.currencyconverter.API.CurrencyHistoryRequest;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private boolean isLoaded = false;
    private SharedPreferences sharedPref;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private EditText txtInput;
    private TextView txtOutput;
    private Spinner spinnerInput;
    private Spinner spinnerOutput;
    private ArrayAdapter<String> spinnerAdapter;
    private GraphView graph;

    /**
     * The constant country.
     */
    public static List<Country> country = new ArrayList<Country>();
    private BroadcastReceiver recieverConversion;
    private BroadcastReceiver recieverCountry;
    private BroadcastReceiver recieverHistory;

    // Hoeveel euro de currency waard is tegenover 1 euro
    private double value = 1.23456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HistoryJSONReader.init(getApplicationContext());
        // Saves the default setting values to file, this method will not be called if it was already called in the past.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Drawer();
        registerBroadcastReciever();
        new CurrencyHistoryRequest(this, "EUR", "USD").execute();

        this.graph = (GraphView) findViewById(R.id.graph);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Days");
        gridLabel.setVerticalAxisTitle("Values");
        gridLabel.setPadding(32); // should allow for 3 digits to fit on screen
        // legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextColor(Color.WHITE);
        graph.getLegendRenderer().setBackgroundColor(Color.argb(50, 63, 81, 181));
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        spinnerInput = findViewById(R.id.spinnerInput);
        spinnerOutput = findViewById(R.id.spinnerOutput);
        // Get the input and output text fields and set the text of input/output field to empty.
        txtInput = findViewById(R.id.txtEditInput);
        txtOutput = findViewById(R.id.txtOutputField);
        txtOutput.setText("");

        //API CLASS
        //new CurrencyAPI(this).execute();
        getCurrency();
    }

    private void getCurrency() {
        new CurrencyCountryList(this).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Drawer() {
        // 'item' is used to detirmine which item in the nav drawer matches the current activity
        // This is used to ensure that the correct item in the drawer is 'Checked'

        // History = 0
        // Main page = 1
        // Cryptocurrency = 2
        // Settings = 3
        int item = 1;
        NavigationView navView = findViewById(R.id.nav_view);
        for (int i = 0; i < 4; i++) {
            if (i == item) {
                navView.getMenu().getItem(i).setChecked(true);
            } else {
                navView.getMenu().getItem(i).setChecked(false);
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        displaySettings();
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_crypto:
                        displayCrypto();
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_main:
                        displayMain();
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_history:
                        displayHistory();
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    private void loadSpinner() {
        // Construct the list

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < country.size(); i++) {
            list.add(country.get(i).getCurrencyID() + " - " + country.get(i).getCurrencyName());
        }

        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInput.setAdapter(spinnerAdapter);
        spinnerOutput.setAdapter(spinnerAdapter);

        // Once spinners are loaded, we register a text watcher that watches for any user input (assuming this is enabled in preferences)
        if (sharedPref.getBoolean(SettingsActivity.PREF_SWITCH_AUTOUPDATE, false)) {
            registerTextWatcher();
        }
    }

    private void displaySettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void displayCrypto() {
        Intent intent = new Intent(this, CryptocurrencyActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void onCalcClick(View v) {
        if (!txtInput.getText().toString().equals("")) {
            String selectionInput = spinnerInput.getSelectedItem().toString().substring(0, Math.min(spinnerInput.getSelectedItem().toString().length(), 3));
            String selectionOutput = spinnerOutput.getSelectedItem().toString().substring(0, Math.min(spinnerOutput.getSelectedItem().toString().length(), 3));
            new CurrencyConversionRequest(this, selectionInput, selectionOutput).execute();
            new CurrencyHistoryRequest(this, selectionInput, selectionOutput).execute();

            txtOutput.setText("Calculating...");
        }
    }

    public void onSwitchCurrencyClick(View v) {
        int inputID = spinnerAdapter.getPosition(spinnerInput.getSelectedItem().toString());
        int outputID = spinnerAdapter.getPosition(spinnerOutput.getSelectedItem().toString());

        spinnerInput.setSelection(outputID, true);
        spinnerOutput.setSelection(inputID, true);
    }

    private void registerTextWatcher() {
        txtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isLoaded) {
                    onCalcClick(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isLoaded) {
                    onCalcClick(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isLoaded) {
                    onCalcClick(null);
                }
            }
        });
    }

    private void registerBroadcastReciever() {
        final Activity activity = this;
        recieverCountry = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("success", false) == false) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Error");
                    dialog.setMessage("An error occured while loading the currency data. Please check your internet connection.");
                    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getCurrency();
                        }
                    });
                    dialog.show();
                } else {
                    loadSpinner();
                    isLoaded = true;
                }
            }
        };
        recieverConversion = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("success", false) == true && isLoaded) {
                    double input = Double.parseDouble(txtInput.getText().toString());
                    double output = Double.parseDouble(intent.getStringExtra("result")) * input;
                    if ((output - (int) output) != 0) {
                        String decimals = sharedPref.getString(SettingsActivity.PREF_EDITTXT_DECIMALS, "2");
                        String decimalFormat = "%." + decimals + "f";
                        Log.d("DEBUG", String.valueOf(output));
                        String res = String.format(decimalFormat, output);
                        Log.d("DEBUG", res);
                        txtOutput.setText(res);

                        // Save to history
                        Spinner outputSpinner = findViewById(R.id.spinnerOutput);
                        String currOut = outputSpinner.getSelectedItem().toString().substring(0, 3);
                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        HistoryJSONReader.addEntry(getApplicationContext(), spinnerInput.getSelectedItem().toString().substring(0, 3), currOut, String.valueOf(input), res, mydate);


                    } else {
                        txtOutput.setText(String.valueOf(output));
                    }
                } else {
                    txtOutput.setText("Error");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Error");
                    dialog.setMessage("An error occured while loading the currency data. Please check your internet connection.");
                    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }


            }
        };

        recieverHistory = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("success", false)) {
                    List<String> result = CurrencyHistoryRequest.getLastOutput();
                    System.out.println("--- INSIDE ---");

                    graph.removeAllSeries();
                    if (spinnerInput.getSelectedItem() != null && spinnerOutput.getSelectedItem() != null) {
                        String selectionInput = spinnerInput.getSelectedItem().toString().substring(0, Math.min(spinnerInput.getSelectedItem().toString().length(), 3));
                        String selectionOutput = spinnerOutput.getSelectedItem().toString().substring(0, Math.min(spinnerOutput.getSelectedItem().toString().length(), 3));

                        List<DataPoint> dataPoints = new ArrayList<>();
                        int i = 0;
                        for (String value : result) {
                            dataPoints.add(new DataPoint(i, Double.parseDouble(value)));
                            i++;
                        }

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(0, Double.parseDouble(result.get(0))),
                                new DataPoint(1, Double.parseDouble(result.get(1))),
                                new DataPoint(2, Double.parseDouble(result.get(2))),
                                new DataPoint(3, Double.parseDouble(result.get(3))),
                                new DataPoint(4, Double.parseDouble(result.get(4))),
                                new DataPoint(5, Double.parseDouble(result.get(5))),
                                new DataPoint(6, Double.parseDouble(result.get(6))),
                                new DataPoint(7, Double.parseDouble(result.get(7)))
                        });
                        series.setColor(Color.rgb(63, 81, 181));
                        series.setBackgroundColor(Color.argb(50, 63, 81, 181));
                        series.setDrawBackground(true);
                        graph.addSeries(series);
                        // legend
                        series.setTitle(selectionInput + " - " + selectionOutput);
                        graph.getLegendRenderer().setVisible(true);
                        graph.getLegendRenderer().setTextColor(Color.WHITE);
                        graph.getLegendRenderer().setBackgroundColor(Color.argb(100, 63, 81, 181));
                        graph.getLegendRenderer().setFixedPosition(15, 15);
                        graph.getLegendRenderer().setWidth(400);
                        graph.getGridLabelRenderer().setPadding(5);
                    }

                    System.out.println("--- OUTSIDE ---");


                    // Code to update graph here...
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(recieverCountry, new IntentFilter("API_COUNTRYLIST_COMPLETED"));
        LocalBroadcastManager.getInstance(this).registerReceiver(recieverConversion, new IntentFilter("API_CONVERSION_COMPLETED"));
        LocalBroadcastManager.getInstance(this).registerReceiver(recieverHistory, new IntentFilter("API_HISTORY_COMPLETED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(recieverConversion);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(recieverCountry);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(recieverHistory);
    }
}