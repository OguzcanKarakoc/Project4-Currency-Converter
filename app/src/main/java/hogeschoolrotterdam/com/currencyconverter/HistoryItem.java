package hogeschoolrotterdam.com.currencyconverter;

/**
 * The type History item.
 */
public class HistoryItem {
    // region fields

    private String inputCurrency;
    private String outputCurrency;
    private String inputValue;
    private String outputValue;
    private String date;

    // endregion

    /**
     * Gets input currency.
     *
     * @return the input currency
     */
// region: getters & setters
    public String getInputCurrency() {
        return inputCurrency;
    }

    /**
     * Gets output currency.
     *
     * @return the output currency
     */
    public String getOutputCurrency() {
        return outputCurrency;
    }

    /**
     * Gets input value.
     *
     * @return the input value
     */
    public String getInputValue() {
        return inputValue;
    }

    /**
     * Gets output value.
     *
     * @return the output value
     */
    public String getOutputValue() {
        return outputValue;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets input currency.
     *
     * @param currency the currency
     */
    public void setInputCurrency(String currency) {
        inputCurrency = currency;
    }

    /**
     * Sets output currency.
     *
     * @param currency the currency
     */
    public void setOutputCurrency(String currency) {
        outputCurrency = currency;
    }

    /**
     * Sets input value.
     *
     * @param value the value
     */
    public void setInputValue(String value) {
        inputValue = value;
    }

    /**
     * Sets output value.
     *
     * @param value the value
     */
    public void setOutputValue(String value) {
        outputValue = value;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }
    // endregion



}
