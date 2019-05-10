package hogeschoolrotterdam.com.currencyconverter.API;

/**
 * The type Cryptocurrency.
 */
public class Cryptocurrency {

    // region: fields

    private String FullName;
    private String Symbol;
    private String USD;
    private String EUR;

    // endregion

    // region: getters & setters

    /**
     * Gets full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return FullName;
    }

    /**
     * Sets full name.
     *
     * @param fullName the full name
     */
    public void setFullName(String fullName) {
        FullName = fullName;
    }

    /**
     * Gets symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return Symbol;
    }

    /**
     * Sets symbol.
     *
     * @param symbol the symbol
     */
    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    /**
     * Gets usd.
     *
     * @return the usd
     */
    public String getUSD() {
        return USD;
    }

    /**
     * Sets usd.
     *
     * @param USD the usd
     */
    public void setUSD(String USD) {
        this.USD = USD;
    }

    /**
     * Gets eur.
     *
     * @return the eur
     */
    public String getEUR() {
        return EUR;
    }

    /**
     * Sets eur.
     *
     * @param EUR the eur
     */
    public void setEUR(String EUR) {
        this.EUR = EUR;
    }

    // endregion


}
