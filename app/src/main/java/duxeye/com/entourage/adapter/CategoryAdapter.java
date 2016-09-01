package duxeye.com.entourage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.model.Category;
import duxeye.com.entourage.model.YearBook;

/**
 * Created by User on 23-07-2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private static String TAG = CategoryAdapter.class.getSimpleName();
    private ArrayList<Category> mArrayList;
    private ItemClickListener onItemClickListener;

    public interface ItemClickListener {
        void onCategoryClick(Category mCategory);
    }



    public CategoryAdapter(ArrayList<Category> mArrayList, ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
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
        public TextView categoryNameTextView,countTextView;

        public MyViewHolder(View view) {
            super(view);
            categoryNameTextView = (TextView) view.findViewById(R.id.tv_name);
            countTextView = (TextView) view.findViewById(R.id.tv_count);
        }


        public void bind(final Category mCategory, final ItemClickListener onItemClickListener) {
            categoryNameTextView.setText(mCategory.getName());
            countTextView.setText(mCategory.getCount());

            categoryNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onCategoryClick(mCategory);
                }
            });
        }
    }
}