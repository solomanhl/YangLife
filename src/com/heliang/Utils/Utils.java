package com.heliang.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public String getCurrentTime() {
		String CurrentTime = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		CurrentTime = formatter.format(curDate);

		return CurrentTime;
	}
}
