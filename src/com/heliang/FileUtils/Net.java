/**
 * 网络下载
 * 
 * @author 贺亮
 * 
 */
package com.heliang.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Net {
	private URL url = null; 
	/**
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return 
	 * 		-1:文件下载出错
	 * 		 0:文件下载成功
	 * 		 1:文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName)
	{
		InputStream inputStream = null;
		int rtn=0;
		try 
		{
			FileUtils fileUtils = new FileUtils();//见FileUtils见FileUtils.java
			
			/*
			if(fileUtils.isFileExist(path + fileName))
			{
				rtn=1;
				return rtn;
			} 
			else {
			*/
			
				inputStream = getInputStreamFromURL(urlStr);	//下载
				if (inputStream != null){//确实下载到了才写文件，避免0文件,避免误删除
					if(fileUtils.isFileExist(path + fileName)){	//如果有，先删除
						fileUtils.deleteFile(path + fileName);
						System.out.println("删除：" +  path + fileName);
					}
					//File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);	//保存
					File resultFile = fileUtils.writeFromInput(path, fileName, inputStream);	//保存
					if(resultFile == null)
					{
						rtn=-1;
						return rtn;
				}
				
				
				}
			//}	// end else
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			rtn=-1;
			return rtn;
		}
		finally
		{
			try 
			{
				if (rtn!=1)//防止文件存在时，执行下面语句出错
				{
					if (inputStream != null)
						inputStream.close();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		rtn=0;
		return rtn;
	}

	
	
	/**
	 * 根据URL得到输入流
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromURL(String urlStr) 
	{
		
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try 
		{
			url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			inputStream = urlConn.getInputStream();
			
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
}
