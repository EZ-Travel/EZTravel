package com.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exceptions.EventApiException;
import com.pojos.Event;
import com.utils.EventManager;

public class EventApi {

	private static final String Url = "http://api.eventful.com/json/events/search";
	private static final String Key = "LwXLT3RFRrkvFVhp";
	private static final String NUMBER_OF_RESULTS = "10";
	private static final String PREFIX = "eventful_";
	private Client client;
	private WebTarget target;
	private Invocation.Builder builder;
	private Calendar calendar = Calendar.getInstance();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// private EventManager eventManager = EventManager.getManager();

	public EventApi() {
		client = ClientBuilder.newClient();
		target = client.target(Url);
	}

	public Collection<Event> getAllEvents() throws EventApiException {
		WeatherApi weatherApi = new WeatherApi();

		builder = target.queryParam("app_key", Key).queryParam("date", "future")
				.queryParam("page_size", NUMBER_OF_RESULTS).request(MediaType.APPLICATION_JSON);
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
			Collection<Event> internalEvents = new ArrayList<>();

			// convert each event to our internal event object
			for (Object object : arrayOfAllEvents) {
				JSONObject event = new JSONObject(object.toString());
				Event internalEvent = new Event();

				// System.out.println(event);

				internalEvent.setDescription("" + event.get("description"));

				// get the end date of the event
				Object endDate = event.get("stop_time");
				if (!endDate.equals(null)) {
					String endDateString = endDate.toString();
					calendar.set(Calendar.YEAR, Integer.parseInt(endDateString.substring(0, 4)));
					calendar.set(Calendar.MONTH, Integer.parseInt(endDateString.substring(5, 7)) - 1);
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDateString.substring(8, 10)));
					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endDateString.substring(11, 13)));
					calendar.set(Calendar.MINUTE, Integer.parseInt(endDateString.substring(14, 16)));
					internalEvent.setExp_date(calendar.getTime());
				} else {
					// if cant get the end date of the event, get the start date
					// of the event
					String startDate = event.getString("start_time");
					System.out.println();
					calendar.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0, 4)));
					calendar.set(Calendar.MONTH, Integer.parseInt(startDate.substring(5, 7)) - 1);
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDate.substring(8, 10)));
					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startDate.substring(11, 13)));
					calendar.set(Calendar.MINUTE, Integer.parseInt(startDate.substring(14, 16)));
					internalEvent.setExp_date(calendar.getTime());
				}

				// get the image object if available
				Object image = event.get("image");
				JSONObject imageJson = new JSONObject();

				// if (image.has("medium") && !image.isNull("medium")) {
				if (!image.toString().equals("null")) {
					imageJson = event.getJSONObject("image").getJSONObject("medium");
					internalEvent.setImage(imageJson.getString("url"));
				}

				internalEvent.setLat(event.getDouble("latitude"));
				internalEvent.setLon(event.getDouble("longitude"));
				internalEvent.setName("" + event.getString("title"));
				internalEvent.setRadius(getRelevantRadius(internalEvent));
				internalEvent.setVendor_id(PREFIX + event.getString("id"));
				internalEvent.setWeather(weatherApi.getRelevantWeather(internalEvent));

				// see attributes for testing
				// System.out.println("Title");
				// System.out.println(internalEvent.getName());
				// System.out.println("Description");
				// System.out.println(internalEvent.getDescription());
				// System.out.println("Vendor Id");
				// System.out.println(internalEvent.getVendor_id());
				// System.out.println("Expiration Date");
				// System.out.println(internalEvent.getExp_date());
				// System.out.println("lon");
				// System.out.println(internalEvent.getLon());
				// System.out.println("lat");
				// System.out.println(internalEvent.getLat());
				// System.out.println("image");
				// System.out.println(internalEvent.getImage());
				// System.out.println("radius");
				// System.out.println(internalEvent.getRadius());
				// System.out.println("weather");
				// System.out.println(internalEvent.getWeather());
				System.out.println(internalEvent.getWeather());
				internalEvents.add(internalEvent);

			}
			// save the new events in the database
			saveEventsInDatabase(internalEvents);

			return internalEvents;
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

	public double getRelevantRadius(Event event) {
		// to be implemented
		return 0;
	}

	public void saveEventsInDatabase(Collection<Event> events) {
		Collection<Event> allEventsFromDB = EventManager.getManager().findAll();
		boolean exists = false;

		for (Event event : events) {
			exists = false;
			String eventVendor_id = event.getVendor_id();

			// check if the event is already in the db.persist if its not
			for (Event eventFromDB : allEventsFromDB) {

				if (eventVendor_id.equals(eventFromDB.getVendor_id())) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				EventManager.getManager().persistEvent(event);
			}

		}
	}

	public static void main(String[] args) {

		EventApi api = new EventApi();
		try {

			System.out.println(api.getAllEvents());

			api.getAllEvents();

			Collection<Event> allEvents = EventManager.getManager().findAll();

			for (Event event : allEvents) {
				System.out.println(event);
			}

		} catch (JSONException | EventApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}