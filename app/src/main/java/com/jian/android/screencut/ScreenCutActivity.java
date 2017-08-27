package com.jian.android.screencut;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ScreenCutActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private RecyclerView mRecycler;

    private int[] iconIds;
    private ArrayList<IconBean> mDatas;
    private ImageView iv_icon_show;
    private EditText et_input;
    private Toolbar toolbar;
    private ImageView mOne;
    private ImageView mTwo;
    private ImageView mThree;
    private MyAdapter myAdapter;
    private int[] idTwos;
    private int[] idThrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_cut);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ShareSDK.initSDK(this);

        imageView= (ImageView) findViewById(R.id.iv_image);
        imageView.setPadding(10,10,10,10);
        iv_icon_show = (ImageView) findViewById(R.id.iv_icon_show);

        et_input = (EditText) findViewById(R.id.et_input);

        ImageView v = (ImageView) findViewById(R.id.s);




        //禁止软键盘弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        File cacheFile=new File(getCacheDir(),"ScreenCut");

        if (cacheFile.exists()){

            Bitmap bm = BitmapFactory.decodeFile(getCacheDir()+"//ScreenCut");

            sd sd = new sd();

           Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.time);

            Bitmap fastblur = sd.fastblur(bitmap, 99);

            v.setImageBitmap(fastblur);

            imageView.setImageBitmap(bm);
        }else {
            Toast.makeText(this,"加载图片失败",Toast.LENGTH_SHORT).show();
        }

        initView();
    }



    public void initView(){

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            default:
                break;
        }
    }


    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setSite(getString(R.string.app_name));
        oks.setImagePath("/sdcard/icon.png");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.share:

                View view=getWindow().getDecorView().findViewById(R.id.fl_show);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();

                try {
                    String status = Environment.getExternalStorageState();
                    // 判斷SD卡是否存在
                    if (status.equals(Environment.MEDIA_MOUNTED)) {

                        String path=Environment.getExternalStorageDirectory().toString();//获取根目录

                        FileOutputStream out=null;
                        try {
                            out=new FileOutputStream(new File(path,"icon.png"));
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);

                            out.flush();
                            out.close();

                            showShare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }

        return true;
    }
}
