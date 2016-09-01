package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import duxeye.com.ecolaundry.newapp.R;

public class TestimonialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial);

        
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestimonialActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
