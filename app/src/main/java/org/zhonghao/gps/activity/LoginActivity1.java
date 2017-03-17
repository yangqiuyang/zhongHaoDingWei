package org.zhonghao.gps.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.zhonghao.gps.R;
import org.zhonghao.gps.entity.DevicesInfo;
import org.zhonghao.gps.entity.NameDates;
import org.zhonghao.gps.utils.Code;

public class LoginActivity1 extends AppCompatActivity {
    ImageView img;
    Code code ;
    Button confirm;
    EditText name,password,code1;
    NameDates nameDates;
    DevicesInfo.LoginResponse loginRsponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        code= new Code();
        img = (ImageView) findViewById(R.id.img);
        name = (EditText) findViewById(R.id.et_name);
        password = (EditText) findViewById(R.id.et_password);
        code1 = (EditText) findViewById(R.id.et_code);
        confirm = (Button) findViewById(R.id.btn_dialog_cancel);

        img.setImageBitmap(code.getInstance().createBitmap());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageBitmap(code.getInstance().createBitmap());
                System.out.println("验证码："+code.getInstance().getCode().toLowerCase());

            }
        });
        System.out.println("验证码："+code.getInstance().getCode().toLowerCase());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (code.getInstance().getCode().toLowerCase().equals(code1.getText().toString().toLowerCase())){

                        Toast.makeText(LoginActivity1.this,"验证码正确",Toast.LENGTH_SHORT).show();
                        nameDates = new NameDates();
                        nameDates.setPassword(password.getText().toString().trim());
                        nameDates.setUsername(name.getText().toString().trim());
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
//                                loginRsponse = ServelBiz.LoginBiz(nameDates,LoginActivity1.this);
                            }
                        }.start();
//                        verifyUser(name.getText().toString().trim(),password.getText().toString().trim());
//                        Intent intent = new Intent(LoginActivity1.this,MainActivity1.class);
//                        startActivity(intent);

                }else {
                        Toast.makeText(LoginActivity1.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                        img.setImageBitmap(code.getInstance().createBitmap());
                        System.out.println("验证码："+code.getInstance().getCode().toLowerCase());
                }
            }
        });

    }

//    private void verifyUser(final String username, final String password1) {
//        nameDates = new NameDates();
//        nameDates.setUsername(username);
//        nameDates.setPassword(password1);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                loginRsponse = ServelBiz.LoginBiz(nameDates,LoginActivity1.this);
//
//            }
//        }).start();
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                loginRsponse = ServelBiz.LoginBiz(nameDates,LoginActivity1.this);
//            }
//        }.start();
        /**
         * 不用框架请求
         */
//        Thread thread = new Thread(new Runnable() {

//            public void run() {
//                Log.d("serve","code=xiancheng");
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL(" http://192.168.17.100:8080/NewGPSTrace/APPloginServlet");
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setReadTimeout(5000);
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("charset", "UTF-8");
//                    connection.setConnectTimeout(5000);
//                    connection.setDoInput(true);
//                    String name = "{'ResponseUserinfo':[{'userName':'"+ username+"','psssWord':'"+ password1+"'}]}";
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes(name);
//                    int a = connection.getResponseCode();
//                    String result = null;
//                    if (a == 200){
//                        result = connection.getResponseMessage();
//                        InputStream is = connection.getInputStream();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                        StringBuilder response = new StringBuilder();
//                        String line;
//                        while ((line = reader.readLine())!=null){
//                            response.append(line);
//                        }
//                        Log.d("serve", "run: "+response.toString());
//                        String jsonData = response.toString();
//                        parseJSONWithJSONObject(jsonData);
//
//
//                    }
//                    Log.d("serve","code="+a);
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                    Log.d("serve","error="+e.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("serve","error="+e.toString());
//                }
//            }
//        });
//        thread.start();
//    }

//
//    private void parseJSONWithJSONObject(String jsonData) {
//        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            JSONObject jsonObject1 = new JSONObject(jsonData);
//            JSONArray devices1 = (JSONArray)jsonObject1.get("devices");
//            String o = (String)devices1.get(0);
//            Log.d("test",o);
//            for (int i = 0;i < jsonArray.length();i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String State = jsonObject.getString("State");
//                ArrayList<String> devices = jsonObject.getString("devices");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
}
