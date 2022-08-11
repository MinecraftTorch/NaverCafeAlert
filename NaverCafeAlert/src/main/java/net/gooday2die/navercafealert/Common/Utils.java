package net.gooday2die.navercafealert.Common;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HexFormat;
import java.util.UUID;

public class Utils {
    /**
     * A static method that translates UUID into username by Mojang's API.
     * Since almost every method call in this method has potential to throw Exceptions, this method throws exceptions
     * when Exception happened. Rather than having to try, catch every single line after line to avoid Exceptions.
     * @param uuid The uuid String to look for username. If uuid could not be parsed, returns "알수없음"
     * @return The username string if found.
     * @throws IOException When URL creation failed, or connection to API failed.
     * @throws JSONException When response String was not able to be parsed.
     */
    public static String translateUUIDtoUsername(String uuid) throws IOException, JSONException {
        try {
            UUID tmpUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) { // If this uuid is not valid, return unknown.
            return "알수없음";
        }

        String urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid; // Generate API URI.

        // Connect URL using HttpURLConnection.
        URL url = new URL(urlString); // Might throw MalformedURLException which is IOException.
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); // Might throw IOException.
        JSONObject jsonObject = parseOutput(con);

        return jsonObject.getString("name");
    }

    /**
     * A static method that translates username into UUID using Mojang's API.
     * Since almost every method call in this method has potential to throw Exceptions, this method throws exceptions
     * when Exception happened. Rather than having to try, catch every single line after line to avoid Exceptions.
     * @param username The username to look for UUID.
     * @return String object that represents user's UUID.
     * @throws IOException When URL creation failed, or connection to API failed.
     * @throws JSONException When response String was not able to be parsed.
     * @throws IllegalArgumentException When UUID could not be parsed.
     */
    public static String translateUsernameToUUID(String username) throws IOException, JSONException, IllegalArgumentException {
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + username; // set API URI.

        // Connect URL using HttpURLConnection.
        URL url = new URL(urlString); // Might throw MalformedURLException which is IOException.
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); // Might throw IOException.

        // Try parsing result.
        JSONObject jsonObject = parseOutput(con);

        // Parse UUID using regex since Mojang returns UUID with no dashes.
        // Check https://stackoverflow.com/questions/18986712/creating-a-uuid-from-a-string-with-no-dashes for more info.
        return UUID.fromString(
                jsonObject.getString("id")
                        .replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                        )
        ).toString();
    }

    /**
     * A static method that parses output from HttpURLConnection object after query (POST or GET)
     * This will generate JSONObject from the request.
     * @param con HttpURLConnection That stores connection information.
     * @return JSONObject that represents the parsed output.
     * @throws IOException When URL creation failed, or connection to API failed.
     * @throws JSONException When response String was not able to be parsed.
     */
    public static JSONObject parseOutput(HttpURLConnection con) throws IOException, JSONException {
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

        // There is something wierd happening with Naver Cafe API.
        // When request failed, the results come as XML format.
        // However, when the request was successfully processed, it comes as json format.
        // Thus, first check if JSON is empty when generated by XML.toJsonObject
        // If it was empty, it means that the result was in json format. Thus generate JSONObject instance.
        // Translate that result into JSONObject for ease of use.
        JSONObject json = XML.toJSONObject(response.toString());
        if (json.length() == 0){
            json = new JSONObject(response.toString());
        }

        // Try generating JSON object using response from request.
        return json;
    }
}
