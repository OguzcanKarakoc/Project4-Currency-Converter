package hogeschoolrotterdam.com.currencyconverter.API;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * The type Currency conversion request.
 */
public class CurrencyConversionRequest extends AsyncTask {

    // region: fields

    private String url = "https://free.currencyconverterapi.com/api/v5/convert?q=";
    private String query;
    private String res;
    private Activity activity;
    private boolean success;

    // endregion

    // region: constructor

    /**
     * Instantiates a new Currency conversion request.
     *
     * @param activity the activity
     * @param from     the from
     * @param to       the to
     */
    public CurrencyConversionRequest(Activity activity, String from, String to) {
        this.activity = activity;
        query = from + "_" + to;
    }

    // endregion

    // region: methods

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url + query + "&compact=ultra");
            con = (HttpsURLConnection) u.openConnection();
            con.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            JSONObject obj = new JSONObject(sb.toString());
            res = obj.getString(query);
            success = true;

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            return null;
        } finally {
            con = null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Intent intent = new Intent("API_CONVERSION_COMPLETED");
        intent.putExtra("result", res);
        intent.putExtra("success", success);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
    }

    // endregion

}
