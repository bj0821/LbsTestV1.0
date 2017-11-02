package com.ynzz.gaodemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ynzz.JsonTestActivity;
import com.ynzz.express.activity.ExpressActivity;
import com.ynzz.gaodemap.map.GaoDeLocationActivity;
import com.ynzz.gaodemap.map.LbsTextActivity;
import com.ynzz.gaodemap.map.PoiSearchActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        findViewById(R.id.id_main_gaode_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GaoDeLocationActivity.class));
            }
        });
        findViewById(R.id.id_main_gaode_search_poi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PoiSearchActivity.class));
            }
        });
        findViewById(R.id.id_main_gaode_textinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LbsTextActivity.class));
            }
        });
        findViewById(R.id.id_router_planning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ExpressActivity.class));
            }
        });
        findViewById(R.id.id_jsontest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(MainActivity.this, JsonTestActivity.class));
                startActivity(new Intent(MainActivity.this, GridMainActivity.class));
            }
        });
    }
}
