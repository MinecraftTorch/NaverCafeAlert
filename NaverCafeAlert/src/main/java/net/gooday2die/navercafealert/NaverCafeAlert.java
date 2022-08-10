package net.gooday2die.navercafealert;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import net.gooday2die.navercafealert.BanListeners.AdvancedBan;
import net.gooday2die.navercafealert.BanListeners.LiteBans;
import net.gooday2die.navercafealert.Common.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;

public final class NaverCafeAlert extends JavaPlugin {
    /**
     * A private method that detects Ban Managers.
     * Supported Ban managers:
     * - LiteBans from <a href="https://www.spigotmc.org/resources/litebans.3715/">here</a>
     */
    private void detectBanManager() {
        if (Bukkit.getPluginManager().getPlugin("LiteBans") != null) {
            Settings.abstractBanListener = new LiteBans();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert] " +
                    ChatColor.GREEN + "LiteBans" + ChatColor.WHITE + " 플러그인에 연결했습니다.");
        } else if (Bukkit.getPluginManager().getPlugin("AdvancedBan") != null) {
            Settings.abstractBanListener = new AdvancedBan();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert] " +
                    ChatColor.GREEN + "AdvancedBan" + ChatColor.WHITE + " 플러그인에 연결했습니다.");
        }
    }

    private class avls implements Listener {
        @EventHandler
        public void onPunishmentEvent(PunishmentEvent e) {
            System.out.println("PunishmentEvent fired.");
            System.out.println(e.getPunishment());
        }
        @EventHandler
        public void onRevokePunishmentEvent(RevokePunishmentEvent e) {
            System.out.println("RevokePunishmentEvent fired.");
            System.out.println(e.getPunishment());
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
            System.out.println(e.getPlayer().getName());
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                " 플러그인을 활성화중입니다...");

        Settings.thisPlugin = this;
        this.detectBanManager();

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

        Bukkit.getServer().getPluginManager().registerEvents(new avls(), this);

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                " 플러그인을 비활성화중입니다...");
    }
}
