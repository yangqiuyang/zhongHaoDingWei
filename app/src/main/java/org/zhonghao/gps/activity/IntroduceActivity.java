package org.zhonghao.gps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroduceActivity extends MyActivity {
    private View view1,view2;
    ViewPager viewPager;
    private List<View> viewList;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        viewPager = (ViewPager) findViewById(R.id.introduce);

        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.fragment_main,null);
        view2 = inflater.inflate(R.layout.fragment_end,null);
        button = (Button) view2.findViewById(R.id.btn_introduce);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);
        viewList.add(view2);
        //设置预加载页数限制
        viewPager.setOffscreenPageLimit(2);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }
        };
        viewPager.setAdapter(pagerAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroduceActivity.this,
                        LoginActivity.class));
                finish();
            }
        });

    }

}
