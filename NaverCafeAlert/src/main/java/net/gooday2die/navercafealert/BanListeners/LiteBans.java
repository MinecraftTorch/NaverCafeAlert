package net.gooday2die.navercafealert.BanListeners;

import litebans.api.Entry;
import litebans.api.Events;
import net.gooday2die.navercafealert.Common.BanInfo;
import net.gooday2die.navercafealert.Common.Settings;
import net.gooday2die.navercafealert.Common.Utils;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LiteBans extends AbstractBanListener {
    private final APIListener apiListener;
    public LiteBans() {
        apiListener = new APIListener();
        this.mainEventHandler();
    }

    @Override
    public void mainEventHandler() {
        Events.get().register(apiListener);
    }

    private static class APIListener extends Events.Listener {
        /**
         * Called after an entry (ban, mute, warning, kick) is added to the database.
         */
        @Override
        public void entryAdded(Entry entry) {
            if (entry.getType().equals("ban")) {
                this.processBan(entry);
            }
        }

        /**
         * Called after an entry (ban, mute, warning, kick) is removed from the database.
         */
        @Override
        public void entryRemoved(Entry entry) {
            System.out.println("REMOVAL");
            System.out.println(entry.getType());
            System.out.println(entry);

            String issuer = entry.getExecutorName();
            String issuerUUID = entry.getExecutorUUID();
            String target = entry.getUuid();
            String reason = entry.getRemovalReason();

            System.out.println(issuer);
            System.out.println(issuerUUID);
            System.out.println(target);
            System.out.println(reason);
        }

        /**
         * A method that processes ban according to Entry generated.
         * @param entry The Entry to process ban.
         */
        private void processBan(Entry entry) {
            String target;

            try { // Try translating UUID into username.
                 target = Utils.translateUUIDtoUsername(entry.getUuid());
            } catch (Exception e) { // When any exception happened.
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                        " 에러가 발생했습니다. 해당 에러를 신고해주세요!");
                target = "알수없음";
            }

            // Store start, end date as Date format.
            Date start = new Date(entry.getDateStart());
            Date end = new Date(entry.getDateEnd());

            // Check IP for unknown IPs.
            String ip = (Objects.equals(entry.getIp(), "#") || entry.getIp() == null) ? "알수없음" : entry.getIp();

            // Generate a BanInfo according to the information from Entry.
            BanInfo banInfo = new BanInfo(target, entry.getUuid(), entry.getExecutorName(), entry.getExecutorUUID(),
                    entry.getReason(), ip, start, end, entry.getDuration(), entry.isIpban());

            if (Settings.cafeBanReportEnabled) {
                // If ban report was enabled.
                try {
                    String cafeURL = Settings.cafeAPI.post(banInfo);
                    // Print log to console.
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " 네이버 카페에 게시글을 올렸습니다 : " + ChatColor.GREEN + cafeURL);

                    // Tell command issuer about Cafe URL.
                    assert entry.getExecutorName() != null;
                    Player issuer = Bukkit.getPlayer(entry.getExecutorName());
                    assert issuer != null;
                    issuer.sendMessage(ChatColor.GOLD + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " 네이버 카페에 게시글을 올렸습니다 : " + ChatColor.GREEN + cafeURL);
                } catch (CafeAPI.TokenRefreshFailedException e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " token 을 refresh 할 수 없습니다. config.yml 의 cafeRefreshToken 을 확인해주세요.");
                } catch (CafeAPI.NaverCafeAPIDisabledException e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NaverCafeAlert]" + ChatColor.WHITE +
                            " Naver API 를 사용하며 에러가 너무 많았습니다. 재사용하려면 /ncr reload 를 해주세요.");
                } catch (CafeAPI.PostFailedException e){
                    e.printStackTrace();
                } catch (NullPointerException | AssertionError ignored) {} // Ignore exceptions with issuer not found.
            }

        }
    }
}
