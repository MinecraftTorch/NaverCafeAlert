package net.gooday2die.navercafealert.Common;

import net.gooday2die.navercafealert.BanListeners.AbstractBanListener;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * A class that loads and stores values from config.yml and forms.yml
 */
public class Settings {
    static public String cafeRefreshToken;
    static public int cafeClubId;
    static public int cafeBanBoardId;
    static public int cafeWarningBoardId;
    static public boolean cafeBanReportEnabled;
    static public boolean cafeWarningReportEnabled;
    static public int naverMaxRetryCount;
    static public int naverMaxFailureCount;
    static public Map<String, String> forms = new HashMap<>(); // For storing forms from forms.yml.
    static public Map<String, String> formTitles = new HashMap<>(); // For storing titles from forms.yml.
    static public JavaPlugin thisPlugin; // For storing this plugin.
    static public AbstractBanListener abstractBanListener; // For storing AbstractBanListener for ban plugin.
    static public CafeAPI cafeAPI; // For storing Naver Cafe API.
    static public SimpleDateFormat df;  // For storing date format.


    /**
     * A static method that loads config.yml and forms.yml
     *
     * @throws SettingsInitFailedException
     */
    public static void loadSettings() throws SettingsInitFailedException {
        Path formsPath = Paths.get(Settings.thisPlugin.getDataFolder().getAbsolutePath(), "forms.yml");

        loadConfig();
        loadForms(formsPath);
        Settings.cafeAPI = new CafeAPI();
    }

    /**
     * A static method for loading config.yml
     */
    private static void loadConfig() {
        FileConfiguration config = Settings.thisPlugin.getConfig(); // Load config
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[NaverCafeAlert]" + ChatColor.WHITE + "config.yml 을 불러오는 중입니다...");

        // Load dateFormat from config.yml
        String dateFormat = config.getString("dateFormat");
        try { // Try loading dateFormat from config.yml and set it as dateFormat.
            assert dateFormat != null;
            df = new SimpleDateFormat(dateFormat);
        } catch (AssertionError | IllegalArgumentException e) { // This means dateFormat was invalid or was null.
            df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초"); // Use default in that case.
        }

        Settings.cafeRefreshToken = config.getString("cafeRefreshToken");
        Settings.cafeClubId = config.getInt("cafeClubId");
        Settings.cafeBanReportEnabled = config.getBoolean("cafeBanReportEnabled");
        Settings.cafeBanBoardId = config.getInt("cafeBanBoardId");
        Settings.cafeWarningReportEnabled = config.getBoolean("cafeWarningReportEnabled");
        Settings.cafeWarningBoardId = config.getInt("cafeWarningBoardId");
        Settings.naverMaxRetryCount = config.getInt("naverMaxRetryCount");
        Settings.naverMaxFailureCount = config.getInt("naverMaxFailureCount");

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[NaverCafeAlert]" + ChatColor.WHITE + "config.yml 을 성공적으로 불러왔습니다.");
    }

    /**
     * A static method that loads forms.yml.
     * @param formsPath the path to forms.yml
     * @throws SettingsInitFailedException When forms.yml was not located. Which shall never happen.
     */
    private static void loadForms(Path formsPath) throws SettingsInitFailedException {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert] " +
                ChatColor.WHITE + "forms.yml 을 불러오는 중입니다...");

        // Load forms.yml
        try { // Try loading forms.yml
            YamlConfiguration formData = YamlConfiguration.loadConfiguration(new FileReader(formsPath.toString()));
            Set<String> formNames = formData.getKeys(false); // Get form names
            for (String formName : formNames) {
                String title = formData.getString(formName + ".title"); // Get title
                List<String> lines = formData.getStringList(formName + ".lines"); // Get lines of content
                StringBuilder body = new StringBuilder();

                for (String line : lines) { // For all lines, add that line to totalContent
                    body.append(line).append("<br>"); // Since we are dealing with html, use <br> for new line.
                }
                Settings.forms.put(formName, body.toString()); // Save body data.
                Settings.formTitles.put(formName, title); // Save title for form.
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[NaverCafeAlert]" + ChatColor.WHITE +
                    ChatColor.WHITE + "forms.yml 을 성공적으로 불러왔습니다.");
        } catch (FileNotFoundException e) {  // This should never happen.
            e.printStackTrace();
            throw new SettingsInitFailedException();
        }
    }

    public static class SettingsInitFailedException extends Exception {
    }
}