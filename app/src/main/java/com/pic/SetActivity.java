package com.pic;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.*;
import java.io.File;
import java.math.BigDecimal;

public class SetActivity extends Activity
{
    SharedPreferences 数据持久化;
    EditText 打印包名,模板宽,模板高,左边距,上边距,左间距,上间距,清晰度;
    TextView 缓存大小,缓存路径;
    void log(String str){Toast.makeText(this,str,Toast.LENGTH_LONG).show();}
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set); 
        数据持久化= getSharedPreferences("test",Activity.MODE_PRIVATE); //实例化SharedPreferences对象（第一步） 
        
        打印包名=this.findViewById(R.id.bao);
        模板宽=this.findViewById(R.id.ban_w);
        模板高=this.findViewById(R.id.ban_h);
        上边距=this.findViewById(R.id.up);
        左边距= this.findViewById(R.id.l);
        上间距=this.findViewById(R.id.jian_up);
        左间距=this.findViewById(R.id.jian_l);
        清晰度=this.findViewById(R.id.dpi);
        缓存大小=this.findViewById(R.id.huan_chun_b);
        缓存路径=this.findViewById(R.id.huan_chun);
        
        打印包名.setText(数据持久化.getString("打印包名", "epson.print"));
        模板宽.setText(数据持久化.getString("模板宽","100"));
        模板高.setText(数据持久化.getString("模板高","150"));
        上边距.setText(数据持久化.getString("上边距","5"));
        左边距.setText(数据持久化.getString("左边距","3"));
        上间距.setText(数据持久化.getString("上间距","2"));
        左间距.setText(数据持久化.getString("左间距","2"));
        清晰度.setText(数据持久化.getString("清晰度","50"));
        缓存路径.setText("缓存路径:"+this.getExternalCacheDir().getAbsolutePath());
        try {
            缓存大小.setText("    当前缓存大小:"+getTotalCacheSize(this));
        } catch (Exception e) {}
    }
    public void set(View view)
    {
        SharedPreferences.Editor editor = 数据持久化.edit(); //实例化SharedPreferences.Editor对象（第二步）
        editor.putString("打印包名", 打印包名.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("模板宽",模板宽.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("模板高",模板高.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("上边距",上边距.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("左边距",左边距.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("上间距",上间距.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("左间距",左间距.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.putString("清晰度",清晰度.getText().toString()); //把输入框的值用putString的方法保存数据 
        editor.commit(); //提交当前数据 
        log("已提交");
    }
    public void get(View view)
    {
        log(
        数据持久化.getString("打印包名", "epson.print")+"\n"+
        数据持久化.getString("模板高","150")+"\n"+
        数据持久化.getString("模板宽","100")+"\n"+
        数据持久化.getString("上边距","5")+"\n"+
        数据持久化.getString("左边距","3")+"\n"+
        数据持久化.getString("上间距","2")+"\n"+
        数据持久化.getString("左间距","2")+"\n"+
        数据持久化.getString("清晰度","50")
        );
    }
    public void del(View view)
    {
        clearAllCache(this);
        try {
            缓存大小.setText("    当前缓存大小:"+getTotalCacheSize(this));
        } catch (Exception e) {}
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
