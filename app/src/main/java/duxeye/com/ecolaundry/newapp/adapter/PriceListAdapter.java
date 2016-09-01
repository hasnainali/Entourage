package duxeye.com.ecolaundry.newapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import duxeye.com.ecolaundry.newapp.dao.ServicesDAO;
import duxeye.com.ecolaundry.newapp.R;

/**
 * Created by Duxeye on 17-11-2015.
 */
public class PriceListAdapter extends BaseAdapter {
    private ArrayList<ServicesDAO> arrayList;
    private LayoutInflater inflater;
    private ViewHolder holder;

    public PriceListAdapter(Activity mActivity, ArrayList<ServicesDAO> arrayList) {
        this.arrayList = arrayList;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.price_list_row, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(arrayList.get(position).getServicesTitle());
        holder.descTextView.setText(arrayList.get(position).getDescription());
        holder.thumbnailImageView.setImageResource(arrayList.get(position).getResourceID());

        return convertView;
    }

    class ViewHolder {
        private TextView titleTextView;
        private TextView descTextView;
        private ImageView thumbnailImageView;

        public ViewHolder(View mView){
            titleTextView = (TextView) mView.findViewById(R.id.tv_title);
            descTextView = (TextView) mView.findViewById(R.id.tv_artist);
            thumbnailImageView = (ImageView) mView.findViewById(R.id.iv_thumbnail);
        }
    }
}
