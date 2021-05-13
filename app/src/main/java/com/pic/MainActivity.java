package com.pic;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import java.io.*;
import android.content.*;
import android.net.*;
import android.graphics.*;
import android.provider.*;
import android.database.*;
import java.util.*;
import android.content.pm.*;
import android.media.*;
import org.w3c.dom.Text;
import android.support.v4.content.FileProvider;
import android.preference.*;

public class MainActivity extends Activity 
{
    String filePath;//传进来的地址
    String txt;//运行信息
    private ProgressDialog dialog;//努力排版中
    SharedPreferences 数据持久化;
    ImageView image_che,image_ren,image_ren_erchun;
    Bitmap che,ren,erchun;
    String 打印包名;
    int 清晰度,模板高,模板宽,左边距,上边距,左间距,上间距;
    TextView text;
    boolean 是否旋转=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在排版，请稍后...");
        dialog.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        数据持久化 = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);;//实例化SharedPreferences对象（第一步）  //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象 
        打印包名=数据持久化.getString("打印包名", "epson.print");
        模板宽=Integer.valueOf(数据持久化.getString("模板宽","100"));
        模板高=Integer.valueOf(数据持久化.getString("模板高","150"));
        上边距=Integer.valueOf(数据持久化.getString("上边距","5"));
        左边距=Integer.valueOf(数据持久化.getString("左边距","3"));
        上间距=Integer.valueOf(数据持久化.getString("上间距","2"));
        左间距=Integer.valueOf(数据持久化.getString("左间距","2"));
        清晰度=Integer.valueOf(数据持久化.getString("清晰度","50"));
        //SetActivity.clearAllCache(this);//清除缓存
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        image_che=findViewById(R.id.che);
        image_ren=findViewById(R.id.ren);
        text=findViewById(R.id.text);
        if (Intent.ACTION_SEND.equals(action) && type != null) 
            {
                if (type.startsWith("image/")) 
                    {
                        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        if (imageUri != null) {
                            filePath= uritodata(this,imageUri);//Uri链接转换data链接
                            File mfile=new File(filePath);//获取图片文件
                            if (mfile.exists()) {
                                zhuan(null);
                            }else{log("内存卡读取失败");}
                        }
                     }
            }  else {}
    }
	
    public void click_1(View view)
    {
        paiban thread1 = new paiban(清晰度,模板高,模板宽,1,1,左边距,上边距,左间距,上间距,che);
        thread1.start();
    }
    public void click_2(View view)
    {
        paiban thread2 = new paiban(清晰度,模板高,模板宽,2,1,左边距,上边距,左间距,上间距,che);
        thread2.start();
    }
    public void click_3(View view)
    {
        paiban thread3 = new paiban(清晰度,模板高,模板宽,1,3,左边距,上边距,左间距,上间距,ren);
        thread3.start();
    }
    public void click_6(View view)
    {
        paiban thread6 = new paiban(清晰度,模板高,模板宽,2,3,左边距,上边距,左间距,上间距,ren);
        thread6.start();
    }
    public void click_4(View view)
    {
        
        paiban thread4 = new paiban(清晰度,模板高,模板宽,2,2,左边距,上边距,左间距,上间距,erchun);
        thread4.start();
    }
    public void zhuan(View view) 
    {
        if(是否旋转==false)
        {
            che=getImageThumbnail(filePath,60*清晰度,90*清晰度);
            ren=getImageThumbnail(filePath,35*清晰度,25*清晰度);
            erchun=getImageThumbnail(filePath,45*清晰度,35*清晰度);
            image_che.setImageBitmap(rotatePicture(che,90));
            image_ren.setImageBitmap(rotatePicture(ren,90));
        }else
        {
            che=getImageThumbnail(filePath,90*清晰度,60*清晰度);
            ren=getImageThumbnail(filePath,25*清晰度,35*清晰度);
            erchun=getImageThumbnail(filePath,35*清晰度,45*清晰度);
            image_che.setImageBitmap(che);
            image_ren.setImageBitmap(ren);
        }
        是否旋转=!是否旋转;
    }
    public Bitmap rotatePicture(final Bitmap bitmap, final int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBitmap;
    }
    void log(String debug){Toast.makeText(getApplicationContext(),debug,Toast.LENGTH_SHORT).show();}//浮动提示
    void debug(String s){txt=txt+"\n"+s;text.setText(txt);};

	private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
												null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
				.getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }
    //Uri转地址
    private String uritodata(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    //分享
    private void initShareIntent(String type,File newimage) {
		//type="com.fooview.android.fooview";
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");
		Uri fileUri = null;
		if (Build.VERSION.SDK_INT >= 24) {
			//Toast.makeText(MainActivity.this,("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。"), Toast.LENGTH_LONG).show();
			fileUri = FileProvider.getUriForFile(this, "com.pic", newimage);
		} else {
			fileUri = Uri.fromFile(newimage);
		}
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);//获取所有应用包名
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
				   if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                    info.activityInfo.name.toLowerCase().contains(type) ) {
					share.putExtra(Intent.EXTRA_STREAM,fileUri);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (found)
			{
				startActivity(Intent.createChooser(share, "Select"));//发送到打印机
			}else{
				Toast.makeText(this,("没有找到打印机驱动当前设置的包名："+打印包名), Toast.LENGTH_LONG).show();
				}
        }
		
    }
