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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.itextpdf.text.Image;

public class MainActivity extends Activity 
{
    String filePath;//传进来的地址
	String filePath90;
    String txt="调试信息";//运行信息
    SharedPreferences 数据持久化;
    ImageView image_che,image_ren;
    Bitmap che,ren;
    String 打印包名;
    TextView text;
    boolean 是否旋转=true;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        数据持久化 = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);;//实例化SharedPreferences对象（第一步）  //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象 
        打印包名=数据持久化.getString("打印包名", "epson.print");
        clearAllCache(this);//清除缓存
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
							File mfile=uri2File(imageUri);
							//debug("接收到的图片链接"+filePath);
                            if (mfile.exists()) {
                                zhuan(null);
								Bitmap bitmap = BitmapFactory.decodeFile(filePath);
								Matrix matrix = new Matrix();
								matrix.postRotate(90);
								Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
								File dir=new File(this.getExternalCacheDir().getAbsolutePath());
								if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
								String fileName = System.currentTimeMillis() + ".jpeg";
								File file = new File(dir, fileName);
								OutputStream outputStream = null; //文件输出流
								try {
									outputStream = new FileOutputStream(file);
									resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); //将图片压缩为JPEG格式写到文件输出流，100是最大的质量程度
								} catch (Exception e) {
									e.printStackTrace();
								}
								filePath90= file.toString();
                            }else{log("内存卡读取失败");}
                        }
                     }
            }  else {}
    }
	
    public void click_1(View view)
    {
        try {
			Document document = new Document(PageSize.A4);
			//PageSize.A4=595 x 842 	A4尺寸=210mm×297mm
			File dir=new File(this.getExternalCacheDir().getAbsolutePath());
			if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
			String fileName = System.currentTimeMillis() + ".pdf";
			File file = new File(dir, fileName);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();//打开文档

			Image img1 =c裁剪图片(pdfWriter,(60f/90f));
			img1.setAbsolutePosition(10,842-170-10);//位置
			img1.scaleAbsolute(255,170);//大小
			document.add(img1);

			document.close();
			pdfWriter.close();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("application/pdf");
			Uri fileUri = null;
			if (Build.VERSION.SDK_INT >= 24) {fileUri = FileProvider.getUriForFile(this, "com.pic",file);}
			else {fileUri = Uri.fromFile(file);}//;log("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。");
			share.putExtra(Intent.EXTRA_STREAM,fileUri);
			share.setPackage(打印包名);
			startActivity(Intent.createChooser(share, "Select"));//发送到打印机
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public void click_2(View view)
    {
        try {
			Document document = new Document(PageSize.A4);
			//PageSize.A4=595 x 842 	A4尺寸=210mm×297mm
			File dir=new File(this.getExternalCacheDir().getAbsolutePath());
			if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
			String fileName = System.currentTimeMillis() + ".pdf";
			File file = new File(dir, fileName);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();//打开文档

			Image img1 =c裁剪图片(pdfWriter,(60f/90f));
			img1.scaleAbsolute(255,170);//大小
			img1.setAbsolutePosition(10,842-170-10);//位置
			document.add(img1);

			img1.setAbsolutePosition(10,842-170-10-170-10);//位置
			document.add(img1);
			
			document.close();
			pdfWriter.close();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("application/pdf");
			Uri fileUri = null;
			if (Build.VERSION.SDK_INT >= 24) {fileUri = FileProvider.getUriForFile(this, "com.pic",file);}
			else {fileUri = Uri.fromFile(file);}//;log("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。");
			share.putExtra(Intent.EXTRA_STREAM,fileUri);
			share.setPackage(打印包名);
			startActivity(Intent.createChooser(share, "Select"));//发送到打印机
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public void click_3(View view)
    {
        try {
			Document document = new Document(PageSize.A4);
			//PageSize.A4=595 x 842 	A4尺寸=210mm×297mm
			File dir=new File(this.getExternalCacheDir().getAbsolutePath());
			if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
			String fileName = System.currentTimeMillis() + ".pdf";
			File file = new File(dir, fileName);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();//打开文档

			Image img1 =c裁剪图片(pdfWriter,(35f/25f));
			img1.scaleAbsolute(71,99);//大小
			img1.setAbsolutePosition(10,842-99-10);//位置
			document.add(img1);

			img1.setAbsolutePosition(10+71+10,842-99-10);//位置
			document.add(img1);

			img1.setAbsolutePosition(10+71+10+71+10,842-99-10);//位置
			document.add(img1);
			
			document.close();
			pdfWriter.close();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("application/pdf");
			Uri fileUri = null;
			if (Build.VERSION.SDK_INT >= 24) {fileUri = FileProvider.getUriForFile(this, "com.pic",file);}
			else {fileUri = Uri.fromFile(file);}//;log("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。");
			share.putExtra(Intent.EXTRA_STREAM,fileUri);
			share.setPackage(打印包名);
			startActivity(Intent.createChooser(share, "Select"));//发送到打印机
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public void click_6(View view)
    {
        try {
			Document document = new Document(PageSize.A4);
			//PageSize.A4=595 x 842 	A4尺寸=210mm×297mm
			File dir=new File(this.getExternalCacheDir().getAbsolutePath());
			if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
			String fileName = System.currentTimeMillis() + ".pdf";
			File file = new File(dir, fileName);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();//打开文档

			Image img1 =c裁剪图片(pdfWriter,(35f/25f));
			img1.scaleAbsolute(71,99);//大小
			img1.setAbsolutePosition(10,842-99-10);//位置
			document.add(img1);
			img1.setAbsolutePosition(10+71+10,842-99-10);//位置
			document.add(img1);
			img1.setAbsolutePosition(10+71+10+71+10,842-99-10);//位置
			document.add(img1);

			img1.setAbsolutePosition(10,842-99-10-99-10);//位置
			document.add(img1);
			img1.setAbsolutePosition(10+71+10,842-99-10-99-10);//位置
			document.add(img1);
			img1.setAbsolutePosition(10+71+10+71+10,842-99-10-99-10);//位置
			document.add(img1);
			
			document.close();
			pdfWriter.close();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("application/pdf");
			Uri fileUri = null;
			if (Build.VERSION.SDK_INT >= 24) {fileUri = FileProvider.getUriForFile(this, "com.pic",file);}
			else {fileUri = Uri.fromFile(file);}//;log("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。");
			share.putExtra(Intent.EXTRA_STREAM,fileUri);
			share.setPackage(打印包名);
			startActivity(Intent.createChooser(share, "Select"));//发送到打印机
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public void click_4(View view)
    {
        
        try {
			Document document = new Document(PageSize.A4);
			//PageSize.A4=595 x 842 	A4尺寸=210mm×297mm
			File dir=new File(this.getExternalCacheDir().getAbsolutePath());
			if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
			String fileName = System.currentTimeMillis() + ".pdf";
			File file = new File(dir, fileName);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();//打开文档

			Image img1 =c裁剪图片(pdfWriter,(45f/35f));
			img1.scaleAbsolute(99,127);//大小
			img1.setAbsolutePosition(10,842-127-10);//位置
			document.add(img1);
			img1.setAbsolutePosition(10+99+10,842-127-10);//位置
			document.add(img1);

			img1.setAbsolutePosition(10,842-127-10-127-10);//位置
			document.add(img1);
			img1.setAbsolutePosition(10+99+10,842-127-10-127-10);//位置
			document.add(img1);

			document.close();
			pdfWriter.close();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("application/pdf");
			Uri fileUri = null;
			if (Build.VERSION.SDK_INT >= 24) {fileUri = FileProvider.getUriForFile(this, "com.pic",file);}
			else {fileUri = Uri.fromFile(file);}//;log("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。");
			share.putExtra(Intent.EXTRA_STREAM,fileUri);
			share.setPackage(打印包名);
			startActivity(Intent.createChooser(share, "Select"));//发送到打印机
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
			public void click_5(View view)
    {
				try {
					Document document = new Document(PageSize.A4);
					//PageSize.A4=595 x 842 	A4尺寸=210mm×297mm
					File dir=new File(this.getExternalCacheDir().getAbsolutePath());
					if (!dir.exists()) {dir.mkdir();}//如果文件夹不存在就先创建。
					String fileName = System.currentTimeMillis() + ".pdf";
					File file = new File(dir, fileName);
					PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
					document.open();//打开文档
					
					Image img1 =c裁剪图片(pdfWriter,(60f/90f));
					img1.scaleAbsolute(255,170);//大小
					img1.setAbsolutePosition(10,842-170-10);//位置
					document.add(img1);
					
					img1.setAbsolutePosition(10+255+10,842-170-10);//位置
					document.add(img1);
					
					document.close();
					pdfWriter.close();
					Intent share = new Intent(android.content.Intent.ACTION_SEND);
					share.setType("application/pdf");
					Uri fileUri = null;
					if (Build.VERSION.SDK_INT >= 24) {fileUri = FileProvider.getUriForFile(this, "com.pic",file);}
					else {fileUri = Uri.fromFile(file);}//;log("当前安卓版本"+Build.VERSION.SDK_INT+"，使用新版分享。");
					share.putExtra(Intent.EXTRA_STREAM,fileUri);
					share.setPackage(打印包名);
					startActivity(Intent.createChooser(share, "Select"));//发送到打印机
				} catch (Exception e) {
				e.printStackTrace();
				}
				
    }
	
	public Image c裁剪图片(PdfWriter writer, float h_w) throws DocumentException, BadElementException, IOException {
		String image_filePath=filePath;
		if(是否旋转==true){image_filePath=filePath90;}
		Image image=Image.getInstance(image_filePath);
		float width = image.getScaledWidth();
		float height = image.getScaledHeight();
		PdfTemplate template = null;
		if((height/h_w)>width){
			template = writer.getDirectContent().createTemplate(
				width,
				width*h_w);
			template.addImage(image, width, 0, 0, height, 
							  0, -(height - width*h_w)/2);
		}else{
			template = writer.getDirectContent().createTemplate(
				height/h_w,
				height);
			template.addImage(image, width, 0, 0, height, 
							  -(width-height/h_w)/2,0);
		}
		return Image.getInstance(template);
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
    
    public void zhuan(View view) 
    {
        if(是否旋转==false)
        {
            che=getImageThumbnail(filePath,600,900);
            ren=getImageThumbnail(filePath,350,250);
            image_che.setImageBitmap(rotatePicture(che,90));
            image_ren.setImageBitmap(rotatePicture(ren,90));
			是否旋转=true;
        }else
        {
            che=getImageThumbnail(filePath,900,600);
            ren=getImageThumbnail(filePath,250,350);
            image_che.setImageBitmap(che);
            image_ren.setImageBitmap(ren);
			是否旋转=false;
        }
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
    @Override
    protected void onPause()//退出软件即关闭
    {
        super.onPause();
        System.exit(0);
    }    
}


