package net.gooday2die.navercafealert.NaverAPI;

import net.gooday2die.navercafealert.Common.BanInfo;
import net.gooday2die.navercafealert.Common.Settings;
import net.gooday2die.navercafealert.Common.WarnInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;


/**
 * A class that takes care of everything related to Naver Cafe API.
 */
public class CafeAPI {
    private String authToken;
    private Date lastRefresh;
    private int refreshExpires;
    private boolean isDisabled = false;
    private int totalFailCount = 0;

    /**
     * A constructor method that initializes CafeAPI class.
     * This method will automatically refresh auth token.
     * @throws TokenRefreshFailedException When refreshing token failed with any kind of errors or exceptions.
     */
    public CafeAPI() throws TokenRefreshFailedException {
        try {
            this.refreshAuthToken();
        } catch (TokenRefreshFailedException e) {
            this.isDisabled = true;
            throw e;
        }
    }

    /**
     * A method that refreshes token.
     * Naver API uses auth tokens. These tokens expire in 3600 seconds after the auth token has been issued.
     * In order to keep using Naver auth tokens, we need to refresh auth token by refresh token that was initially issued.
     * For more information about Naver auth API, check <a href="https://developers.naver.com/docs/login/api/api.md">here</a>.
     * @throws TokenRefreshFailedException When refreshing token failed with any kind of errors or exceptions.
     */
    private void refreshAuthToken() throws TokenRefreshFailedException {
        final String clientID = "CV9k94bxxkTqzQrPMzjg"; // The client ID for this plugin.
        // The client secret from Naver API for this plugin. This will be revoked after this plugin has been released.
        final String clientSecret = "JhQ63hsmB_";

        String urlString = "https://nid.naver.com/oauth2.0/token?grant_type=refresh_token&client_id=" + clientID
                + "&client_secret=" + clientSecret + "&refresh_token=" + Settings.cafeRefreshToken;
        StringBuilder result = new StringBuilder();
        try { // Try connecting API and request refresh token.
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open connection
            conn.setRequestMethod("GET"); // Make GET Request.
            try (BufferedReader reader = new BufferedReader( // Try reading results.
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
                JSONObject jsonObj = new JSONObject(result.toString());
                if (conn.getResponseCode() == 200) {
                    // If response code was 200 and everything was ok
                    authToken = jsonObj.getString("access_token"); // Store current access token.
                    lastRefresh = new Date(); // Store current time as last refreshed time.
                    refreshExpires = jsonObj.getInt("expires_in"); // Store when this auth token will be expired.
                }
                else // If response code was not 200, throw exception.
                    throw new TokenRefreshFailedException();
            }
        } catch (IOException | JSONException e) { // If connection failed or was not able to find access_token from result.
            e.printStackTrace();
            totalFailCount++; // Increment failure count.
            if (totalFailCount > Settings.naverMaxFailureCount)
                isDisabled = true;
            throw new TokenRefreshFailedException(); // throw exception.
        }
    }

    /**
     * A method that checks if current refresh token needs refreshing token.
     * When we generate an auth token using refresh token, the generated auth token will be expired in specific time.
     * @return returns true if refreshing token is necessary, false if not.
     */
    private boolean needTokenRefreshing() {
        Date currentTime = new Date();
        long difference = (currentTime.getTime() - lastRefresh.getTime()) / 1000;
        return difference > refreshExpires;
    }

    /**
     * A method that posts article to Naver Cafe for BanInfo.
     * This method will write ban report to Naver Cafe.
     * @param banInfo The BanInfo to report to Naver Cafe.
     * @return The URL that this article was posted.
     * @throws PostFailedException When posting article failed due to any reason.
     * @throws NaverCafeAPIDisabledException When Naver Cafe API was disabled before but was trying to use Naver API.
     * @throws TokenRefreshFailedException When refreshing token failed with any kind of errors or exceptions.
     */
    public String post(BanInfo banInfo)
            throws PostFailedException, NaverCafeAPIDisabledException, TokenRefreshFailedException {
        int failCount = 0;
        while (failCount <= Settings.naverMaxRetryCount) { // Try writing article for naverMaxRetryCount times.
            try {
                return this.writeArticle(Settings.cafeBanBoardId, banInfo.translatedTitle, banInfo.translatedContent);
            } catch (Exception e) {
                e.printStackTrace();
                failCount++;
                Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                        " 카페에 게시글을 작성하지 못했습니다. 재시도중 ..." + ChatColor.GREEN + failCount + ChatColor.WHITE +
                        " / " + Settings.naverMaxRetryCount);
                if (failCount == Settings.naverMaxRetryCount) throw e; // Throw the latest exception if this was last.
            }
        }
        throw new IllegalStateException(); // Code should never hit here.
    }

