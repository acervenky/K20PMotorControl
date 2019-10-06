package cn.iqianye.xiaomi.motor;


import android.app.Application;
import android.view.WindowManager;

public class ApplicationClass extends Application {
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public WindowManager.LayoutParams getMywmParams() {
		return wmParams;

	}

}

