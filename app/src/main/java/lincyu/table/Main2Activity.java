package lincyu.table;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Main2Activity extends Activity implements SurfaceHolder.Callback {

    Button bt_up,bt_right,bt_left,bt_down;
    Switch sw_voice,sw_image;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView1;
    ImageView iv_collision,iv_water;
    EditText et_account;
    Camera camera;
    int worng1=0,worng2=0;


    //錄音變數
    private MediaRecorder mediaRecorder = null;
    File voice_recodeFile;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        //定義專區

        bt_down = (Button)findViewById(R.id.bt_do);
        bt_down.setOnClickListener(bt_down_CL);
        bt_up = (Button)findViewById(R.id.bt_top);
        bt_up.setOnClickListener(bt_up_CL);
        bt_right = (Button)findViewById(R.id.bt_right);
        bt_right.setOnClickListener(bt_right_CL);
        bt_left = (Button)findViewById(R.id.bt_left);
        bt_left.setOnClickListener(bt_left_CL);
        iv_collision=(ImageView)findViewById(R.id.iv_collision);
        iv_water=(ImageView)findViewById(R.id.iv_water);
        sw_voice = (Switch)findViewById(R.id.sw_Voice);
        sw_image = (Switch)findViewById(R.id.sw_Image);
        surfaceView1=(SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder=surfaceView1.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);



        et_account=(EditText)findViewById(R.id.et_account);
        //啟動SERVER
        Thread TCP_SocketServerThread = new Thread(new TCP_SocketServerThread());
        TCP_SocketServerThread.start();
        Thread UDP_ServerThread = new Thread(new UDP_ServerThread());
        UDP_ServerThread.start();
//        Thread WorngThread =new Thread(new WorngThread());
//        WorngThread.start();

//        // 錄音讀取檔案名稱
//        Intent intent = getIntent();
//       fileName = intent.getStringExtra("fileName");

        //Switch設定
        CompoundButton.OnCheckedChangeListener sw_image_CCL=new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked)
                {
                    Toast t = Toast.makeText(Main2Activity.this,"image open", Toast.LENGTH_SHORT);
                    t.show();
                }
                else
                {
                    Toast t = Toast.makeText(Main2Activity.this,"image close", Toast.LENGTH_SHORT);
                    t.show();
                }
            }

        };
        sw_image.setOnCheckedChangeListener(sw_image_CCL);

        CompoundButton.OnCheckedChangeListener sw_voice_CCL=new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked)
                {
                    Toast t = Toast.makeText(Main2Activity.this,"voice open", Toast.LENGTH_SHORT);
                    t.show();

                    //錄音
                    //設定錄音檔
                     fileName = "record.amr";
                    try {
                        File SDCardpath = Environment.getExternalStorageDirectory();
                        File myDataPath = new File( SDCardpath.getAbsolutePath() + "/download" );
                        if( !myDataPath.exists() ) myDataPath.mkdirs();
                        voice_recodeFile = new File(SDCardpath.getAbsolutePath() + "/download/"+fileName);

                        mediaRecorder = new MediaRecorder();

                        //設定音源
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        //設定輸出檔案的格式
                        mediaRecorder
                                .setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                        //設定編碼格式
                        mediaRecorder
                                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        //設定錄音檔位置
                        mediaRecorder
                                .setOutputFile( voice_recodeFile.getAbsolutePath());

                        mediaRecorder.prepare();

                        //開始錄音
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//


                }
                else
                {
                    Toast t = Toast.makeText(Main2Activity.this,"voice close", Toast.LENGTH_SHORT);
                    t.show();

                    if(mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        File SDCardpath = Environment.getExternalStorageDirectory();
                        Uri uri = Uri.parse( SDCardpath.getAbsolutePath() + "/download/"+fileName);//路徑
                        MediaPlayer  mediaPlayer = MediaPlayer.create(Main2Activity.this,uri);
                        if(mediaPlayer!=null){
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  //播完後釋放
                                @Override
                                public void onCompletion(MediaPlayer playSuccess) {
                                    playSuccess.release();
                                }
                            });
                        }else{//如果抓步道直跑這裡
                            Toast.makeText(getBaseContext(), "錯誤",Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }

        };
        sw_voice.setOnCheckedChangeListener(sw_voice_CCL);
        // 更改警告ImageView_view.setImageResource(R.drawable.th);

    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        System.out.println("surfaceDestroyed");
        camera.stopPreview();
        //關閉預覽
        camera.release();

        //
    }

    //相機設定
    public void surfaceCreated(SurfaceHolder holder) {

            camera = Camera.open();
            Toast.makeText(Main2Activity.this,"正確", Toast.LENGTH_SHORT);

        try {

            Camera.Parameters parameters=camera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setPreviewSize(320, 220);
           // camera.setParameters(parameters);
            //設置參數
            camera.setPreviewDisplay(surfaceHolder);
            //鏡頭的方向和手機相差90度，所以要轉向
            //camera.setDisplayOrientation(90);
            //攝影頭畫面顯示在Surface上
            camera.startPreview();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    //按鈕設定
    private View.OnClickListener bt_up_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            Intent intent = getIntent();
            String ip;
            ip=intent.getStringExtra("IP");

            TCP_SocketClientThread TCP_SocketClientThread = new TCP_SocketClientThread("F",ip);
            TCP_SocketClientThread.start();

        }
    };
    private View.OnClickListener bt_right_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
