package com.heliang.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public String getCurrentTime() {
		String CurrentTime = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		CurrentTime = formatter.format(curDate);

		return CurrentTime;
	}
}
