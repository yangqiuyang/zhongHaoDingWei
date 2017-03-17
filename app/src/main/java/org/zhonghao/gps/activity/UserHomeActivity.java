package org.zhonghao.gps.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyApplication;

public class UserHomeActivity extends AppCompatActivity {
    TextView userName, email, company, telephone;
    ImageView imageView;
    Button button;
    Handler handler;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        setView();
        myApplication = (MyApplication) this.getApplicationContext();
        handler = myApplication.getMyHandler();
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_home_toolbar);
//        toolbar.addView(View.inflate(MapActivity.this,R.layout.list_layout,null),0);
        toolbar.setTitle("个人中心");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPreferences pref;
                SharedPreferences.Editor editor;
                pref = PreferenceManager.getDefaultSharedPreferences(UserHomeActivity.this);
               editor = pref.edit();
                editor.putBoolean("automatic", false);
                editor.commit();
                handler.sendEmptyMessage(3);

            }
        });
    }

    private void setView() {
        userName = (TextView) findViewById(R.id.tv_userhome_username);
        email = (TextView) findViewById(R.id.tv_userhome_email);
        company = (TextView) findViewById(R.id.tv_userhome_company);
        telephone = (TextView) findViewById(R.id.tv_userhome_phonenum);
        button  = (Button) findViewById(R.id.btn_backLogin);
        userName.setText(MyApplication.myUserInnfo.getUserName());
        email.setText(MyApplication.myUserInnfo.getEmail());
        company.setText(MyApplication.myUserInnfo.getCompany());
        telephone.setText(MyApplication.myUserInnfo.getTelphone());
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
