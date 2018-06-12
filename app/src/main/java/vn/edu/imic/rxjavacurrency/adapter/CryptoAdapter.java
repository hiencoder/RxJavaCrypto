package vn.edu.imic.rxjavacurrency.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.edu.imic.rxjavacurrency.R;
import vn.edu.imic.rxjavacurrency.model.Market;

/**
 * Created by MyPC on 31/05/2018.
 */

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoHolder>{
    private Context context;
    private List<Market> listMarket;

    public CryptoAdapter(Context context, List<Market> listMarket) {
        this.context = context;
        this.listMarket = listMarket;
    }

    @NonNull
    @Override
    public CryptoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market,parent,false);
        return new CryptoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoHolder holder, int position) {
        Market market = listMarket.get(position);
        holder.bindMarket(market);
    }

    @Override
    public int getItemCount() {
        return listMarket.size();
    }

    public class CryptoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_market)
        TextView tvMarket;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_volume)
        TextView tvVolume;
        @BindView(R.id.cv_crypto)
        CardView cvCoin;
        public CryptoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindMarket(Market market){
            tvMarket.setText(market.getMarket());
            tvPrice.setText(market.getPrice());
            tvVolume.setText(market.getVolume() + "");
            if (market.coinName.equalsIgnoreCase("eth")){
                cvCoin.setCardBackgroundColor(Color.GRAY);
            }else {
                cvCoin.setCardBackgroundColor(Color.GREEN);
            }
        }
    }
}
