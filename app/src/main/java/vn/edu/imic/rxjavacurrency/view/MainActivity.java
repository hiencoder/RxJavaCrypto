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
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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
                .map(result -> Observable.fromIterable(result.getTicker().getMarkets()))
                .flatMap(x -> x).filter(y -> {
                    y.coinName = "btc";
                    return true;
                }).toList().toObservable();

        Observable<List<Market>> bitcObservable = apiService.getCurrency("btc")
                .map(new Function<Crypto, List<Market>>() {
                    @Override
                    public List<Market> apply(Crypto crypto) throws Exception {
                        return crypto.getTicker().getMarkets();
                    }
                })
                .flatMap(new Function<List<Market>, ObservableSource<Market>>() {
                    @Override
                    public ObservableSource<Market> apply(List<Market> markets) throws Exception {
                        return new Observable<Market>() {
                            @Override
                            protected void subscribeActual(Observer<? super Market> observer) {

                            }
                        };
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

    private void whiteNoticationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    private void configToolbar() {

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
