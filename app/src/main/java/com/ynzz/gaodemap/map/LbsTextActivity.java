package com.ynzz.gaodemap.map;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.ynzz.gaodemap.R;
import com.ynzz.gaodemap.utils.HttpUtil;
import com.ynzz.gaodemap.utils.SIMCardInfo;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LbsTextActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {
    private TextView position;
    public static AMapLocationClient mLocationClient = null;
    public static AMapLocationClientOption mLocationOption = null;
    private LocationSource.OnLocationChangedListener mListener;
    private StringBuilder currentPosition;

    // 标识首次定位
    private boolean isFirstLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new AMapLocationClient(getApplicationContext());
        setContentView(R.layout.activity_lbs_text);
        position = (TextView) findViewById(R.id.positon);
        initGaoDeMap();
    }

    /**
     * 初始化高德地图
     */
    public void initGaoDeMap() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置高德地图定位回调监听
        mLocationClient.setLocationListener(this);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
        // 设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        // SDK默认采用连续定位模式，时间间隔2000ms
        // 设置定位间隔，单位毫秒，默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        // 设置定位同时是否需要返回地址描述
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否强制刷新WIFI，默认为强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        // 设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为false，不允许模拟位置。
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位请求超时时间，默认为30秒
        // 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(50000);
        // 设置是否开启定位缓存机制
        // 缓存机制默认开启，可以通过以下接口进行关闭。
        // 当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
        // 关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        // 设置是否只定位一次，默认为false
        mLocationOption.setOnceLocation(false);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动高德地图定位
        mLocationClient.startLocation();
    }
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
            currentPosition = new StringBuilder();
            currentPosition.append("--------------------------------------------------------------\n");
            currentPosition.append("纬度：").append(aMapLocation.getLatitude()).
                    append("\n");
            currentPosition.append("经度：").append(aMapLocation.getLongitude()).
                    append("\n");
            currentPosition.append("海拔：").append(aMapLocation.getAltitude()).
                    append("\n");
        currentPosition.append("速度：").append(aMapLocation.getSpeed()+"米/秒").append("\n");
        currentPosition.append("方向：").append(aMapLocation.getBearing()).append("\n");
        currentPosition.append("精度：").append(aMapLocation.getAccuracy()+"米").append("\n");
        currentPosition.append("AOI ：").append(aMapLocation.getAoiName()).append("\n");
        currentPosition.append("地址：").append(aMapLocation.getAddress()).append("\n");
        //currentPosition.append("楼层：").append(aMapLocation.getFloor()).append("\n");
        currentPosition.append("城市编码：").append(aMapLocation.getCityCode()).append("\n");
        currentPosition.append("地区编码：").append(aMapLocation.getAdCode()).append("\n");
        currentPosition.append("提供者：").append(aMapLocation.getProvider()).append("\n");
            currentPosition.append("定位方式：");
            if (aMapLocation.getLocationType()==aMapLocation.LOCATION_TYPE_GPS){
               // if (aMapLocation.getGpsAccuracyStatus()!=-1){
                currentPosition.append("GPS");
            //}else if (aMapLocation.getGpsAccuracyStatus()==-1){
            }else if (aMapLocation.getLocationType()!=aMapLocation.LOCATION_TYPE_GPS){
                currentPosition.append("网络");
            }
            currentPosition.append("").append("\n");
         //   currentPosition.append("--------------------------------------------------------------").append("\n");
            currentPosition.append("当前时间：").append(aMapLocation.getTime()).append("\n");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String newtime = formatter.format(aMapLocation.getTime());
            currentPosition.append("转换时间：").append(newtime).append("\n");
            currentPosition.append("--------------------------------------------------------------").append("\n");
            String srvcName = Context.TELEPHONY_SERVICE;
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
            String imei = telephonyManager.getDeviceId();
            String imsi = telephonyManager.getSubscriberId();
            String number = telephonyManager.getLine1Number();
            String simOperator = telephonyManager.getSimOperator();//返回SIM卡运营商的单个核细胞数+冶
            String simSerialNumber = telephonyManager.getSimSerialNumber();//返回SIM卡的序列号
            currentPosition.append("IMEI：").append(imei).append("\n");
            currentPosition.append("IMSI：").append(imsi).append("\n");
            SIMCardInfo siminfo = new SIMCardInfo(LbsTextActivity.this);
            currentPosition.append("手机号：").append(siminfo.getNativePhoneNumber()).append("\n");
            currentPosition.append("运营商：").append(siminfo.getProvidersName()).append("\n");
            currentPosition.append("SIM卡序：").append(simSerialNumber).append("\n");
            currentPosition.append("--------------------------------------------------------------");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    position.setText(currentPosition);
                }
            });
        try {
            final Map<String,String> map = new HashMap<String, String>();
            map.put("longitude", aMapLocation.getLongitude()+"");
            map.put("latitude", aMapLocation.getLatitude()+"");
//            try {
//                URL url = new URL("http://www.pool.ntp.org/zh/");
//                URLConnection uc=url.openConnection();//生成连接对象
//                uc.connect(); //发出连接
//                long time =uc.getDate(); //取得网站日期时间
//                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String newtime2 = formatter1.format(time);
//                map.put("time",newtime2+"");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            map.put("time",aMapLocation.getAdCode());
            map.put("mac", imei+"");
            map.put("remark",imsi+"");
            map.put("serialnumber", simSerialNumber);
            if (aMapLocation.getLongitude()!=0.0 && aMapLocation.getLatitude()!=0.0){
                final String url = HttpUtil.BASE_URL+"processing";
                Button uploadgps = (Button)findViewById(R.id.uploadgps);
                uploadgps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            HttpUtil.postRequest(url, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }
    @Override
    public void deactivate() {
        mListener = null;
    }
}
