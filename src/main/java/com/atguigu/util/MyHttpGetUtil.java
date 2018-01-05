package com.atguigu.util;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MyHttpGetUtil {
	public static String doGet(String url)throws Exception {
		
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		
		
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse=null; 
		
		try {
			
			httpResponse=httpClient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
			httpClient.close();
			
			
		}
		
		return null;
	}

}