    /**
     * A method that posts article to Naver Cafe for WarnInfo.
     * This method will write warn report to Naver Cafe.
     * @param warnInfo The WarnInfo to report to Naver Cafe.
     * @return The URL that this article was posted.
     * @throws PostFailedException When posting article failed due to any reason.
     * @throws NaverCafeAPIDisabledException When Naver Cafe API was disabled before but was trying to use Naver API.
     * @throws TokenRefreshFailedException When refreshing token failed with any kind of errors or exceptions.
     */
    public String post(WarnInfo warnInfo)
            throws PostFailedException, NaverCafeAPIDisabledException, TokenRefreshFailedException {
        return this.writeArticle(Settings.cafeWarningBoardId, warnInfo.translatedTitle, warnInfo.translatedContent);
    }

    /**
     * A method that writes article using Naver Cafe API.
     * @param menuId The ID of menu to write article to.
     * @param title The title of article.
     * @param contents The contents of article.
     * @return The URL that this article was posted.
     * @throws PostFailedException When posting article failed to any reason.
     * @throws NaverCafeAPIDisabledException When Naver Cafe API was disabled before but was trying to use Naver API.
     * @throws TokenRefreshFailedException When refreshing token failed with any kind of errors or exceptions.
     */
    public String writeArticle(int menuId, String title, String contents)
            throws PostFailedException, NaverCafeAPIDisabledException, TokenRefreshFailedException {
        if (isDisabled) { // If Naver API was disabled since something went wrong last time.
            throw new NaverCafeAPIDisabledException();
        }

        if (this.needTokenRefreshing()) // Check if token needs refreshing.
            try {
                this.refreshAuthToken();
            } catch (TokenRefreshFailedException e) {
                isDisabled = true;
                throw e;
            }

        // The following code is from Naver API tutorial.
        // For more information, check https://developers.naver.com/docs/login/cafe-api/cafe-api.md#%EC%B9%B4%ED%8E%98-%EA%B0%80%EC%9E%85-%C2%B7-%EA%B8%80%EC%93%B0%EA%B8%B0-api-%EB%AA%85%EC%84%B8
        String header = "Bearer " + authToken; // whitespace after Bearer header.

        try { // try connecting and request POST.
            String strClubId = Integer.toString(Settings.cafeClubId); // Get clubId
            String strMenuId = Integer.toString(menuId);
            String apiURL = "https://openapi.naver.com/v1/cafe/" + strClubId + "/menu/" + strMenuId + "/articles";
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection)url.openConnection(); // Make connection
            con.setRequestMethod("POST"); // Use POST request
            con.setRequestProperty("Authorization", header); // Set Authorization

            // Set title of the article
            String subject = URLEncoder.encode(URLEncoder.encode(title, "UTF-8"), "MS949");
            // Set content of the article
            String content = URLEncoder.encode(URLEncoder.encode(contents, "UTF-8"), "MS949");
            String postParams = "subject=" + subject + "&content=" + content;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

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

            JSONObject message = json.getJSONObject("message");

            // If the HTTP response had key "error" meaning that something went wrong.
            // Naver API returns response code 200 even if it had something wrong going on.
            // The endpoint of "message.error.msg" has the reason of what went wrong.
            if (message.has("error")) {
                JSONObject error = message.getJSONObject("error"); // Get error JSON object
                try {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert] " +
                            ChatColor.WHITE + "네이버 카페 게시글 작성 중 에러가 발생했습니다 : " + error.getString("msg"));
                    throw new PostFailedException(error.getString("msg"));
                } catch (JSONException e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert] " +
                            ChatColor.WHITE + "네이버 카페 게시글 작성 중 에러가 발생했습니다 : " + error.getString("message"));
                    throw new PostFailedException(error.getString("message"));
                }
            } else { //  If everything went ok, return article URL.
                JSONObject result = message.getJSONObject("result");
                return result.getString("articleUrl");
            }
        } catch (Exception e) { // If connecting or requesting POST failed.
            e.printStackTrace();
            totalFailCount++; // Increment failure count.
            if (totalFailCount > Settings.naverMaxFailureCount)
                isDisabled = true;
            throw new PostFailedException(e.getMessage()); // Throw exception.
        }
    }

    /**
     * A method that sets Naver API as enabled.
     */
    public void setEnabled() {
        isDisabled = false;
    }

    /**
     * An Exception class that is thrown when refreshing token failed.
     */
    public static class TokenRefreshFailedException extends RuntimeException {}

    /**
     * An Exception that is thrown when posting to Naver Cafe using API failed.
     */
    public static class PostFailedException extends RuntimeException {
        /**
         * A constructor method for PostFailedException.
         * @param msg The reason why posting article failed.
         */
        public PostFailedException(String msg) {
            super(msg);
        }
    }

    /**
     * An Exception that is thrown when NaverCafeAPI was disabled before but is currently trying to use Naver API.
     */
    public static class NaverCafeAPIDisabledException extends RuntimeException {}
}