//            Toast t = Toast.makeText(Main2Activity.this,"RIGHT", Toast.LENGTH_SHORT);
//            t.show();
            Intent intent = getIntent();
            String ip;
            ip=intent.getStringExtra("IP");
            TCP_SocketClientThread TCP_SocketClientThread = new TCP_SocketClientThread("R",ip);
            TCP_SocketClientThread.start();

        }
    };
    private View.OnClickListener bt_left_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
//            Toast t = Toast.makeText(Main2Activity.this,"LEFT", Toast.LENGTH_SHORT);
//            t.show();
            Intent intent = getIntent();
            String ip;
            ip=intent.getStringExtra("IP");
            TCP_SocketClientThread TCP_SocketClientThread = new TCP_SocketClientThread("L",ip);
            TCP_SocketClientThread.start();

        }
    };
    private View.OnClickListener bt_down_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
//            Toast t = Toast.makeText(Main2Activity.this,"DOWN", Toast.LENGTH_SHORT);
//            t.show();
            Intent intent = getIntent();
            String ip;
            ip=intent.getStringExtra("IP");
            TCP_SocketClientThread TCP_SocketClientThread = new TCP_SocketClientThread("B",ip);
            TCP_SocketClientThread.start();
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("操作說明");
                ad.setMessage("方向鍵控制\n開關決定是否傳影音過去\n當發生警告事項時，警告燈會變\nMenu中可查詢車子身在的溫濕度");
                DialogInterface.OnClickListener listener =
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface di, int i) {
                            }
                        };
                ad.setPositiveButton("確定", listener);
                ad.show();
                break;

            case R.id.action_exit:


                break;

            case R.id.action_note:
                Intent intent2 = getIntent();
                String ip;
                ip=intent2.getStringExtra("IP");
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this,Note.class);
                intent.putExtra("IP",ip);
                startActivity(intent);


                break;


        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    //Server端
    private class TCP_SocketServerThread extends Thread {
       int cleint_msg=0;

        @Override
        public void run() {
            ServerSocket ss =null;
            Socket s =null;
            DataInputStream din = null;
            DataOutputStream dout =null;
            try{
                ss= new ServerSocket(8880);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            while(true){
                try{
                    s=ss.accept();
                    din = new DataInputStream(s.getInputStream());
                    dout = new DataOutputStream(s.getOutputStream());
                    cleint_msg = din.readInt(); //這是裡傳來的訊息

                    Main2Activity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {



                            if (cleint_msg ==1){
                                iv_collision.setImageResource(R.drawable.error);

                            }
                            if (cleint_msg ==2){

                                iv_water.setImageResource(R.drawable.error);

                            }
                            if (cleint_msg ==3){
                                iv_collision.setImageResource(R.drawable.fine);

                            }
                            if (cleint_msg ==4){
                                iv_water.setImageResource(R.drawable.fine);

                            }
                        }
                    });


                    dout.writeUTF("User_Server");
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
                        if(s != null){
                            s.close();
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
        String server_msg,msg,ip;

        public TCP_SocketClientThread(String msg,String ip){
            this.msg=msg;
            this.ip=ip;
        }
        @Override
        public void run() {
            Socket s = null;
            DataOutputStream dout = null;
            DataInputStream din =null;
            try{
                s = new Socket(ip,8888);
                dout = new DataOutputStream(s.getOutputStream());
                din = new DataInputStream(s.getInputStream());
                dout.writeUTF(msg);
//                Toast t = Toast.makeText(Main2Activity.this,msg, Toast.LENGTH_SHORT);
//                t.show();
                server_msg = din.readUTF(); //這是裡傳來的訊息
                //UI更新
                Main2Activity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


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
    //UDP
    public class UDP_ServerThread extends Thread{
        public void run(){
            int		port = 8080;
            byte []	buf = new byte[1000];
            byte []	buf2 = new byte[1000];

            try {
                // Construct a datagram socket and bind it to the specified port
                DatagramSocket socket = new DatagramSocket(port);

                System.out.println("Waiting on port : " + port);

                // Construct a DatagramPacket for receiving packets
                DatagramPacket recPacket = new DatagramPacket(buf, buf.length);
                while (true) {
                    // Receive a datagram packet from this socket
                    socket.receive(recPacket);
                    // Process message
                    InetAddress senderAddr = recPacket.getAddress();
                    int senderPort = recPacket.getPort();
                    final String msg = new String("Receive message " + new String(buf, 0, buf.length) +
                            " from address : " + senderAddr +
                            ", port : " + 8080);


                    System.out.println(msg);
                    //UI更新
                    Main2Activity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(Main2Activity.this,msg,Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });


                }

            }catch (Exception e){

            }
        }
    }

}
