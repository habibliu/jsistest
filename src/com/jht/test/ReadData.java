package com.jht.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ReadData extends AbstractJavaSamplerClient{

	private static Properties prop=ConfigHelper.getProperties("param");
	/**
	 * 在循环中调用查询接口
	 */
	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		// TODO Auto-generated method stub
		SampleResult sr=new SampleResult();
		sr.setSampleLabel("ReadData");
		try {
			sr.sampleStart();
			execute(prop.getProperty("url_search"),construcSearchtHttpEntity());
			sr.sampleEnd();
			sr.setSuccessful(true);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sr.setSuccessful(false);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sr.setSuccessful(false);
		}
		
		return sr;
	}

	/**
	 * 登录系统
	 */
	@Override
	public void setupTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		super.setupTest(context);
		//对平台发起登录请求
		try {
			execute(prop.getProperty("url_login"),construcSearchtHttpEntity());
			getLogger().info("系统登录成功！");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		super.teardownTest(context);
	}

	/**
	 * API执行方法，此方法是一个模板方法，子类无需实现
	 */
	final public void execute(String url,HttpEntity requstEntity) {
		// TODO Auto-generated method stub
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		CloseableHttpResponse response = null;
		
		try {
			// 发起请求
			long startTime=System.currentTimeMillis();
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url))
					.setEntity(requstEntity)
					.build();
			response = httpclient.execute(requst);
			long endTime=System.currentTimeMillis();
			getLogger().info("本次API调用耗时----->"+(endTime-startTime)/1000+"."+(endTime-startTime)%1000+"秒");
			extractResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	} 
	
	/**
	 * 构造HTTP请求实体
	 * 
	 * @param param
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	protected HttpEntity construcLogintHttpEntity()
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userName", prop.getProperty("userName")));
		list.add(new BasicNameValuePair("password", prop.getProperty("password")));
		list.add(new BasicNameValuePair("check", prop.getProperty("check")));
		
		HttpEntity en = new UrlEncodedFormEntity(list, "UTF-8");
		getLogger().info("登录参数:" + list.toString());
		return en;
	}
	
	protected HttpEntity construcSearchtHttpEntity()
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("_search", prop.getProperty("_search")));
		list.add(new BasicNameValuePair("nd", prop.getProperty("nd")));
		list.add(new BasicNameValuePair("page.pageSize", prop.getProperty("page.pageSize")));
		list.add(new BasicNameValuePair("page.pageIndex", getPageIndex()+""));
		list.add(new BasicNameValuePair("page.sidx", ""));
		list.add(new BasicNameValuePair("page.sord", prop.getProperty("page.pageSize")));
		list.add(new BasicNameValuePair("criterias", prop.getProperty("criterias")));
		HttpEntity en = new UrlEncodedFormEntity(list, "UTF-8");
		getLogger().info("请求参数:" + list.toString());
		return en;
	}
	
	protected void extractResult(CloseableHttpResponse response) throws Exception {
		// TODO Auto-generated method stub
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {//成功调用
			String results = EntityUtils.toString(response.getEntity());
			JsonObject json=new JsonParser().parse(results).getAsJsonObject();
			getLogger().info("请求返回信息："+json);
		} else {
			getLogger().error("执行失败！"+"\tstatusCode:"+statusCode);
		}
	}
	
	private int getPageIndex(){
		int max=200;
        int min=1;
        Random random = new Random();

        int pageIndex = random.nextInt(max - min) + min;
		return pageIndex;
	}
	

}
