
package vn.edu.imic.rxjavacurrency.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Market {

    @SerializedName("market")
    private String mMarket;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("volume")
    private Double mVolume;

    public String getMarket() {
        return mMarket;
    }

    public void setMarket(String market) {
        mMarket = market;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public Double getVolume() {
        return mVolume;
    }

    public void setVolume(Double volume) {
        mVolume = volume;
    }

}
