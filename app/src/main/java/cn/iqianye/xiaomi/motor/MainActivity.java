package cn.iqianye.xiaomi.motor;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.jaredrummler.android.shell.Shell;

public class MainActivity extends AppCompatActivity
{

	String appCachePath;
    String popupCommand = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/xiaomi-motor.bin popup 1";
	String takebackCommand = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/xiaomi-motor.bin takeback 1";
	
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	private FloatView myFV;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		appCachePath = getExternalCacheDir().getAbsolutePath();
		AssetsUtils.copyFolderFromAssetsToSD(this, "files", appCachePath + "/");
        if (!OtherUtils.isMIUI()) // 检测MIUI
        {
            Toast.makeText(this, "不支持MIUI系统，请使用类原生或Flyme系统！", Toast.LENGTH_LONG).show();
            finish();
        }
		else
		if (!OtherUtils.checkRoot()) // 检测ROOT
        {
            Toast.makeText(this, "获取Root权限失败，请检查是否给予本软件Root权限！", Toast.LENGTH_LONG).show();
            finish();
        }
		else
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if (!Settings.canDrawOverlays(this))
			{
				startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
			}
			else
			{
				createFloatView();
			}
		}
    }

	public void popup(View view)
	{
		Shell.SU.run("cp -r " + appCachePath + "/* /data/local/tmp");
		Shell.SU.run("chmod 777 /data/local/tmp/*");
		Shell.SU.run(popupCommand);
		Shell.SU.run("rm -f /data/local/tmp/*");
	}

	public void takeback(View view)
	{
		Shell.SU.run("cp -r " + appCachePath + "/* /data/local/tmp");
		Shell.SU.run("chmod 777 /data/local/tmp/*");
		Shell.SU.run(takebackCommand);
		Shell.SU.run("rm -f /data/local/tmp/*");
	}

	public void openHomePage(View view)
	{
		OtherUtils.openUrl("https://www.zhenxin.xyz", this);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

	public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
			case R.id.join_Group:
                joinQQGroup("ntrQLxF4416LNwDiisFZZPjeQ3QNavMj");

                break;
		    case R.id.alipay:
				OtherUtils.openUrl("https://qr.alipay.com/tsx08529pzn1idznbmfobf7", this);
				break;
			default:
				break;

		}
		return true;
	}

	/****************
	 *
	 * 发起添加群流程。群号：ZhenXin&#39;s Group(683903250) 的 key 为： ntrQLxF4416LNwDiisFZZPjeQ3QNavMj
	 * 调用 joinQQGroup(ntrQLxF4416LNwDiisFZZPjeQ3QNavMj) 即可发起手Q客户端申请加群 ZhenXin&#39;s Group(683903250)
	 *
	 * @param key 由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public boolean joinQQGroup(String key)
	{
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try
		{
			startActivity(intent);
			return true;
		}
		catch (Exception e)
		{
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}

	private void createFloatView()
	{
		myFV = new FloatView(getApplicationContext());
		myFV.setImageResource(R.mipmap.icon);
		// 获取WindowManager
		 wm = (WindowManager) getApplicationContext().getSystemService("window");
		// 设置LayoutParams(全局变量）相关参数
	    wmParams = ((ApplicationClass) getApplication()).getMywmParams();

		wmParams.type = LayoutParams.TYPE_PHONE;// 设置window type
		wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
		// 设置Window flag
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
			| LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 
		 * 下面的flags属性的效果形同“锁定”。
		 * 
		 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * 
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
		 * 
		 * | LayoutParams.FLAG_NOT_FOCUSABLE
		 * 
		 * | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 调整悬浮窗口至左上角，便于调整坐标
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 0;
		wmParams.y = 0;
		// 设置悬浮窗口长宽数据
		wmParams.width = 40;
		wmParams.height = 40;
		// 显示myFloatView图像
		wm.addView(myFV, wmParams);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		// 在程序退出(Activity销毁）时销毁悬浮窗口
		wm.removeView(myFV);
	}
}
