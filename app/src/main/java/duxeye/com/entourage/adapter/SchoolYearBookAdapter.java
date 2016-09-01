package duxeye.com.entourage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.model.ResultFromDBSearchSchool;

/**
 * Created by User on 23-07-2016.
 */
public class SchoolYearBookAdapter extends RecyclerView.Adapter<SchoolYearBookAdapter.MyViewHolder> {
    private static String TAG = SchoolYearBookAdapter.class.getSimpleName();
    private ArrayList<ResultFromDBSearchSchool> mArrayList;
    private ItemClickListener onItemClickListener;
    private Activity mActivity;

    public interface ItemClickListener {
        void inItemClick(ResultFromDBSearchSchool mSearchSchool);
    }



    public SchoolYearBookAdapter(Activity mActivity, ArrayList<ResultFromDBSearchSchool> mArrayList, ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bd_yearbook_search_school, parent, false);
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


        public void bind(final ResultFromDBSearchSchool mSearchSchool, final ItemClickListener onItemClickListener) {
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