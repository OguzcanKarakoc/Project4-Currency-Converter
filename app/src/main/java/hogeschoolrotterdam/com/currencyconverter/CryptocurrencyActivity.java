package hogeschoolrotterdam.com.currencyconverter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Spinner;

import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hogeschoolrotterdam.com.currencyconverter.API.Cryptocurrency;
import hogeschoolrotterdam.com.currencyconverter.API.CryptocurrencyListRequest;
import hogeschoolrotterdam.com.currencyconverter.API.CurrencyHistoryRequest;

/**
 * The type Cryptocurrency activity.
 */
public class CryptocurrencyActivity extends AppCompatActivity {

    // region: fields

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private BroadcastReceiver recieverCrypto;

    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Cryptocurrency> cryptoList;
    private RecyclerView.Adapter adapter;

    // endregion

    // region: constructor

    // endregion

    // region: methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptocurrency);
        Drawer();
        new CryptocurrencyListRequest(this).execute();
        registerBroadcastReciever();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Drawer.
     */
    public void Drawer() {
        // 'item' is used to detirmine which item in the nav drawer matches the current activity
        // This is used to ensure that the correct item in the drawer is 'Checked'

        // History = 0
        // Main page = 1
        // Cryptocurrency = 2
        // Settings = 3
        int item = 2;
        NavigationView navView = findViewById(R.id.nav_view);
        for (int i=0; i < 4; i++) {
            if (i == item) {
                navView.getMenu().getItem(i).setChecked(true);
            }
            else {
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

    private void registerBroadcastReciever() {
        final Activity activity = this;
        recieverCrypto = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                cryptoList = CryptocurrencyListRequest.getResult();
                mList = findViewById(R.id.cryptoRecyclerList);

                adapter = new CryptocurrencyAdapter(getApplicationContext(), cryptoList);

                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

                mList.setHasFixedSize(true);
                mList.setLayoutManager(linearLayoutManager);
                mList.addItemDecoration(dividerItemDecoration);
                mList.setAdapter(adapter);

            }


        };

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(recieverCrypto, new IntentFilter("API_CRYPTOCURRENCY_REQUEST"));
    }


    // endregion

}