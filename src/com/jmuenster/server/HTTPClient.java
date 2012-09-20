package com.jmuenster.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * @author Julian Munster
 *
 */
public class HTTPClient {

	// httpClient field holds an http client instance
	private HttpClient httpclient;
	
	/**
	 * The Constructor initiates the httpclient field.
	 */
	public HTTPClient () {
		httpclient = new DefaultHttpClient();
	}
	
	/**
	 * This method connects to the server and sends it a
	 * get (post) call. This can have any parameter passed 
	 * in as part of the call to cut down on the notes returned.
	 * Then it returns the response of the get.
	 * 
	 * @param url the url of the HttpGet call
	 * @param search a search string
	 * @return response
	 * 
	 */
	public HttpResponse httpGet(String url, String search) {
        HttpResponse response = null;
		try {
	        HttpGet httpget = new HttpGet(url+search);
			httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
	        response = httpclient.execute(httpget);
        } catch (Exception e) {e.printStackTrace();}
        return response;
	}
	
	/**
	 * This method connects to the server and sends it a
	 * post call. This must have every parameter passed in as 
	 * part of the call even if they are an empty String or 0.
	 * Then it returns the response of the post.
	 * 
	 * @param url the url of the HttpPost call
	 * @param j the json object that gets attached
	 * @return response
	 * 
	 */
	public HttpResponse httpPost(String url, String j) {
        HttpResponse response = null;
        try {
			StringEntity s = new StringEntity(j.toString());
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setEntity(s);
            response = httpclient.execute(httppost);
        } catch (Exception e) {e.printStackTrace();}
        return response;
	}
	
	/**
	 * This method connects to the server and sends it a
	 * put call. This must have every parameter passed in 
	 * that needs to be updated and the unique_id field. 
	 * Then it returns the response of the put.
	 * 
	 * @param url the url of the HttpPut call
	 * @param j the json object that gets attached
	 * @return response
	 * 
	 */
	public HttpResponse httpPut(String url, JSONObject j) {
        HttpResponse response = null;
        try {
			StringEntity s = new StringEntity(j.toString());
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            HttpPut httpput = new HttpPut(url);
            httpput.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpput.setEntity(s);
            response = httpclient.execute(httpput);
        } catch (Exception e) {e.printStackTrace();}
        return response;
	}
	
	/**
	 * This method connects to the server and sends it a
	 * delete call. This must have the unique_id field.
	 * Then it returns the response of the delete.
	 * 
	 * @param url the url of the HttpDelete call
	 * @return response
	 * 
	 */
	public HttpResponse httpDelete(String url, String id) {
        HttpResponse response = null;
        try {
            HttpDelete httpdelete = new HttpDelete(url+id);
            httpdelete.setHeader("Content-Type", "application/x-www-form-urlencoded");
            response = httpclient.execute(httpdelete);
        } catch (Exception e) {e.printStackTrace();}
        return response;
	}
}
