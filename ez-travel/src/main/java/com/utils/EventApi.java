package com.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventApi {

	private static final String Url = "http://api.eventful.com/json/events/search";
	private static final String Key = "LwXLT3RFRrkvFVhp";
	private static final String numberOfResults = "90";
	private Client client;
	private WebTarget target;
	private Invocation.Builder builder;

	public EventApi() {
		client = ClientBuilder.newClient();
		target = client.target(Url);
	}

	public String getAllEvents() {
		builder = target.queryParam("app_key", Key).queryParam("date", "future")
				.queryParam("page_size", numberOfResults).queryParam("page_number", numberOfResults)
				.request(MediaType.APPLICATION_JSON);
		String res = builder.get().readEntity(String.class);
		System.out.println(res);
		JSONObject obj = new JSONObject(res);
		// System.out.println(res);
		if (obj.get("total_items").equals("0")) {
			return "No events available - check for errors!";
		} else {
			// get the event object containing the array of events
			JSONObject jsonObject = obj.getJSONObject("events");
			// get the array of all events
			JSONArray arrayOfAllEvents = jsonObject.getJSONArray("event");

			return arrayOfAllEvents.toString();
		}
	}

	// is needed?
	public String getEventsByLocation(int lon, int lat) {
		builder = target.queryParam("app_key", Key).queryParam("location", "(\"" + lon + "," + lat + "\")")
				.request(MediaType.APPLICATION_JSON);
		String res = builder.get().readEntity(String.class);
		System.out.println(res);
		JSONObject obj = new JSONObject(res);
		// System.out.println(res);
		if (obj.get("total_items").equals("0")) {
			return "No events available for this area";
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

		JSONArray arrayOfAllEvents = new JSONArray(api.getAllEvents());
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
