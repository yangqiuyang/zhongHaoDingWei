package org.zhonghao.gps.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyActivity;

public class AboutUsActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.about_us_toolbar);
//        toolbar.addView(View.inflate(MapActivity.this,R.layout.list_layout,null),0);
        toolbar.setTitle("关于");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        TextView version = (TextView) findViewById(R.id.tv_version);
        try {
            version.setText(org.zhonghao.gps.biz.Tools.getCurrentVersion(AboutUsActivity.this));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
