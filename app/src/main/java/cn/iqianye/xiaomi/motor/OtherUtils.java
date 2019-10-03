package cn.iqianye.xiaomi.motor;
import com.jaredrummler.android.shell.Shell;
import com.stericson.RootTools.RootTools;
import android.net.Uri;
import android.content.Intent;
import android.app.Activity;

public class OtherUtils
{
	public static boolean isMIUI()
    {
        String s = Shell.SH.run("getprop ro.miui.ui.version.code").toString();
        if (s.isEmpty())
        {
            return false;
        }    
        else
        {
            return true;
        }

    }
	
    public static Boolean checkRoot()
    {
        if (RootTools.isRootAvailable())
        {
            if (!RootTools.isAccessGiven())
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        return true;
    }
	
	public static void openUrl(String url,Activity activity)
	{
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		activity.startActivity(intent);
	}
}
