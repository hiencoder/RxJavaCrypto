package vn.edu.imic.rxjavacurrency.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.rxjavacurrency.R;
import vn.edu.imic.rxjavacurrency.adapter.CryptoAdapter;
import vn.edu.imic.rxjavacurrency.model.Crypto;
import vn.edu.imic.rxjavacurrency.model.Market;
import vn.edu.imic.rxjavacurrency.network.APIClient;
import vn.edu.imic.rxjavacurrency.network.ApiService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiService apiService;
    private Unbinder unbinder;
    private CryptoAdapter cryptoAdapter;
    private List<Market> listMarket = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_currency)
    RecyclerView rvCrypto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        configToolbar();
        whiteNoticationBar(rvCrypto);
        configRecyclerView();

        /*Lấy ra mã bitcoin(btc)*/
        apiService = APIClient.getClient().create(ApiService.class);
        Observable<List<Market>> btcObservable = apiService.getCurrency("btc")
                /*Chuyển đổi kết quả của map thành các luồng Observable*/
                .map(result -> Observable.fromIterable(result.getTicker().getMarkets()))
                /*FlatMap làm việc với từng phần tử, do đó chuyển đổi ArrayList thành
                 * các phần tử đơn lẻ.*/
                .flatMap(x -> x)
                /*Filter để thay đổi các response*/
                .filter(y -> {
                    y.coinName = "btc";
                    return true;
                })
                /*toList chuyển đổi kết quả của flatMap thành list*/
                .toList()
                /*toObservable chuyển dổi thành các luồng Observable*/
                .toObservable();

        Observable<List<Market>> ethObservable = APIClient.getClient().create(ApiService.class).getCurrency("eth")
                .map(result -> Observable.fromIterable(result.getTicker().getMarkets()))
                .flatMap(x -> x).filter(y -> {
                    y.coinName = "eth";
                    return true;
                }).toList().toObservable();

        Observable.merge(btcObservable, ethObservable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Market>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Market> markets) {
                        Log.d(TAG, "onNext: ");
                        listMarket.addAll(markets);
                        cryptoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

        Observable<List<Market>> bitcObservable = apiService.getCurrency("btc")
                /*Phát ra list market*/
                .map(new Function<Crypto, List<Market>>() {
                    @Override
                    public List<Market> apply(Crypto crypto) throws Exception {
                        return crypto.getTicker().getMarkets();
                    }
                })
                .flatMap(new Function<List<Market>, ObservableSource<Market>>() {
                    @Override
                    public ObservableSource<Market> apply(List<Market> markets) throws Exception {
                        return Observable.fromArray(markets.toArray(new Market[markets.size()]));
                    }
                })
                .filter(new Predicate<Market>() {
                    @Override
                    public boolean test(Market market) throws Exception {
                        market.coinName = "btc";
                        return true;
                    }
                }).toList().toObservable();
    }

    private void configRecyclerView() {
        cryptoAdapter = new CryptoAdapter(this, listMarket);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCrypto.setItemAnimator(new DefaultItemAnimator());
        rvCrypto.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvCrypto.setLayoutManager(layoutManager);
        rvCrypto.setAdapter(cryptoAdapter);
    }

    /**
     * @param view
     */
    private void whiteNoticationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    /**
     *
     */
    private void configToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @param markets
     */
    private void handleResult(List<Market> markets) {
        listMarket.addAll(markets);
        cryptoAdapter.notifyDataSetChanged();
    }


    private void handleError(Throwable t) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        unbinder.unbind();
        super.onDestroy();
    }
}
