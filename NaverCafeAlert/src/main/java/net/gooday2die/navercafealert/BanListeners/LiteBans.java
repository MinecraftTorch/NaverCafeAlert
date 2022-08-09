package net.gooday2die.navercafealert.BanListeners;

import litebans.api.Entry;
import litebans.api.Events;
import net.gooday2die.navercafealert.Common.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Objects;


/**
 * A class that takes care of LiteBans API.
 */
public class LiteBans extends AbstractBanListener {
    private final LiteBansAPIListener apiListener;

    /**
     * A constructor method for class LiteBans.
     */
    public LiteBans() {
        apiListener = new LiteBansAPIListener();
        this.mainEventHandler();
    }

    /**
     * A private method that sets LiteBans Events registered to our LiteBansAPIListener
     */
    @Override
    public void mainEventHandler() {
        Events.get().register(apiListener);
    }

    /**
     * A private class that is for taking care of extending Events.Listener for LiteBans API.
     * This implements IBanAPIListener and extends Events.Listener for LiteBansAPI.
     */
    private static class LiteBansAPIListener extends Events.Listener implements IBanAPIListener {
        /**
         * Called after an entry (ban, mute, warning, kick) is added to the database.
         */
        @Override
        public void entryAdded(Entry entry) {
            if (entry.getType().equals("ban")) { // If this was ban.
                this.processBan(entry);
            } else if (entry.getType().equals("warn")) { // If this was warn.
                this.processWarn(entry);
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
         * @param object The object to process ban.
         */
        public void processBan(@Nullable Object object) {
            Entry entry = (Entry) object;
            String target;

            try { // Try translating UUID into username.
                assert entry != null;
                target = Utils.translateUUIDtoUsername(entry.getUuid());
            } catch (Exception | AssertionError e) { // When any exception happened. (AssertionError should never happen)
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

            if (Settings.cafeBanReportEnabled) this.postArticle(banInfo);
        }

        /**
         * A method that processes warn according to Entry generated.
         * @param object The Entry to process warn.
         */
        public void processWarn(@Nullable Object object) {
            Entry entry = (Entry) object;
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
            Date issuedDate = new Date(entry.getDateStart());
            String ip = entry.getIp();

            WarnInfo warnInfo = new WarnInfo(target, entry.getUuid(), entry.getExecutorName(), entry.getExecutorUUID(),
                    entry.getReason(), ip, issuedDate);

            if (Settings.cafeWarningReportEnabled) this.postArticle(warnInfo); // If warn report was enabled, post it.
        }
    }
}
