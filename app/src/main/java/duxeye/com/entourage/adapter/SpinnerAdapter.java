package duxeye.com.entourage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rey.material.widget.RadioButton;
import java.util.ArrayList;
import duxeye.com.entourage.R;
import duxeye.com.entourage.model.State;

/**
 * Created by User on 20-07-2016.
 */
public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.ItemHolder> {
    private ArrayList<State> arrayList;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private static int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(State item,int selectedPosition);
    }


    public SpinnerAdapter(Context context, int selectedPosition, ArrayList<State> arrayList, OnItemClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.selectedPosition = selectedPosition;
        this.onItemClickListener = listener;
    }

    @Override
    public SpinnerAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_span, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpinnerAdapter.ItemHolder holder, final int position) {
        holder.bind(arrayList.get(position), onItemClickListener);
        holder.mRadioButton.setChecked(position == selectedPosition);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private RadioButton mRadioButton;

        public ItemHolder(View itemView) {
            super(itemView);
            mRadioButton = (RadioButton) itemView.findViewById(R.id.iv_thumb);
        }

        public void bind(final State rowItem, final OnItemClickListener listener) {
            mRadioButton.setText(rowItem.getName());

            mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, arrayList.size());
                    listener.onItemClick(rowItem,selectedPosition);
                }
            });

        }

    }
}
