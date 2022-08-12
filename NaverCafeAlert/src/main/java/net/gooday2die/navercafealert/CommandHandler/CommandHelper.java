package net.gooday2die.navercafealert.CommandHandler;

import net.gooday2die.navercafealert.Common.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * A class that helps command execution.
 */
public class CommandHelper {
    /**
     * A method that sends help message to sender.
     * @param sender The CommandSender who issued command.
     */
    public void showHelp(CommandSender sender) {
        String message = ChatColor.GOLD + "-----=[ NaverCafeAlert ]=-----\n" +
                ChatColor.GREEN + "/nca help" + ChatColor.WHITE + ": 이 페이지를 출력합니다.\n" +
                ChatColor.GREEN + "/nca reload" + ChatColor.WHITE + ": 플러그인을 reload 합니다.\n" +
                ChatColor.GREEN + "/nca reset" + ChatColor.WHITE + ": 네이버 카페의 실패 횟수를 리셋합니다.\n";
        sender.sendMessage(message);
    }

    /**
     * A method that resets Naver cafe as enabled.
     * When Naver cafe exceeds max error count, it gets disabled.
     * This method will reset it so that it can be used again.
     * @param sender The CommandSender who issued command.
     */
    public void reset(CommandSender sender) {
        Settings.cafeAPI.setEnabled();
        sender.sendMessage(ChatColor.GOLD + "[NaverCafeAlert] " +
                ChatColor.WHITE + "네이버 API 를 초기화 했습니다. 이제 다시 사용 가능합니다.");
    }

    /**
     * A method that reloads config.yml and forms.yml
     * @param sender The CommandSender who issued command.
     */
    public void reload(CommandSender sender) {
        try { // Try reloading files.
            Settings.loadSettings();
            sender.sendMessage(ChatColor.GOLD + "[NaverCafeAlert] " +
                    ChatColor.WHITE + "플러그인을 reload 했습니다.");
        } catch (Settings.SettingsInitFailedException e) { // If exception occurred, disable plugin.
            sender.sendMessage(ChatColor.RED + "[NaverCafeAlert] " +
                    ChatColor.WHITE + "플러그인을 reload 할 수 없습니다. 플러그인을 종료합니다.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Settings.thisPlugin); // Disable this plugin.
        }
    }
}
