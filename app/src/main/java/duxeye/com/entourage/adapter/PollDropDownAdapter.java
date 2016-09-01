package duxeye.com.entourage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.RadioButton;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.model.Answer;
import duxeye.com.entourage.model.Category;
import duxeye.com.entourage.model.PollData;

/**
 * Created by User on 20-07-2016.
 */
public class PollDropDownAdapter extends RecyclerView.Adapter<PollDropDownAdapter.ItemHolder> {
    private ArrayList<Answer> arrayList;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private static int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(Answer item, int selectedPosition);
    }


    public PollDropDownAdapter(Context context, int selectedPosition, ArrayList<Answer> arrayList, OnItemClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.selectedPosition = selectedPosition;
        this.onItemClickListener = listener;
    }

    @Override
    public PollDropDownAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_span, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PollDropDownAdapter.ItemHolder holder, final int position) {
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

        public void bind(final Answer rowItem, final OnItemClickListener listener) {
            mRadioButton.setText(rowItem.getAnswer());

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
