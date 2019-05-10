package hogeschoolrotterdam.com.currencyconverter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * The type History json reader.
 */
public final class HistoryJSONReader {

    // region: fields

    private static boolean errorOccured = false;
    private static String fileName = "history.json";

    // endregion

    // region: methods

    /**
     * Init.
     *
     * @param context the context
     */
    public static void init(Context context) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            try {
                JSONArray emptyArray = new JSONArray();
                String res = emptyArray.toString();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
                outputStreamWriter.write(res);
                outputStreamWriter.close();
                Log.d("DEBUG", "New JSON created!");
            } catch (Exception e) {
                Log.d("Exception", e.toString());
                Toast.makeText(context, "An error occured while loading history", Toast.LENGTH_SHORT);
                errorOccured = true;
            }
        }
    }

    /**
     * Read string.
     *
     * @param context the context
     * @return the string
     */
    public static String Read(Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    /**
     * Add entry.
     *
     * @param context        the context
     * @param currencyInput  the currency input
     * @param currencyOutput the currency output
     * @param input          the input
     * @param output         the output
     * @param date           the date
     */
    public static void addEntry(Context context, String currencyInput, String currencyOutput, String input, String output, String date) {
        String str = Read(context);
        JSONArray array;

        try {
            array = new JSONArray(str);
            JSONObject obj = new JSONObject();
            obj.put("InputCurrency", currencyInput);
            obj.put("OutputCurrency", currencyOutput);
            obj.put("InputValue", input);
            obj.put("OutputValue", output);
            obj.put("Date", date);
            array.put(obj);
            String res = array.toString();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(res);
            outputStreamWriter.close();

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    /**
     * Clear history.
     *
     * @param context the context
     */
    public static void clearHistory(Context context) {
        try {
            JSONArray empty = new JSONArray();
            String res = empty.toString();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(res);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    // endregion

}
