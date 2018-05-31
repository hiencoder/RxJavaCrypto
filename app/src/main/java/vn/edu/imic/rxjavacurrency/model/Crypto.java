
package vn.edu.imic.rxjavacurrency.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Crypto {

    @SerializedName("error")
    private String mError;
    @SerializedName("success")
    private Boolean mSuccess;
    @SerializedName("ticker")
    private Ticker mTicker;
    @SerializedName("timestamp")
    private Long mTimestamp;

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

    public Ticker getTicker() {
        return mTicker;
    }

    public void setTicker(Ticker ticker) {
        mTicker = ticker;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Long timestamp) {
        mTimestamp = timestamp;
    }

}
