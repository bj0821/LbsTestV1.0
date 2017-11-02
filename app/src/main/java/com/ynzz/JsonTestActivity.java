package com.ynzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzz.gaodemap.R;
import com.ynzz.jsontest.Student;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTestActivity extends AppCompatActivity {
    ListView listview;
    Button searchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_test);
        listview = (ListView)findViewById(R.id.studentListViewId);
        searchBtn = (Button)findViewById(R.id.searchStudentBtnId);

        searchBtn.setOnClickListener(new BtnOnClickListener());
    }
    class BtnOnClickListener implements View.OnClickListener
    {
        //按钮的点击事件 请求服务器获得数据并将数据现在listview中
        public void onClick(View v) {
            showData();
        }
    }
    /**
     * 显示数据
     */
    public void showData()
    {
        String data = getResponseData();
        //将数据进行解析获得数据对象
        if(!data.equals(""))
        {
            //将json 中的数据进行解析
            Gson gson = new Gson();
            List<Student> list = gson.fromJson(data, new TypeToken<List<Student>>() {
            }.getType());
            //adapter数据
            List<Map<String,String>> dataList  = new ArrayList<Map<String,String>>();
            for(Student stu:list)
            {
                Map<String,String> map = new HashMap<String, String>();
                map.put("name",stu.getName());
                map.put("sex", stu.getSex()+"");
                dataList.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(this,dataList,
                    android.R.layout.simple_list_item_2,new String[]{"name","age"},new int[]{android.R.id.text1,android.R.id.text2});
            listview.setAdapter(adapter);

        }

    }

    /**
     * 获得请求的数据
     * @return 返回服务器响应的数据
     */
    private String getResponseData() {
        //请求网络
        String urlStr = "http://192.168.1.117:8080/JsonWeb/SendStudentInfoServlet";
        String responseData="";
        try {
            URL objUrl = new URL(urlStr);
            URLConnection connection = objUrl.openConnection();
            connection.connect();
            //获得服务器响应的数据
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(connection.getInputStream()));
            //数据
            String data = null;

            while((data=in.readLine())!=null)
            {
                responseData+=data;
            }
            in.close();
        } catch (Exception e) {
            System.out.println("internet connection error");
        }
        return responseData;
    }
}
