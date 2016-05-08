package lincyu.book_server;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {
    Button btn_send;
    EditText et_outmsg;
    TextView tv_inmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_send = (Button)findViewById(R.id.button);
        et_outmsg = (EditText)findViewById(R.id.editText);
        tv_inmsg = (TextView)findViewById(R.id.textView2);

        btn_send.setOnClickListener(btn_send_CL);

    }
    private View.OnClickListener btn_send_CL = new View.OnClickListener() {
        Socket s = null;
        DataOutputStream dout = null;
        DataInputStream din =null;
        public void onClick(View v) {
            Thread1 thread1 = new Thread1();
            thread1.start();

//            HandlerThread handlerThread = new HandlerThread("jerome");
//            handlerThread.start();
//            try{
//                s = new Socket("36.233.118.77",8888);
//                dout = new DataOutputStream(s.getOutputStream());
//                din = new DataInputStream(s.getInputStream());
//                tv_inmsg.setText("Server msg:"+din.readUTF());
//            }
//            catch (Exception e){
//                e.getStackTrace();
//                btn_send.setText("new");
//            }
//            finally {
//                try{
//                    if(dout !=null){
//                        dout.close();
//                    }
//                    if(din !=null){
//                        din.close();
//                    }
//                    if(s != null){
//                        s.close();
//                    }
//                }catch (Exception e){
//                    e.getStackTrace();
//                    btn_send.setText("finally");
//                }
//            }




//            Handler  threadHandler;
//            threadHandler = new Handler(handlerThread.getLooper()) {
//
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                            try{
//                s = new Socket("36.233.118.77",8888);
//                dout = new DataOutputStream(s.getOutputStream());
//                din = new DataInputStream(s.getInputStream());
//            dout.writeUTF(et_outmsg.getText().toString());
//                tv_inmsg.setText("Server msg:"+din.readUTF());
//            }
//            catch (Exception e){
//                e.getStackTrace();
//                btn_send.setText("new");
//            }
//            finally {
//                try{
//                    if(dout !=null){
//                        dout.close();
//                    }
//                    if(din !=null){
//                        din.close();
//                    }
//                    if(s != null){
//                        s.close();
//                    }
//                }catch (Exception e){
//                    e.getStackTrace();
//                    btn_send.setText("finally");
//                }
//            }
//
//                }
//            };
        }

    };
    class Thread1 extends Thread{
        public void run(){
            Socket s = null;
            DataOutputStream dout = null;
            DataInputStream din =null;
            try{
                s = new Socket("192.168.1.115",8888);
                dout = new DataOutputStream(s.getOutputStream());
                din = new DataInputStream(s.getInputStream());
                tv_inmsg.setText("Server msg:"+din.readUTF());
            }
            catch (Exception e){
                e.getStackTrace();
                btn_send.setText("new");
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
                    btn_send.setText("finally");
                }
            }


            }
    }

}
