package com.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exceptions.EventApiException;

public class EventApi {

	private static final String Url = "http://api.eventful.com/json/events/search";
	private static final String Key = "LwXLT3RFRrkvFVhp";
	private static final String numberOfResults = "5";
	private Client client;
	private WebTarget target;
	private Invocation.Builder builder;

	public EventApi() {
		client = ClientBuilder.newClient();
		target = client.target(Url);
	}

	public String getAllEvents() throws EventApiException {
		builder = target.queryParam("app_key", Key).queryParam("date", "future")
				.queryParam("page_size", numberOfResults).queryParam("page_number", numberOfResults)
				.request(MediaType.APPLICATION_JSON);
		String res = builder.get().readEntity(String.class);
		// System.out.println(res);
		JSONObject obj = new JSONObject(res);
		// System.out.println(res);
		if (obj.get("total_items").equals("0")) {
			throw new EventApiException("No events found!", null);
		} else {
			// get the event object containing the array of events
			JSONObject jsonObject = obj.getJSONObject("events");
			// get the array of all events
			JSONArray arrayOfAllEvents = jsonObject.getJSONArray("event");

			return arrayOfAllEvents.toString();
		}
	}

	// is needed?
	public String getEventsByLocation(int lon, int lat) throws EventApiException {
		builder = target.queryParam("app_key", Key).queryParam("location", "(\"" + lon + "," + lat + "\")")
				.request(MediaType.APPLICATION_JSON);
		String res = builder.get().readEntity(String.class);
		System.out.println(res);
		JSONObject obj = new JSONObject(res);
		// System.out.println(res);
		if (obj.get("total_items").equals("0")) {
			throw new EventApiException("No events found for lon=" + lon + " ,lat=" + lat, null);
		} else {
			// get the event object containing the array of events
			JSONObject jsonObject = obj.getJSONObject("events");
			// get the array of all events
			JSONArray arrayOfAllEvents = jsonObject.getJSONArray("event");

			return arrayOfAllEvents.toString();
		}
	}

	public static void main(String[] args) {

		EventApi api = new EventApi();

		JSONArray arrayOfAllEvents = new JSONArray();
		try {
			arrayOfAllEvents = new JSONArray(api.getAllEvents());
		} catch (JSONException | EventApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Object object : arrayOfAllEvents) {
			JSONObject event = new JSONObject(object.toString());
			System.out.println("ID");
			System.out.println("--------------------");
			System.out.println(event.get("id"));
			System.out.println("TITLE");
			System.out.println("--------------------");
			System.out.println(event.get("title"));
			System.out.println("START TIME");
			System.out.println("--------------------");
			System.out.println(event.get("start_time"));
			System.out.println("------------------------------------------");

		}
	}
}