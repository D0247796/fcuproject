package lincyu.table;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    Thread TCP_SocketServerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        btn_connect =(Button)findViewById(R.id.btn_connect);
        et_account=(EditText)findViewById(R.id.et_account);
        tv_state=(TextView)findViewById(R.id.tv_state);
        tv_myip=(TextView)findViewById(R.id.tv_myIP);
        bt_test=(Button)findViewById(R.id.bt_test);

        btn_connect.setOnClickListener(btn_connect_CL);
        bt_test.setOnClickListener(bt_test_CL);

//        tv_myip.setText(getIpAddress());

        TCP_SocketServerThread = new Thread(new TCP_SocketServerThread());
        TCP_SocketServerThread.start();


    }

    //按鈕監聽
    private View.OnClickListener btn_connect_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            TCP_SocketClientThread TCP_SocketClientThread = new TCP_SocketClientThread();
            TCP_SocketClientThread.start();
            //沒加的話只能一次
            Thread changeThread =new Thread(new changeThread());
            changeThread.start();
            tv_state.setText("連結中");

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
    private class TCP_SocketServerThread extends Thread {
        String cleint_msg;

        @Override
        public void run() {
            ServerSocket TCP_server_serversocket =null;
            Socket TCP_server_socket =null;
            DataInputStream din = null;
            DataOutputStream dout =null;
            try{
                TCP_server_serversocket= new ServerSocket(8888);
                System.out.println("以監聽8888阜");
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("出事拉1");
            }
            while(true){
                try{
                    TCP_server_socket=TCP_server_serversocket.accept();
                    din = new DataInputStream(TCP_server_socket.getInputStream());
                    dout = new DataOutputStream(TCP_server_socket.getOutputStream());
                    cleint_msg = din.readUTF(); //這是裡傳來的訊息
                    dout.writeUTF("User_startServer");
                    //UI更新
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tv_state.setText(cleint_msg);
                        }
                    });


                }
                catch(Exception e){
                  e.getStackTrace();
                }
                finally{
                    try{
                        if(dout !=null){
                            dout.close();
                        }
                        if(din !=null){
                            din.close();
                        }
                        if(TCP_server_socket != null){
                            TCP_server_socket.close();
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    //Client端
    private class TCP_SocketClientThread extends Thread {
        String server_msg,server_msg2=null;
        @Override
        public void run() {
            Socket TCP_client_socket = null;
            String ip="";
            ip=et_account.getText().toString();
            DataOutputStream TCP_client_dout = null;
            DataInputStream TCP_client_din =null;

            try{
                TCP_client_socket = new Socket(ip,8888);
                //UI更新
                TCP_client_dout = new DataOutputStream(TCP_client_socket.getOutputStream());
                TCP_client_din = new DataInputStream(TCP_client_socket.getInputStream());
                TCP_client_dout.writeUTF("User_Client");
               server_msg = TCP_client_din.readUTF(); //這是裡傳來的訊息
                server_msg2=server_msg;
                if(server_msg2.equals("Car_Server")){
                    connect=1;
                    server_msg2=null;
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
                    if(TCP_client_dout !=null){
                        TCP_client_dout.close();
                    }
                    if(TCP_client_din !=null){
                        TCP_client_din.close();
                    }
                    if(TCP_client_socket != null){
                        TCP_client_socket.close();
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
