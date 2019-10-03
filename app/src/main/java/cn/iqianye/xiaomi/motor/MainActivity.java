package cn.iqianye.xiaomi.motor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.jaredrummler.android.shell.Shell;

public class MainActivity extends AppCompatActivity
{

	String appCachePath;
    String popupCommand = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/xiaomi-motor.bin popup 1";
	String takebackCommand = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/xiaomi-motor.bin takeback 1";
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
        }else
		if (!OtherUtils.checkRoot()) // 检测ROOT
        {
            Toast.makeText(this, "获取Root权限失败，请检查是否给予本软件Root权限！", Toast.LENGTH_LONG).show();
            finish();
        }
		
		startFloatingButtonService();
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

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        if (requestCode == 0)
		{
            if (!Settings.canDrawOverlays(this))
			{
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
			else
			{
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatingButtonService.class));
            }
        }
    }

    public void startFloatingButtonService()
	{
        intent = new Intent(this, FloatingService.class);
        intent.putExtra(FloatingService.ACTION, FloatingService.HIDE);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        fileListener.stopWatching();
        stopService(intent);
    }

}
