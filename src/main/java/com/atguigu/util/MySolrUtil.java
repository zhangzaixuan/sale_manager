package com.atguigu.util;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class MySolrUtil {
	static HttpSolrServer solar=null;
	static {
		solar=new HttpSolrServer(MyPorpertiesUtil.getMypath("solar.properties", "sku_solar"));
		solar.setParser(new XMLResponseParser());
		solar.setConnectionTimeout(10000000);
		
	}
	public static HttpSolrServer getMysolar() {
		
		return solar;
	}

}
