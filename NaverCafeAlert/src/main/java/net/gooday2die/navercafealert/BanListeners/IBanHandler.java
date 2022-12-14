package net.gooday2die.navercafealert.BanListeners;

import net.gooday2die.navercafealert.Common.*;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

public interface IBanHandler {
    /**
     * A method that processes ban by Object provided.
     * This method is expected to generate BanInfo instance and call PostArticle using the BanInfo created.
     * @param object The Object to process ban information.
     */
    void processBan(@Nullable Object object);

    /**
     * A method that processes warn by Object provided.
     * This method is expected to generate WarnInfo instance and call PostArticle using the WarnInfo created.
     * @param object The Object to process warn information.
     */
    void processWarn(@Nullable Object object);

    /**
     * A method that processes mute by Object provided.
     * This method is expected to generate MuteInfo instance and call PostArticle using the MuteInfo created.
     * @param object The Object to process mute information.
     */
    void processMute(@Nullable Object object);

    /**
     * A method that posts article using NaverCafe API.
     * This method will be run async since HTTP request might take some time.
     * If this method is in main thread, this will cause server to wait for the result.
     * @param info The AbstractInfo to post article.
     */
    default void postArticle(AbstractInfo info) {
        new BukkitRunnable() {
            /**
             * An overridden method run for class BukkitRunnable.
             */
            @Override
            public void run() {
                CommandSender issuer = Bukkit.getPlayer(info.executorName);
                if (info.executorName.equals("Console")) issuer = Bukkit.getConsoleSender();

                try { // Try posting article.
                    String cafeURL = "";

                    if (info instanceof BanInfo) { // If this was BanInfo.
                        cafeURL = Settings.cafeAPI.post((BanInfo) info);
                    } else if (info instanceof WarnInfo) { // If this was WarnInfo.
                        cafeURL = Settings.cafeAPI.post((WarnInfo) info);
                    } else if (info instanceof MuteInfo) { // If this was MuteInfo.
                        cafeURL = Settings.cafeAPI.post((MuteInfo) info);
                    }

                    // Print log to console.
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " ????????? ????????? ???????????? ??????????????? : " + ChatColor.GREEN + cafeURL);

                    // Tell command issuer about Cafe URL.
                    assert issuer != null;
                    issuer.sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " ????????? ????????? ???????????? ??????????????? : " + ChatColor.GREEN + cafeURL);

                } catch (CafeAPI.TokenRefreshFailedException e) { // When token refresh failed.
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " token ??? refresh ??? ??? ????????????. config.yml ??? cafeRefreshToken ??? ??????????????????.");

                    // Tell command issuer about Error.
                    assert issuer != null;
                    issuer.sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " token ??? refresh ??? ??? ????????????. config.yml ??? cafeRefreshToken ??? ??????????????????.");
                } catch (CafeAPI.NaverCafeAPIDisabledException e) { // When Naver API got disabled.
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " Naver API ??? ???????????? ????????? ?????? ???????????????. ?????????????????? /ncr reset ??? ????????????.");

                    // Tell command issuer about Error.
                    assert issuer != null;
                    issuer.sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " Naver API ??? ???????????? ????????? ?????? ???????????????. ?????????????????? /ncr reset ??? ????????????.");
                } catch (CafeAPI.PostFailedException e){ // When something went wrong with posting article.
                    e.printStackTrace();

                    // Tell command issuer about Error.
                    assert issuer != null;
                    issuer.sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " ???????????? ????????? ??? ????????????. ?????? ????????? ??????????????????.");
                } catch (AssertionError | NullPointerException ignored) {} // Ignore when user was not found.
            }
        }.runTaskAsynchronously(Settings.thisPlugin);
    }
}
