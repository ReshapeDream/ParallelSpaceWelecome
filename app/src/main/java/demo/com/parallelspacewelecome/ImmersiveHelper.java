package demo.com.parallelspacewelecome;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

/**
 * @author nzbao
 * @CreateTime 2017/11/27
 * @Desc 沉浸式设计帮助类
 * 通过代码设置状态栏与虚拟导航栏的颜色或图片，以达到沉浸式设计的目的
 */
public class ImmersiveHelper {

    /**
     * 同时设置状态栏与导航栏为同一颜色
     * @param activity
     * @param immersiveColor
     */
    public static void setImmersiveColor(Activity activity, int immersiveColor) {
        setImmersiveColor(activity,immersiveColor,immersiveColor);
    }

    /**
     * 同时设置状态栏与导航栏为不同颜色
     * @param activity
     * @param statusColor
     * @param naviColor
     */
    public static void setImmersiveColor(Activity activity, int statusColor,int naviColor) {
        setImmersiveStatusColor(activity,statusColor);
        setImmersiveNavigationColor(activity, naviColor);
    }

    /**
     * 同时设置状态栏与导航栏为不同图片
     * @param activity
     * @param statusDrawable
     * @param naviDrawable
     */
    public static void setImmersiveDrawable(Activity activity, Drawable statusDrawable,Drawable naviDrawable) {
        setImmersiveStatusDrawable(activity,statusDrawable);
        setImmersiveNavigationDrawable(activity, naviDrawable);
    }

    /**
     * 同时设置状态栏与导航栏为不同图片
     * @param activity
     * @param statusDrawable
     * @param naviDrawable
     */
    public static void setImmersiveDrawable(Activity activity, int statusDrawable,int naviDrawable) {
        setImmersiveDrawable(activity,activity.getResources().getDrawable(statusDrawable),activity.getResources().getDrawable(naviDrawable));
    }

    /**
     * 设置沉浸式状态栏的颜色
     * @param activity
     * @param immersiveColor
     *
     * 需要通过代码或xml 设置activity的根布局 fitsSystemWindows 为 true
     */
    public static void setImmersiveStatusColor(Activity activity, int immersiveColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(immersiveColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = Resources.getSystem().getDimensionPixelSize(getResourceIdByName("com.android.internal.R$dimen", "status_bar_height"));
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View status = new View(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusHeight);
            status.setLayoutParams(params);
            status.setBackgroundColor(immersiveColor);
            decorView.addView(status);
        }
    }

    /**
     * 状态栏透明
     * @param activity
     * @param
     */
    public static void setStatusTranslucent(Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void setNavigationTranslucent(Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 设置沉浸式虚拟导航栏的颜色
     * @param activity
     * @param immersiveColor
     *
     * 需要通过代码或xml 设置activity的根布局 fitsSystemWindows 为 true
     */
    public static void setImmersiveNavigationColor(Activity activity, int immersiveColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(immersiveColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&isShowNavigationBar(activity)) {
            int navigationHeight = Resources.getSystem().getDimensionPixelSize(getResourceIdByName("com.android.internal.R$dimen", "navigation_bar_height"));
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            View view = new View(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, navigationHeight);
            params.gravity= Gravity.BOTTOM;
            view.setLayoutParams(params);
            view.setBackgroundColor(immersiveColor);
            decorView.addView(view);
        }
    }

    /**
     * 设置沉浸式状态栏的图片
     * @param activity
     * @param statusDrawable
     *
     * 需要通过代码或xml 设置activity的根布局 fitsSystemWindows 为 true
     */
    public static void setImmersiveStatusDrawable(Activity activity, Drawable statusDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = Resources.getSystem().getDimensionPixelSize(getResourceIdByName("com.android.internal.R$dimen", "status_bar_height"));
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            decorView.setFitsSystemWindows(true);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View status = new View(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusHeight);
            status.setLayoutParams(params);
            status.setBackground(statusDrawable);
            decorView.addView(status);
        }
    }

    /**
     * 设置沉浸式虚拟导航栏的图片
     * @param activity
     * @param naviDrawable
     *
     * 需要通过代码或xml 设置activity的根布局 fitsSystemWindows 为 true
     */
    public static void setImmersiveNavigationDrawable(Activity activity, Drawable naviDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&isShowNavigationBar(activity)) {
            int navigationHeight = Resources.getSystem().getDimensionPixelSize(getResourceIdByName("com.android.internal.R$dimen", "navigation_bar_height"));
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            View view = new View(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, navigationHeight);
            params.gravity=Gravity.BOTTOM;
            view.setLayoutParams(params);
            view.setBackground(naviDrawable);
            decorView.addView(view);
        }
    }

    /**
     * R.drawable R.mipmap 等等
     * 获取app自定义的资源
     *
     * @param clazz
     * @param name
     * @return
     */
    public static int getResourceIdByName(Class<?> clazz, String name) {
        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(name);
            return declaredField.getInt(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * com.android.internal.R$dimen
     * android 系统内部的资源不能直接通过类获取！要使用上述格式的字符串通过Class.forName()得到类对象
     * 获取app的资源通过 getPackageName+".R$color" 来获取类
     *
     * @param className
     * @param name
     * @return
     */
    public static int getResourceIdByName(String className, String name) {
        try {
            return getResourceIdByName(Class.forName(className), name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 判断 navation bar(虚拟键)是否显示
     * 横竖屏切换时 虚拟导航栏的位置可能影响
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isShowNavigationBar(Activity activity) {
        WindowManager windowManager = activity.getWindow().getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics out = new DisplayMetrics();
        defaultDisplay.getRealMetrics(out);//包含虚拟键的尺寸
        int height = out.heightPixels;
        int width = out.widthPixels;
        out = new DisplayMetrics();
        defaultDisplay.getMetrics(out);//不包含虚拟键的信息
        int contentHeight = out.heightPixels;
        int contentWidth = out.widthPixels;
        int deltaH = height - contentHeight;
        int deltaW = width - contentWidth;
        int requestedOrientation = activity.getRequestedOrientation();
        if(requestedOrientation== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            return deltaW>0;
        }else {
            return deltaH>0;
        }
    }
}
