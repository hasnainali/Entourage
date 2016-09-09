package duxeye.com.entourage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.model.LadderYearBook;
import duxeye.com.entourage.model.SearchSchool;

/**
 * Created by User on 23-07-2016.
 */
public class SearchSchoolYearBookAdapter extends RecyclerView.Adapter<SearchSchoolYearBookAdapter.MyViewHolder> {
    private static String TAG = SearchSchoolYearBookAdapter.class.getSimpleName();
    private ArrayList<SearchSchool> mArrayList;
    private ItemClickListener onItemClickListener;
    private Activity mActivity;

    public interface ItemClickListener {
        void inItemClick(SearchSchool mSearchSchool);
    }



    public SearchSchoolYearBookAdapter(Activity mActivity, ArrayList<SearchSchool> mArrayList, ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_school, parent, false);
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
        private TextView schoolNameTextView;

        public MyViewHolder(View view) {
            super(view);
            schoolNameTextView = (TextView) view.findViewById(R.id.tv_search_school_name);
        }


        public void bind(final SearchSchool mSearchSchool, final ItemClickListener onItemClickListener) {
            schoolNameTextView.setText(mSearchSchool.getOrgName());

            schoolNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.inItemClick(mSearchSchool);
                }
            });
        }
    }
}