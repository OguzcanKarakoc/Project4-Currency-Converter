package hogeschoolrotterdam.com.currencyconverter.API;

/**
 * The type Country.
 */
public class Country {

    // region: fields

    private String currencyID;
    private String currencyName;
    private String id;
    private String name;

    // endregion

    // region: getters & setters

    /**
     * Gets currency id.
     *
     * @return the currency id
     */
    public String getCurrencyID() {
        return currencyID;
    }

    /**
     * Gets currency name.
     *
     * @return the currency name
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets currency id.
     *
     * @param currencyID the currency id
     */
    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    /**
     * Sets currency name.
     *
     * @param currencyName the currency name
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    // endregion

}
