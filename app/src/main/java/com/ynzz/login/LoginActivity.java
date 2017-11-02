package com.ynzz.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ynzz.gaodemap.GridMainActivity;
import com.ynzz.gaodemap.MainActivity;
import com.ynzz.gaodemap.R;
import com.ynzz.gaodemap.map.GaoDeLocationActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static  String url="http://192.168.1.117:8080/Loginmysql/login.action?";
    private final String url_constant="http://192.168.1.117:8080/Loginmysql/login.action?";
    private EditText txUserName;
    private EditText txPassword;
    private Button btnLogin;
    private Button btnskip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置线程的策略
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        //设置虚拟机的策略
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                //.detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置初始化视图
        initView();
        //设置事件监听器方法
        setListener();
    }
    /**
     * 创建初始化视图的方法
     */
    private void initView() {
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnskip = (Button)findViewById(R.id.skip);
        txUserName=(EditText)findViewById(R.id.UserName);
        txPassword=(EditText)findViewById(R.id.textPasswd);
    }
    /**
     * 设置事件的监听器的方法
     */
    private void setListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=txUserName.getText().toString();
                Log.v("userName = ", userName);
                String password=txPassword.getText().toString();
                Log.v("passwd = ",password);
                loginRemoteService(userName,password);

            }
        });
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, GridMainActivity.class));
            }
        });
    }


    /**
     * 获取Struts2 Http 登录的请求信息
     * @param  userName
     * @param  password
     */
    public void loginRemoteService(String userName,String password){
        String result=null;
        String a = "登录成功！";
        try {

            //创建一个HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            //远程登录URL
            //下面这句是原有的
            //processURL=processURL+"userName="+userName+"&password="+password;
            url= url_constant+"userName="+userName+"&password="+password;
            Log.d("远程URL", url);
            //创建HttpGet对象
            HttpGet request=new HttpGet(url);
            //请求信息类型MIME每种响应类型的输出（普通文本、html 和 XML，json）。允许的响应类型应当匹配资源类中生成的 MIME 类型
            //资源类生成的 MIME 类型应当匹配一种可接受的 MIME 类型。如果生成的 MIME 类型和可接受的 MIME 类型不 匹配，那么将
            //生成 com.sun.jersey.api.client.UniformInterfaceException。例如，将可接受的 MIME 类型设置为 text/xml，而将
            //生成的 MIME 类型设置为 application/xml。将生成 UniformInterfaceException。
            request.addHeader("Accept","text/json");
            //获取响应的结果
            HttpResponse response =httpclient.execute(request);
            //获取HttpEntity
            HttpEntity entity=response.getEntity();
            //获取响应的结果信息
            String json = EntityUtils.toString(entity,"UTF-8");
            //JSON的解析过程
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                result=jsonObject.get("message").toString();
            }
            if(result==null){
                json="登录失败请重新登录";
            }
            //创建提示框提醒是否登录成功
            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
            final String finalResult = result;
            //String a = "登录成功！";
            builder.setTitle("提示")
                    .setMessage(result)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }).create().show();
               //
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
