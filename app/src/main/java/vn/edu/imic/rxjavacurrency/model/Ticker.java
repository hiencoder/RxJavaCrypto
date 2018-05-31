
package vn.edu.imic.rxjavacurrency.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Ticker {

    @SerializedName("base")
    private String mBase;
    @SerializedName("change")
    private String mChange;
    @SerializedName("markets")
    private List<Market> mMarkets;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("target")
    private String mTarget;
    @SerializedName("volume")
    private String mVolume;

    public String getBase() {
        return mBase;
    }

    public void setBase(String base) {
        mBase = base;
    }

    public String getChange() {
        return mChange;
    }

    public void setChange(String change) {
        mChange = change;
    }

    public List<Market> getMarkets() {
        return mMarkets;
    }

    public void setMarkets(List<Market> markets) {
        mMarkets = markets;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getTarget() {
        return mTarget;
    }

    public void setTarget(String target) {
        mTarget = target;
    }

    public String getVolume() {
        return mVolume;
    }

    public void setVolume(String volume) {
        mVolume = volume;
    }

}
