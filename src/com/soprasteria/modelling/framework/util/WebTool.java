package com.soprasteria.modelling.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.soprasteria.modelling.framework.context.ApplicationContext;

public class WebTool {
	// set timeout in sec
	public final static int timeout = 60;

	public static String curl(String url) throws IOException {
		InputStream inps = null;
		String res = "";
		try {
			URL getURL = new URL(url);

			HttpURLConnection huc = (HttpURLConnection) getURL.openConnection();
			huc.setConnectTimeout(timeout*1000); // 1 minute
			huc.setReadTimeout(timeout*1000); // 1 minute

			huc.setRequestMethod("GET");
			huc.addRequestProperty("User-Agent", "Mozilla/4.76"); 

			inps = huc.getInputStream(); // throws java.io.FileNotFoundException
			res = IOUtils.toString(inps, "UTF8");
		} catch (IOException except) {
			throw except;
		} finally {
			IOUtils.closeQuietly(inps);
		}

		return res;
	}

	public static String getHttpWithParameter(String url,
			Map<String, String> parameters) throws Exception {
		System.out.println("==============get json to " + url);
		CloseableHttpClient httpClient = null;

		try {
			httpClient = HttpClientBuilder.create().build();
			if (parameters != null) {
				List<NameValuePair> getParameters = new ArrayList<>();
				for (Entry<String, String> keyValue : parameters.entrySet()) {
					getParameters.add(new BasicNameValuePair(keyValue.getKey(),
							keyValue.getValue()));
				}
				String paramString = URLEncodedUtils.format(getParameters,
						"utf-8");
				url += "?" + paramString;
			}
			System.out.println(url);
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("error");
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				return output;
			}

		} catch (Throwable ex) {
			System.out.println("error" + ex.getMessage());
			new ApplicationContext().log("POST JSON Exception: "
					+ ex.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static <T> T getAndParseToObject(String url, Class<T> type)
			throws IOException {
		String response = WebTool.curl(url);
		//System.out.println(response);
		Gson gson = new Gson();
		T object = gson.fromJson(response, type);
		return object;
	}

	public static String postJsonWithAuthentication(String url,
			String jsonstring, String key) {
		System.out.println("==============post json to " + url);
		System.out.println("==============post json " + jsonstring);

		int timeout = 10;
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(config).build();

		try {
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(jsonstring);
			request.addHeader("content-type", "application/json");
			request.addHeader("Authorization", key);
			request.setEntity(params);
			System.out.println("post json request: " + request.toString());
			HttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				return output;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			new ApplicationContext().log("POST JSON Exception: "
					+ ex.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String postJson(String url, String jsonstring) {
		System.out.println("==============post json to " + url);
		System.out.println("==============post json " + jsonstring);

		int timeout = 10;
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(config).build();

		try {
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(jsonstring);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			System.out.println("post json request: " + request.toString());
			HttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				return output;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			new ApplicationContext().log("POST JSON Exception: "
					+ ex.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String postHttpWithParameter(String url,
			Map<String, String> parameters) throws Exception {
		System.out.println("==============post json to " + url);
		CloseableHttpClient httpClient = null;

		try {
			httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			request.addHeader("content-type", "application/json");
			List<NameValuePair> postParameters = new ArrayList<>();
			for (Entry<String, String> keyValue : parameters.entrySet()) {
				postParameters.add(new BasicNameValuePair(keyValue.getKey(),
						keyValue.getValue()));
			}
			request.setEntity(new UrlEncodedFormEntity(postParameters));
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("error");
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				return output;
			}

		} catch (Throwable ex) {
			System.out.println("error" + ex.getMessage());
			new ApplicationContext().log("POST JSON Exception: "
					+ ex.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
