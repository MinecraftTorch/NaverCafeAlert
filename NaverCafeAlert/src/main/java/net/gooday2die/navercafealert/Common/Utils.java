package net.gooday2die.navercafealert.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    /**
     * A static method that translates UUID into username by Mojang's API.
     * Since almost every method call in this method has potential to throw Exceptions, this method throws exceptions
     * when Exception happened. Rather than having to try, catch every single line after line to avoid Exceptions.
     * @param uuid The uuid String to look for username.
     * @return The username string if found.
     * @throws IOException When URL creation failed, or connection to API failed.
     * @throws JSONException When response String was not able to be parsed.
     */
    public static String translateUUIDtoUsername(String uuid) throws IOException, JSONException {
        String urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid; // Generate APi URI.

        // Connect URL using HttpURLConnection.
        URL url = new URL(urlString); // Might throw MalformedURLException which is IOException.
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); // Might throw IOException.

        // For storing response from request.
        StringBuilder response = new StringBuilder();

        // Try filling in response.
        if (con.getResponseCode() == 200) { // Might throw IOException. When response was successfully processed.
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));  // Might throw IOException.
            for (String line; (line = reader.readLine()) != null; ) {
                response.append(line);
            }
        } else { // When something went wrong.
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));  // Might throw IOException.
            for (String line; (line = reader.readLine()) != null; ) {
                response.append(line);
            }
        }

        // Try generating JSON object using response from request.
        JSONObject json = new JSONObject(response.toString());  // Might throw JSONException.
        return json.getString("name"); // Might throw JSONException.
    }
}
