package lincyu.table;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    Button btn_connect,bt_test;
    EditText et_password,et_account;
    TextView tv_state,tv_myip;
    int connect=0;
    Thread socketServerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        btn_connect =(Button)findViewById(R.id.btn_connect);
        et_password=(EditText)findViewById(R.id.et_passworld);
        et_account=(EditText)findViewById(R.id.et_account);
        tv_state=(TextView)findViewById(R.id.tv_state);
        tv_myip=(TextView)findViewById(R.id.tv_myIP);
        bt_test=(Button)findViewById(R.id.bt_test);

        btn_connect.setOnClickListener(connectListener);
        bt_test.setOnClickListener(bt_test_CL);

        tv_myip.setText(getIpAddress());

         socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();


    }

    //按鈕監聽
    private View.OnClickListener connectListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            SocketClientThread SocketClientThread = new SocketClientThread();
            SocketClientThread.start();
            //沒加的話只能一次
            Thread changeThread =new Thread(new changeThread());
            changeThread.start();

        }
    };
    private View.OnClickListener bt_test_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Main2Activity.class);
            startActivity(intent);
        }
    };

    //菜單設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_use:


                break;
            case R.id.action_exit:


                break;


        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    //拿取 IP
    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    //Server端
    private class SocketServerThread extends Thread {
        String cleint_msg;

        @Override
        public void run() {
            ServerSocket ss =null;
            Socket s =null;
            DataInputStream din = null;
            DataOutputStream dout =null;
            try{
                ss= new ServerSocket(8888);
                System.out.println("以監聽8888阜");
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("出事拉1");
            }
            while(true){
                try{
                    System.out.println("測試１１１１");
                    s=ss.accept();
                    System.out.println("測試");
                    din = new DataInputStream(s.getInputStream());
                    dout = new DataOutputStream(s.getOutputStream());
                    cleint_msg = din.readUTF(); //這是裡傳來的訊息
                    dout.writeUTF("MainActivityServer");
                    //UI更新
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tv_state.setText(cleint_msg);
                        }
                    });


                }
                catch(Exception e){
                    System.out.println("出事拉2");
                }
                finally{
                    try{
                        System.out.println("測試222");
                        if(dout !=null){
                            dout.close();
                        }
                        if(din !=null){
                            din.close();
                        }
                        if(s != null){
                            s.close();
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        System.out.println("出事拉3");
                    }
                }
            }
        }

    }

    //Client端
    private class SocketClientThread extends Thread {
        String server_msg,server2_msg=null;
        @Override
        public void run() {
            Socket s = null;
            String ip="";
            ip=et_account.getText().toString();
            DataOutputStream dout = null;
            DataInputStream din =null;
            try{
                s = new Socket(ip,8888);
                dout = new DataOutputStream(s.getOutputStream());
                din = new DataInputStream(s.getInputStream());
                dout.writeUTF("Hi,Client");
               server_msg = din.readUTF(); //這是裡傳來的訊息
                server2_msg=server_msg;
                if(server2_msg!=null){
                    connect=1;
                    server2_msg=null;
                }
                //UI更新
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tv_state.setText(server_msg);

                    }
                });



            }
            catch (Exception e){
                e.getStackTrace();
            }
            finally {
                try{
                    if(dout !=null){
                        dout.close();
                    }
                    if(din !=null){
                        din.close();
                    }
                    if(s != null){
                        s.close();
                    }
                }catch (Exception e){
                    e.getStackTrace();
                }
            }


        }
    }
    //確認接收 轉換介面 傳遞IP
    private class changeThread extends Thread {

        @Override
        public void run() {
            while(true) {
                if (connect == 1) {
                    String ip = et_account.getText().toString();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Main2Activity.class);
                    intent.putExtra("IP",ip);
                    startActivity(intent);
                    connect = 0;
                    break;



                }
            }
        }
    }
}
