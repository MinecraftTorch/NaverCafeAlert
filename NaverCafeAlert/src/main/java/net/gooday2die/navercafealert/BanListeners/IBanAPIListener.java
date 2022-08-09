package net.gooday2die.navercafealert.BanListeners;

import net.gooday2die.navercafealert.Common.AbstractInfo;
import net.gooday2die.navercafealert.Common.BanInfo;
import net.gooday2die.navercafealert.Common.Settings;
import net.gooday2die.navercafealert.Common.WarnInfo;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface IBanAPIListener {
    /**
     * A method that processes ban by Object provided.
     * @param o The Object to process ban information.
     */
    void processBan(@Nullable Object o);

    /**
     * A method that processes warn by Object provided.
     * @param o The Object to process warn information.
     */
    void processWarn(@Nullable Object o);

    /**
     * A method that posts article using NaverCafe API.
     * @param info The AbstractInfo to post article.
     */
    default void postArticle(AbstractInfo info) {
        Player issuer = Bukkit.getPlayer(info.executorName);

        try { // Try posting article.
            String cafeURL = "";

            if (info instanceof BanInfo) { // If this was BanInfo.
                cafeURL = Settings.cafeAPI.post((BanInfo) info);
            } else if (info instanceof WarnInfo) { // If this was WarnInfo.
                cafeURL = Settings.cafeAPI.post((WarnInfo) info);
            }

            // Print log to console.
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " 네이버 카페에 게시글을 올렸습니다 : " + ChatColor.GREEN + cafeURL);

            // Tell command issuer about Cafe URL.
            assert issuer != null;
            issuer.sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " 네이버 카페에 게시글을 올렸습니다 : " + ChatColor.GREEN + cafeURL);

        } catch (CafeAPI.TokenRefreshFailedException e) { // When token refresh failed.
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " token 을 refresh 할 수 없습니다. config.yml 의 cafeRefreshToken 을 확인해주세요.");

            // Tell command issuer about Error.
            assert issuer != null;
            issuer.sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " token 을 refresh 할 수 없습니다. config.yml 의 cafeRefreshToken 을 확인해주세요.");
        } catch (CafeAPI.NaverCafeAPIDisabledException e) { // When Naver API got disabled.
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " Naver API 를 사용하며 에러가 너무 많았습니다. 재사용하려면 /ncr reset 을 해주세요.");

            // Tell command issuer about Error.
            assert issuer != null;
            issuer.sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " Naver API 를 사용하며 에러가 너무 많았습니다. 재사용하려면 /ncr reset 을 해주세요.");
        } catch (CafeAPI.PostFailedException e){ // When something went wrong with posting article.
            e.printStackTrace();

            // Tell command issuer about Error.
            assert issuer != null;
            issuer.sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                    " 게시글을 작성할 수 없습니다. 콘솔 로그를 확인해주세요.");
        } catch (AssertionError | NullPointerException ignored) {} // Ignore when user was not found.
    }
}
