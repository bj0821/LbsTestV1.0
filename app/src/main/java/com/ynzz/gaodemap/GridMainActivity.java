package com.ynzz.gaodemap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.ynzz.JsonTestActivity;
import com.ynzz.express.activity.ExpressActivity;
import com.ynzz.gaodemap.adapter.Icon;
import com.ynzz.gaodemap.adapter.MyAdapter;
import com.ynzz.gaodemap.map.GaoDeLocationActivity;
import com.ynzz.gaodemap.map.LbsTextActivity;
import com.ynzz.gaodemap.map.PoiSearchActivity;

import java.util.ArrayList;

public class GridMainActivity extends AppCompatActivity {
    private Context mContext;
    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_main);
        mContext = GridMainActivity.this;
        grid_photo = (GridView) findViewById(R.id.grid_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.drawable.app_conference, "列表"));
        mData.add(new Icon(R.drawable.app_mapattend, "定位"));
        mData.add(new Icon(R.drawable.app_notification, "搜索"));
        mData.add(new Icon(R.drawable.carused, "GPS信息"));
        mData.add(new Icon(R.drawable.vehiclereturn, "物流"));
        mData.add(new Icon(R.drawable.vehiclem, "学生列表"));
        mData.add(new Icon(R.drawable.workovertime, "暂无"));

        mAdapter = new MyAdapter<Icon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, Icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };

        grid_photo.setAdapter(mAdapter);

        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(GridMainActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(GridMainActivity.this, GaoDeLocationActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(GridMainActivity.this, PoiSearchActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(GridMainActivity.this, LbsTextActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(GridMainActivity.this, ExpressActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(GridMainActivity.this, JsonTestActivity.class));
                        break;
                    default:
                        Toast.makeText(mContext, "你点击了未设置项", Toast.LENGTH_SHORT).show();
                        break;
                }

                //Toast.makeText(mContext, "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(GridMainActivity.this, MainActivity.class));
            }
        });

    }
}
