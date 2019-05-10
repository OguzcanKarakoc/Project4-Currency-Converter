package hogeschoolrotterdam.com.currencyconverter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * The type History adapter.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    // region: fields

    private Context context;
    private List<HistoryItem> list;

    // endregion

    // region: constructor

    /**
     * Instantiates a new History adapter.
     *
     * @param context the context
     * @param list    the list
     */
    public HistoryAdapter(Context context, List<HistoryItem> list) {
        this.context = context;
        this.list = list;
    }

    // endregion

    // region: methods

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryItem history = list.get(position);

        holder.overview.setText(String.format("%s %s ► %s %s", history.getInputCurrency(), history.getInputValue(), history.getOutputCurrency(), history.getOutputValue()));
        holder.date.setText(history.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Overview.
         */
        public TextView overview, /**
         * The Date.
         */
        date;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);

            overview = itemView.findViewById(R.id.txtHistoryMain);
            date = itemView.findViewById(R.id.txtHistoryDate);
        }
    }

    // endregion

}