//获取300dpi的缩略图降低运算压力
    public Bitmap getImageThumbnail(String imagePath, int width, int height) { 
        Bitmap bitmap = null; 
        BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        // 获取这个图片的宽和高，注意此处的bitmap为null 
        bitmap = BitmapFactory.decodeFile(imagePath, options); 
        options.inJustDecodeBounds = false; // 设为 false 
        // 计算缩放比 
        int h = options.outHeight; 
        int w = options.outWidth; 
        int beWidth = w / width; 
        int beHeight = h / height; 
        int be = 1; 
        if (beWidth < beHeight) { 
            be = beWidth; 
        } else { 
            be = beHeight; 
        } 
        if (be <= 0) { 
            be = 1; 
        } 
        options.inSampleSize = be; 
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false 
        bitmap = BitmapFactory.decodeFile(imagePath, options); 
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象 
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT); 
        return bitmap; 
    }
    
    //保存排好版的图片
    public File saveImageToGallery(Bitmap bmp) {
        File dir=new File(this.getExternalCacheDir().getAbsolutePath());
        // 首先保存图片
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);
        // 创建一个位于SD卡上的文件 
        FileOutputStream fileOutStream=null; 
        try { 
            fileOutStream=new FileOutputStream(file); 
            //把位图输出到指定的文件中 
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutStream); 
            fileOutStream.close(); 
        } catch (IOException io) { 
            io.printStackTrace(); 
        } 
		return file;
    }
    @Override
    protected void onPause()//退出软件即关闭
    {
        super.onPause();
        System.exit(0);
    }
    /**
    *在新线程进行排版任务。
    **/
    class paiban extends Thread
    {
        int BAN_H,BAN_W,X,Y,L,UP,JIAN_L,JIAN_UP;
        Bitmap bm;
        paiban(int dpi,int 版高,int 版宽,int 行数,int 列数,int 左边距,int 上边距,int 左间距,int 上间距,Bitmap 图片)
        {
            dialog.show();
            BAN_H=版高*dpi;
            BAN_W=版宽*dpi;
            X=列数;
            Y=行数;
            L=左边距*dpi;
            UP=上边距*dpi;
            JIAN_L=左间距*dpi;
            JIAN_UP=上间距*dpi;
            bm=图片;
        }
        @Override
        public void run() 
        {
            Bitmap bagimage=Bitmap.createBitmap(BAN_W,BAN_H,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bagimage);
            for(int i=0;i<X;i++)
            {
                for(int n=0;n<Y;n++)
                {
                    canvas.drawBitmap(bm,L+(JIAN_L*i+bm.getWidth()*i),UP+(JIAN_UP*n+bm.getHeight()*n), null);
                }
            }
            canvas.save();  
            canvas.restore();  
            File f= saveImageToGallery(bagimage);//把图片保存到文件
            dialog.dismiss();// 隐藏对话框
			Looper.prepare();
			initShareIntent(打印包名,f);//自动发送打印机
			Looper.loop();
        }
    }    
}


