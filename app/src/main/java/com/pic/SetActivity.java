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
		当前界面.findPreference("打印包名").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
    }
}
