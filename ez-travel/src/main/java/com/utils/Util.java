package com.utils;

public class Util {

	static public double distance(double lat1, double lon1, double lat2, double lon2) {

		double R = 6378.137; // Radius of earth in KM
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;

		return d * 1000; // meters
	}

	public static String generalizeWeather(String weather) {

		switch (weather.toLowerCase()) {

		// all cases for sunny
		case "clear":
		case "clouds":
		case "mist":
		case "fog":
			return "sunny";

		// all cases for rainy
		case "rain":
		case "drizzle":
		case "thunderstorm":
			return "rainy";

		default:
			System.err.println("-------------------------------------------------------------------------");
			System.err.println("ATTENTION");
			System.err.println("-------------------------------------------------------------------------");
			return weather.toLowerCase() + " - is not recognized as an option!";
		}

	}

}
