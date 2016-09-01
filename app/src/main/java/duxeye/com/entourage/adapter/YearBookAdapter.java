package duxeye.com.entourage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.model.YearBook;

/**
 * Created by User on 23-07-2016.
 */
public class YearBookAdapter extends RecyclerView.Adapter<YearBookAdapter.MyViewHolder> {
    private static String TAG = YearBookAdapter.class.getSimpleName();
    private ArrayList<YearBook> mArrayList;
    private ItemClickListener onItemClickListener;

    public interface ItemClickListener {
        void onYearBookClick(YearBook yearBook);
    }



    public YearBookAdapter(ArrayList<YearBook> mArrayList,ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_year_book, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mArrayList.get(position), onItemClickListener);

    }


    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView yearBookNameTextView;

        public MyViewHolder(View view) {
            super(view);
            yearBookNameTextView = (TextView) view.findViewById(R.id.tv_name);
        }


        public void bind(final YearBook yearBook, final ItemClickListener onItemClickListener) {
            yearBookNameTextView.setText(yearBook.getYearBookName());
            yearBookNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onYearBookClick(yearBook);
                }
            });
        }
    }
}