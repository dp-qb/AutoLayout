package com.pic;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.*;
import java.io.File;
import java.math.BigDecimal;
import android.preference.*;

public class SetActivity extends PreferenceActivity
{
    SharedPreferences 数据持久化;
	PreferenceManager 当前界面;
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_layout);
		当前界面 = getPreferenceManager();
        数据持久化 = getPreferenceScreen().getSharedPreferences();//实例化SharedPreferences对象（第一步） 
		findPreference("打印包名").setSummary(数据持久化.getString("打印包名", "epson.print"));
		findPreference("模板宽").setSummary(数据持久化.getString("模板宽", "100"));
		findPreference("模板高").setSummary(数据持久化.getString("模板高", "150"));
		findPreference("上边距").setSummary(数据持久化.getString("上边距", "5"));
		findPreference("左边距").setSummary(数据持久化.getString("左边距", "3"));
        findPreference("上间距").setSummary(数据持久化.getString("上间距", "2"));
		findPreference("左间距").setSummary(数据持久化.getString("左间距", "2"));
		findPreference("清晰度").setSummary(数据持久化.getString("清晰度", "50"));
		当前界面.findPreference("打印包名").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("模板宽").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("模板高").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("上边距").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("左边距").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("上间距").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("左间距").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		当前界面.findPreference("清晰度").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
    }
    /**
     * 获取缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }
    /**
     * 清除缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
            + "TB";
    }
}
