package org.zhonghao.gps.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.biz.Tools;

public class FirstActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        String versionName = sp.getString("versionName","1.0");
        //判断是否第一次登陆，第一次跳转欢迎页,否则跳转登录页
        boolean isFirst = true;
        try {
            String versionNameNow = Tools.getCurrentVersion(FirstActivity.this);
            if (versionName.equals(versionNameNow)){
                isFirst = false;
            }else {
                editor.putString("versionName",versionNameNow);
                editor.commit();

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Handler handler = new Handler();

        if (isFirst) {
            handler.postDelayed(new Runnable() {
                // 2秒之后执行
                @Override
                public void run() {
                    startActivity(new Intent(FirstActivity.this,
                            IntroduceActivity.class));
                    finish();
                }
            }, 1000);

        } else {
            // 停2秒后跳转到MapActivity

            handler.postDelayed(new Runnable() {
                // 2秒之后执行
                @Override
                public void run() {
                    startActivity(new Intent(FirstActivity.this,
                            LoginActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}
