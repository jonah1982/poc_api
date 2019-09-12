package com.soprasteria.project.momcc;

import java.io.IOException;
//// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Azure {
	public static String BASE_URL = "https://southeastasia.api.cognitive.microsoft.com";
	public static String QA_URL = "https://mamatest.azurewebsites.net/qnamaker";

	public static void main(String[] args) throws ParseException, URISyntaxException, IOException, JSONException {
		System.out.println(getCentiment("hello world"));
		System.out.println(getKeyPhrases("hello world"));
		System.out.println(getAnswer("Passport Lost").toString(2));
		// Request body
		/**
		 *
		 * { "documents": [ { "id": "1", "text": "Hello world" }, { "id": "2", "text":
		 * "Bonjour tout le monde" }, { "id": "3", "text": "La carretera estaba
		 * atascada. Hab¨ªa mucho tr¨¢fico el d¨ªa de ayer." }, { "id": "4", "text": ":) :(
		 * :D" } ] }
		 */

	}

	/**
	 * 
	 * @param msg - the message to be analysed
	 * @return score
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ParseException
	 * @throws JSONException 
	 */
	public static double getCentiment(String msg) throws URISyntaxException, ParseException, IOException, JSONException {
		HttpPost request = new HttpPost(new URI(BASE_URL + "/text/analytics/v2.0/sentiment"));
		
		JSONObject obj = new JSONObject();
		JSONArray documents = new JSONArray();
		JSONObject textObject = new JSONObject();
		textObject.put("id", "1");
		textObject.put("text", msg);
		documents.put(0, textObject);
		obj.put("documents", documents);

		StringEntity reqEntity = new StringEntity(obj.toString());


		request.setHeader("Content-Type", "application/json");
		request.setHeader("Ocp-Apim-Subscription-Key", "47120f8dd5934d44a90a24d34f5b7a3f");

		request.setEntity(reqEntity);

		HttpClient httpclient = HttpClients.createDefault();
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		JSONObject jr = new JSONObject(EntityUtils.toString(entity));
		
		return ((Double)((JSONObject)((JSONArray)jr.get("documents")).get(0)).get("score"));

	}
	
	public static JSONArray getKeyPhrases(String msg) throws URISyntaxException, ClientProtocolException, IOException, JSONException{
		HttpPost request = new HttpPost(new URI(BASE_URL + "/text/analytics/v2.0/keyPhrases"));
		
		JSONObject obj = new JSONObject();
		JSONArray documents = new JSONArray();
		JSONObject textObject = new JSONObject();
		textObject.put("id", "1");
		textObject.put("text", msg);
		documents.put(0, textObject);
		obj.put("documents", documents);

		StringEntity reqEntity = new StringEntity(obj.toString());


		request.setHeader("Content-Type", "application/json");
		request.setHeader("Ocp-Apim-Subscription-Key", "47120f8dd5934d44a90a24d34f5b7a3f");

		request.setEntity(reqEntity);

		HttpClient httpclient = HttpClients.createDefault();
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		JSONObject jr = new JSONObject(EntityUtils.toString(entity));
		
		JSONArray _result = jr.getJSONArray("documents").getJSONObject(0).getJSONArray("keyPhrases");
		return _result;
	}
	
	public static JSONArray getAnswer(String msg) throws URISyntaxException, ClientProtocolException, IOException, JSONException{
		HttpPost request = new HttpPost(new URI(QA_URL + "/knowledgebases/659d095e-c64d-4afe-b975-8443c57ede92/generateAnswer"));
		
		JSONObject obj = new JSONObject();
		obj.put("question", msg);

		StringEntity reqEntity = new StringEntity(obj.toString());


		request.setHeader("Content-Type", "application/json");
		request.setHeader("Authorization", "EndpointKey 74aecda0-e6df-4851-9090-d21df5370741");

		request.setEntity(reqEntity);

		HttpClient httpclient = HttpClients.createDefault();
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		JSONObject jr = new JSONObject(EntityUtils.toString(entity));
		System.out.println("112\n"+jr);
		JSONArray _result = jr.getJSONArray("answers");
		return _result;
	}
}
