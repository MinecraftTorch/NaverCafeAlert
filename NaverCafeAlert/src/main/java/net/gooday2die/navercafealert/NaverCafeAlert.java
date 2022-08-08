package net.gooday2die.navercafealert;

import net.gooday2die.navercafealert.BanListeners.LiteBans;
import net.gooday2die.navercafealert.Common.Settings;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;

public final class NaverCafeAlert extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                " 플러그인을 활성화중입니다...");

        Settings.abstractBanListener = new LiteBans();
        Settings.thisPlugin = this;

        saveDefaultConfig();
        // If forms.yml does not exist, generate one
        if (!new File(Paths.get(Settings.thisPlugin.getDataFolder().getAbsolutePath(), "forms.yml").toString()).exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[KRBanMGR] " +
                    ChatColor.WHITE + "forms.yml 을 찾을 수 없습니다. 새로 작성하고 기본 설정을 사용합니다.");
            saveResource("forms.yml", false);
        }

        // Try loading config.yml and forms.yml
        try {
            Settings.loadSettings();
        } catch (Settings.SettingsInitFailedException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " 설정을 읽어오는데 에러가 발생했습니다. 플러그인을 종료합니다.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                " 플러그인을 비활성화중입니다...");
    }
}
