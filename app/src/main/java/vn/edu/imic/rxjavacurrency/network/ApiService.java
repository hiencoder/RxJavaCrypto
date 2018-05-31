package vn.edu.imic.rxjavacurrency.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.imic.rxjavacurrency.model.Crypto;

/**
 * Created by MyPC on 31/05/2018.
 */

public interface ApiService {
    /*Để sử dụng được RxJava trong Retrofit ta cần thực hiện 2 thay đổi sau:
    * 1. Thêm RxJava vào trong Retrofit.Builder.
    * 2. Sử dụng Observable trong interface thay vì sử dụng Call.*/
    @GET("{coin}-usd")
    Observable<Crypto> getCurrency(@Path("coin") String coin);
}
