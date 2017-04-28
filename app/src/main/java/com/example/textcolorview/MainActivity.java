package com.example.textcolorview;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("执行onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        自定义绘制TV的类
         */
        TextColorView tv = (TextColorView)findViewById(R.id.tv);
        tv.setTextAngle(0);
        tv.setTvText("wangxiaobo");

//        tv.setTextStartColor(getResources().getColor(R.color.colorPrimaryDark));

        /*
        测量手机屏幕的实际状态栏，标题栏的宽高
         */
        //1.获取手机自身物理设备的宽高
        float density = getResources().getDisplayMetrics().density;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        System.out.println("手机屏幕的密度：" + density + "手机屏幕的密度2：" + metrics.density);
        System.out.println("手机屏幕的宽度："+metrics.widthPixels+"手机屏幕的高度："+metrics.heightPixels);


        //获取手机状态栏的高度方法1，使用系统的属性值进行获取
        int statusBarHeight = -1;
        //获取status_bar_height的资源ID
        int ResourceID = getResources().getIdentifier("status_bar_height","dimen","android");

        if(ResourceID>0){
            statusBarHeight = getResources().getDimensionPixelSize(ResourceID);
        }
        System.out.println("手机状态栏的高度方法1：" + statusBarHeight);

        //方法二：通过java的反射机制获取
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight2 = getResources().getDimensionPixelSize(height);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("手机状态栏的高度方法2：" + statusBarHeight2);

        //方法三，借助应用区域的top属性,但是使用的时候需要放在 onWindowFocusChanged()中才回出现想要的效果

        getWindowStatusBarHeightAboutTop();

        //方法四，借助屏幕和应用区域高度

        getWindowStatusBarHeightAboutHeight();

        /*
        获取手机的标题栏的高度，整体的思路就是 应用区域高度-绘制区域的高度   或者使用绘制区域的Top-应用区域的Top
         */




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("执行onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("执行onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("执行onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("执行onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("执行onDestroy");
    }

    private void getWindowStatusBarHeightAboutTop(){

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        System.out.println("依靠显示界面的Top属性获取状态栏的高度方法三" + rect.top);



    }


    private void getWindowStatusBarHeightAboutHeight(){
        Rect rectHeight = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectHeight);

        System.out.println("依靠显示界面的Top属性获取状态栏的高度方法四"+(getResources().getDisplayMetrics().heightPixels-rectHeight.height()));



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        System.out.println("执行onWindowFocusChanged");

        getWindowStatusBarHeightAboutTop();
        getWindowStatusBarHeightAboutHeight();


        //1.首先获取手机应用区域
        Rect rectProject = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectProject);
        //2.获取手机屏幕绘制的区域
        Rect rectDraw = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rectDraw);
        int h = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        System.out.println("绘制区域的Top：" + h);
        System.out.println("绘制区域的Height：" + (rectDraw.height())+"----------------"+getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight());
        System.out.println("应用区域的Top：" + (rectProject.top));
        System.out.println("应用区域的Height：" + (rectProject.height()));
        System.out.println("标题栏的高度使用Top："+(rectDraw.top-rectProject.top));
        System.out.println("标题栏的高度使用Height："+(rectProject.height()-rectDraw.height()));
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
