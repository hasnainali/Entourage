package duxeye.com.entourage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.customViews.CustomTextView;
import duxeye.com.entourage.model.School;

/**
 * Created by Ondoor (Hasnain) on 7/21/2016.
 */
public class SearchSchoolAdapter extends RecyclerView.Adapter<SearchSchoolAdapter.ViewHolder> {
    private static final String TAG = SearchSchoolAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<School> arrayList;
    private View.OnClickListener mOnClickListener;

    public SearchSchoolAdapter(ArrayList<School> arrayList, View.OnClickListener mOnClickListener) {
        this.arrayList = arrayList;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.search_school_row, parent, false);
        view.setOnClickListener(mOnClickListener);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.addressTextView.setText(arrayList.get(position).getOrgName());

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView addressTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            addressTextView = (CustomTextView) itemView.findViewById(R.id.tv_address);

        }
    }

    public void clearRecyclerData(ArrayList<School> schools){
        try{
            arrayList.clear();
            arrayList.addAll(schools);
            notifyDataSetChanged();

        }catch (Exception e){
            Log.e(TAG,"Error in clearRecyclerData: "+e);
        }

    }

}
