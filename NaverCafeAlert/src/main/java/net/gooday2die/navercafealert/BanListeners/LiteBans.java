package net.gooday2die.navercafealert.BanListeners;

import litebans.api.Entry;
import litebans.api.Events;
import net.gooday2die.navercafealert.Common.BanInfo;
import net.gooday2die.navercafealert.Common.Utils;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiteBans extends AbstractBanListener {
    private APIListener apiListener;
    public LiteBans(CafeAPI cafeAPI) {
        super(cafeAPI);
        apiListener = new APIListener();
        this.mainEventHandler();
    }

    @Override
    public void mainEventHandler() {
        Events.get().register(apiListener);
    }

    private class APIListener extends Events.Listener {
        private SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm초");

        /**
         * Called after an entry (ban, mute, warning, kick) is added to the database.
         */
        @Override
        public void entryAdded(Entry entry) {
            System.out.println("ADDED");
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
            System.out.println(entry.toString());

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

            // Generate a BanInfo according to the information from Entry.
            BanInfo banInfo = new BanInfo(target, entry.getUuid(), entry.getExecutorName(), entry.getExecutorUUID(),
                    entry.getReason(), entry.getIp(), start, end, entry.getDuration(), entry.isIpban());

            System.out.println(banInfo);
        }
    }
}
