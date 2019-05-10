package hogeschoolrotterdam.com.currencyconverter.API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import hogeschoolrotterdam.com.currencyconverter.MainActivity;

/**
 * The type Currency country list.
 */
public class CurrencyCountryList extends AsyncTask {

    // region: fields

    private List<Country> country = new ArrayList<Country>();
    private String url = "https://free.currencyconverterapi.com/api/v5/countries";
    private Activity activity;
    private ProgressDialog prog;
    private boolean success;

    // endregion

    // region: constructor

    /**
     * Instantiates a new Currency country list.
     *
     * @param activity the activity
     */
    public CurrencyCountryList(MainActivity activity) {
        this.activity = activity;
    }

    // endregion

    // region: methods

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        prog = new ProgressDialog(activity);
        prog.setMessage("Loading data...");
        prog.show();

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            JSONObject obj = new JSONObject(sb.toString()).getJSONObject("results");
            Iterator<String> keysIterator = obj.keys();
            List<String> blacklist = new ArrayList<String>();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                JSONObject item = obj.getJSONObject(key);
                String id = item.getString("currencyId");
                if (!blacklist.contains(id)) {
                    Country c = new Country();
                    c.setCurrencyID(item.getString("currencyId"));
                    c.setCurrencyName(item.getString("currencyName"));
                    c.setId(item.getString("id"));
                    c.setName(item.getString("name"));
                    country.add(c);
                    blacklist.add(id);
                }
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            return "Error";
        } finally {
            con = null;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        Intent intent = new Intent("API_COUNTRYLIST_COMPLETED");
        intent.putExtra("success", success);
        MainActivity.country = country;
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
        prog.dismiss();
    }

    // endregion

}
