package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import duxeye.com.ecolaundry.newapp.adapter.ServicesListAdapter;
import duxeye.com.ecolaundry.newapp.dao.ServicesDAO;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class ServicesActivity extends Activity {
    private static final String TAG = ServicesActivity.class.getSimpleName();
    private ArrayList<ServicesDAO> arrayList;
    private ListView mListView;
    private ServicesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        arrayList = Utility.getServicesContent();
        init();

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ServicesActivity.this.finish();
             }
         });

        adapter = new ServicesListAdapter(ServicesActivity.this,arrayList);
        mListView.setAdapter(adapter);
    }
    private void init(){
        mListView = (ListView) findViewById(R.id.iv_services);
    }




    @Override
    public void onBackPressed() {

    }
}
