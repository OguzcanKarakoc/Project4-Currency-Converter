package hogeschoolrotterdam.com.currencyconverter.API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * The type Cryptocurrency list request.
 */
public class CryptocurrencyListRequest extends AsyncTask {

    // region: fields

    //private String urlCoinList = "https://min-api.cryptocompare.com/data/all/coinlist";
    private String urlCoinList;
    private Activity activity;
    private ProgressDialog prog;

    private static List<Cryptocurrency> result;
    private boolean success;
    private String[] symbolArray = {
            "BTC",
            "ETH",
            "XRP",
            "BCH",
            "EOS",
            "LTC",
            "XLM",
            "ADA",
            "TRX",
            "DASH"
    };
    private String[] nameArray = {
            "Bitcoin",
            "Ethereum",
            "Ripple",
            "Bitcoin Cash",
            "EOS",
            "LiteCoin",
            "Stellar",
            "Cardano",
            "TRON",
            "Dash"
    };

    // endregion

    // region: constructor

    public CryptocurrencyListRequest(Activity activity) {
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
        List<Cryptocurrency> cryptoList = new ArrayList<>();
        HttpsURLConnection con;
        try {
            for (int i=0; i < symbolArray.length; i++) {
                String fullName = nameArray[i];
                JSONObject data = doRequest(symbolArray[i]);
                Cryptocurrency model = new Cryptocurrency();
                model.setFullName(fullName);
                model.setSymbol(symbolArray[i]);
                model.setEUR(data.getString("EUR"));
                model.setUSD(data.getString("USD"));
                cryptoList.add(model);
            }
            success = true;
            result = cryptoList;

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            return "Error";
        } finally {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        prog.dismiss();
        Intent intent = new Intent("API_CRYPTOCURRENCY_REQUEST");
        intent.putExtra("success", success);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);

    }

    private String GetConversionValue(String cryptoSymbol, String outputSymbol) {
        String url = String.format("https://min-api.cryptocompare.com/data/price?fsym=%s&tsyms=%s", cryptoSymbol, outputSymbol);
        HttpsURLConnection con;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            JSONObject obj = new JSONObject(sb.toString());
            return obj.getString(outputSymbol);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            return "Error";
        }
    }

    private JSONObject doRequest(String symbol) {
        try {
            HttpsURLConnection con;
            URL u = new URL(String.format("https://min-api.cryptocompare.com/data/price?fsym=%s&tsyms=USD,EUR", symbol));
            con = (HttpsURLConnection) u.openConnection();
            con.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            JSONObject obj = new JSONObject(sb.toString());
            return obj;
        }
        catch (Exception e) {
            e.printStackTrace();
            JSONObject errorObj = new JSONObject();
            try {
                errorObj.put("USD", "Error");
                errorObj.put("EUR", "Error");
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                return null;
            }
            return null;
        }
    }

    // endregion

    // region: getters & setters



    public static List<Cryptocurrency> getResult() {
        return result;
    }

    // endregion

}

