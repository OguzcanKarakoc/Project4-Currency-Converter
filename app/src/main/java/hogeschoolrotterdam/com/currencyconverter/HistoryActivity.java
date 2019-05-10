package hogeschoolrotterdam.com.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The type History activity.
 */
public class HistoryActivity extends AppCompatActivity {

    // region: fields

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<HistoryItem> historyList;
    private RecyclerView.Adapter adapter;

    // endregion

    // region: methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Drawer();

        mList = findViewById(R.id.historyList);

        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(getApplicationContext(), historyList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        try {
            JSONArray array = new JSONArray(HistoryJSONReader.Read(getApplicationContext()));
            for (int i = array.length()-1; i >= 0; i--) {
                JSONObject obj = array.getJSONObject(i);
                HistoryItem item = new HistoryItem();
                item.setInputCurrency(obj.getString("InputCurrency"));
                item.setOutputCurrency(obj.getString("OutputCurrency"));
                item.setInputValue(obj.getString("InputValue"));
                item.setOutputValue(obj.getString("OutputValue"));
                item.setDate(obj.getString("Date"));
                historyList.add(item);
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

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
        int item = 0;
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

    // endregion

}