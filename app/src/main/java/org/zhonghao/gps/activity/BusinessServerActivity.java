package org.zhonghao.gps.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import org.zhonghao.gps.R;

public class BusinessServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_server);
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_home_toolbar);
//        toolbar.addView(View.inflate(MapActivity.this,R.layout.list_layout,null),0);
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
    }
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
//            Toast.makeText(UserHomeActivity.this,"dianjile "+x1,Toast.LENGTH_SHORT).show();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
//            Toast.makeText(UserHomeActivity.this,"dianjile "+x2,Toast.LENGTH_SHORT).show();
        }
        if (x2 - x1 > 160 && (y2-y1<50 || y1-y2<50)){
            finish();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}