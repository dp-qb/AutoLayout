package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.View;
import android.widget.TextView;
import android.net.Uri;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.icu.util.Calendar;
import android.content.Intent;
import android.provider.MediaStore;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import java.io.FileOutputStream;
import java.io.File;
import android.widget.Toast;
import android.content.Context;
import android.content.ContentResolver;
import android.provider.DocumentsContract;
import android.content.ContentUris;
import java.io.IOException;
import android.content.SharedPreferences;

public class MainActivity extends Activity 
{
	private static int XC=20;
	Uri saveUri,request;//相册返回的Uri，第三方请求的Uri
	TextView text;
	ImageView imageView;
	String picturePath;//相册返回的Uri的真实文件路径
	String str="调试信息";//debug
    SharedPreferences sharedPreferences;//数据持久化
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text =  findViewById(R.id.text);
		imageView = findViewById(R.id.imgView);
		 request=getIntent().getParcelableExtra("output");
		logt("收到的图片链接请求"+request);//收到的图片链接请求
        sharedPreferences= getSharedPreferences("test",Activity.MODE_PRIVATE); //实例化SharedPreferences对象（第一步） 
        picturePath =sharedPreferences.getString("image_uri", ""); // 使用getString方法获得value，注意第2个参数是value的默认值 
		String old_image=sharedPreferences.getString("old_image_uri",""); // 使用getString方法获得value，注意第2个参数是value的默认值 
		if(old_image!=null){deleteSingleFile(old_image);}
		logt("要发送的图片"+picturePath);//收到的相册链接请求
		if(request==null){imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));}
		else{
			fh(null);
			gb(null);
		}
	}
    private void deleteSingleFile(String filePath$Name) {
        // 文件拼接把私有连接转换为绝对地址
		String b = filePath$Name.substring(filePath$Name.lastIndexOf("/") + 1, filePath$Name.length());
		File file = new File("/storage/emulated/0/DCIM/Camera/"+b);
		logt("/storage/emulated/0/DCIM/Camera/"+b);
		file.delete();
    }

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{//接收相册返回的图片
		if(XC==requestCode)
		{
			if (null != data) 
			{
				saveUri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				//查询我们需要的数据
				Cursor cursor = getContentResolver().query(saveUri,filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				SharedPreferences.Editor editor = sharedPreferences.edit(); //实例化SharedPreferences.Editor对象（第二步）
				editor.putString("image_uri",picturePath); //把要发送的图片连接用putString的方法保存数据 
				editor.commit(); //提交当前数据 
				//得到图片文件file和图片bitmap数据BitmapFactory.decodeFile(picturePath)
				imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
				logt("相册返回的Uri的真实文件路径"+picturePath);
			}
		}
	}

	public void ce(View view)
	{//向相册请求图片
		Intent intent = new Intent(
			Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		//这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
		startActivityForResult(intent, XC);//intent打开相册
	}
	public void fh(View view)
	{//把图片内容写入到请求连接
		logt("开始发送");
		FileOutputStream fileOutStream=null; 
		try { 
			File mfile=   new File(getFilePathByUri(this,request));
			SharedPreferences.Editor editor = sharedPreferences.edit(); //实例化SharedPreferences.Editor对象（第二步）
			editor.putString("old_image_uri",mfile.toString()); //
			editor.commit(); //提交当前数据 
			logt("请求的真实文件路径"+mfile);
			fileOutStream=new FileOutputStream(mfile); 
			//把位图输出到指定的文件中 
			BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.PNG, 100, fileOutStream); 
			fileOutStream.close(); 
			logt("图片写入完毕！");
		} catch (IOException io) 
		{ 
			logt("文件读取失败");
		} 
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
	}
	public void gb(View view){finish();}//关闭当前界面

	void log(String debug){Toast.makeText(getApplicationContext(),debug,Toast.LENGTH_SHORT).show();}//浮动提示
	void logt(String debug){str+="\n"+debug;text.setText(str);}

	public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
																	  Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
