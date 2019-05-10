package hogeschoolrotterdam.com.currencyconverter.API;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * The type Currency history request.
 */
public class CurrencyHistoryRequest extends AsyncTask {

    // region: fields

    private String url = "https://free.currencyconverterapi.com/api/v5/convert?q=USD_EUR,EUR_USD&compact=ultra&date=2018-02-19&endDate=2018-02-24";
    private String query;

    // Current date
    private String todayDate;
    private String previousDate;

    private static List<String> lastOutput = new ArrayList<String>();
    private Activity activity;
    private boolean success;

    // endregion

    // region: constructor

    /**
     * Instantiates a new Currency history request.
     *
     * @param activity the activity
     * @param from     the from
     * @param to       the to
     */
    public CurrencyHistoryRequest(Activity activity, String from, String to) {
        this.activity = activity;
        todayDate = getCurrentDay();
        previousDate = getPreviousWeek();
        query = from + "_" + to;
        url = "https://free.currencyconverterapi.com/api/v5/convert?q=" + query + "&compact=ultra&date=" + previousDate + "&endDate=" + todayDate;
    }

    // endregion

    // region: methods

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpsURLConnection con;
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

            JSONObject obj = new JSONObject(sb.toString()).getJSONObject(query);
            List<String> list = new ArrayList<String>();
            Iterator<String> iter = obj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    list.add(obj.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < list.size(); i++) {
                Log.d("DEBUG", list.get(i));
            }
            lastOutput = list;
            success = true;

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        //Log.d("DEBUG", "Holy shit, it worked");
        Intent intent = new Intent("API_HISTORY_COMPLETED");
        intent.putExtra("success", success);
        System.out.println(" ------------------- BROADCAST");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        System.out.println(" ------------------- END");
    }

    private String getCurrentDay() {
        Date dateInstance = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInstance);
        Date date = cal.getTime();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return timeStamp;
    }

    private String getPreviousWeek() {
        Date dateInstance = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInstance);
        cal.add(Calendar.DATE, -8);
        Date dateBefore30Days = cal.getTime();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(dateBefore30Days);
        return timeStamp;
    }

    // endregion

    // region: getters & setters

    /**
     * Gets last output.
     *
     * @return the last output
     */
    public static List<String> getLastOutput() {
        return lastOutput;
    }

    // endregion

}
