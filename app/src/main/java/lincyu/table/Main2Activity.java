package lincyu.table;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private ImageView ImageView_view;
    Button bt_up,bt_right,bt_left,bt_down;
    Switch sw_voice,sw_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //定義專區
        ImageView_view = (ImageView) findViewById(R.id.View);
        bt_down = (Button)findViewById(R.id.bt_do);
        bt_down.setOnClickListener(bt_down_CL);
        bt_up = (Button)findViewById(R.id.bt_top);
        bt_up.setOnClickListener(bt_up_CL);
        bt_right = (Button)findViewById(R.id.bt_right);
        bt_right.setOnClickListener(bt_right_CL);
        bt_left = (Button)findViewById(R.id.bt_left);
        bt_left.setOnClickListener(bt_left_CL);
        sw_voice = (Switch)findViewById(R.id.sw_Voice);
        sw_image = (Switch)findViewById(R.id.sw_Image);

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
                }
                else
                {
                    Toast t = Toast.makeText(Main2Activity.this,"voice close", Toast.LENGTH_SHORT);
                    t.show();
                }
            }

        };
        sw_voice.setOnCheckedChangeListener(sw_voice_CCL);
        //ImageView_view.setImageResource(R.drawable.th);

    }


    //按鈕設定
    private View.OnClickListener bt_up_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            Toast t = Toast.makeText(Main2Activity.this,"UP", Toast.LENGTH_SHORT);
            t.show();

        }
    };
    private View.OnClickListener bt_right_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            Toast t = Toast.makeText(Main2Activity.this,"RIGHT", Toast.LENGTH_SHORT);
            t.show();

        }
    };
    private View.OnClickListener bt_left_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            Toast t = Toast.makeText(Main2Activity.this,"LEFT", Toast.LENGTH_SHORT);
            t.show();

        }
    };
    private View.OnClickListener bt_down_CL = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            Toast t = Toast.makeText(Main2Activity.this,"DOWN", Toast.LENGTH_SHORT);
            t.show();

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


        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
