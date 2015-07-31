package gtrj.shbz.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import gtrj.shbz.R;
import gtrj.shbz.util.ContextString;
import gtrj.shbz.util.OkHttpUtil;


public class LoginActivity extends Activity {
    public LinearLayout loginActivity;
    private BootstrapEditText username;
    private BootstrapEditText password;
    private BootstrapButton loginBtn;
    private ProgressBarCircularIndeterminate loading;
    private Activity loginContext;

    private BootstrapEditText ipAddress;
    SharedPreferences preferences;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        loginContext = this;

        loginActivity = (LinearLayout) findViewById(R.id.login);
        loginActivity.setBackgroundResource(R.drawable.bg);

        loading = (ProgressBarCircularIndeterminate) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        username = (BootstrapEditText) findViewById(R.id.username);
        password = (BootstrapEditText) findViewById(R.id.password);

        //≈‰÷√IPµÿ÷∑
        ipAddress = (BootstrapEditText) findViewById(R.id.ip_address);
        preferences = getSharedPreferences("SHBZ", 0);
        s = preferences.getString("ipAddress", "");
        ipAddress.setText(s);

        loginBtn = (BootstrapButton) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            editable(false);
            Thread t = new Thread(() -> {
                boolean loginSuccess = loginSuccess();
                if (loginSuccess) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ipAddress", ipAddress.getText().toString());
                    editor.commit();

                    Intent intent = new Intent();
                    intent.setClass(loginContext, MainActivity.class);
                    startActivity(intent);
                    loginContext.finish();
                } else {
                    Message msg = msgHandler.obtainMessage();
                    msg.arg1 = 1;
                    msgHandler.sendMessage(msg);
                }
            });
            t.start();
        });
        String isSessionOutOfTime = getIntent().getStringExtra("isSessionOutOfTime");
        if (isSessionOutOfTime != null && "1".equals(isSessionOutOfTime)) {
            Toast.makeText(this, "µ«¬Ω≥¨ ±£¨«Î÷ÿ–¬µ«¬º", Toast.LENGTH_LONG).show();
        }
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Toast.makeText(getApplicationContext(), "µ«¬º ß∞‹£¨’ÀªßªÚ√‹¬Î¥ÌŒÛ", Toast.LENGTH_SHORT).show();
                    editable(true);
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "«Î≈‰÷√IPµÿ÷∑", Toast.LENGTH_SHORT).show();
                    editable(true);
                    break;
                default:
                    break;
            }
        }
    };

    private boolean loginSuccess() {
        if (!ipAddress.getText().toString().equals("")) {
            ContextString.SERVER = "http://" + ipAddress.getText().toString() + "/SHBZ/";
        } else {
            Message msg = msgHandler.obtainMessage();
            msg.arg1 = 3;
            msgHandler.sendMessage(msg);
            return false;
        }

        Map<String, String> map = new HashMap<>();
        map.put("userName", username.getText().toString());
        map.put("userPwd", password.getText().toString());
        try {
            //String result = HttpClientUtil.getData(ContextString.LOGIN, map);
            //∏ƒ”√okhttp
            String result = OkHttpUtil.Post(ContextString.LOGIN, map);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> resultMap = gson.fromJson(result, type);
            if (resultMap != null && resultMap.get("IsLogin") != null && resultMap.get("IsLogin").equals("0")) {
                SharedPreferences preferences = getSharedPreferences("SHBZ", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userName", username.getText().toString());
                editor.apply();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void editable(boolean flag) {
        username.setEnabled(flag);
        password.setEnabled(flag);
        loginBtn.setEnabled(flag);
        if (flag) {
            loading.setVisibility(View.INVISIBLE);
        } else {
            loading.setVisibility(View.VISIBLE);
        }
    }

}
