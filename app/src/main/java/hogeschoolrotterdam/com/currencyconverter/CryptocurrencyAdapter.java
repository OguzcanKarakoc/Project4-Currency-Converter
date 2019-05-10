package hogeschoolrotterdam.com.currencyconverter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hogeschoolrotterdam.com.currencyconverter.API.Cryptocurrency;

public class CryptocurrencyAdapter extends RecyclerView.Adapter<CryptocurrencyAdapter.ViewHolder> {

    private Context context;
    private List<Cryptocurrency> list;

    public CryptocurrencyAdapter(Context context, List<Cryptocurrency> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cryptocurrency_single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cryptocurrency crypto = list.get(position);

        holder.header.setText(crypto.getFullName());
        holder.footer.setText(String.format("EUR: %s | USD: %s", crypto.getEUR(), crypto.getUSD()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView header, footer;

        public ViewHolder(View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.txtHeader);
            footer = itemView.findViewById(R.id.txtFooter);
        }
    }

}
