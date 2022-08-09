package net.gooday2die.navercafealert.Common;

import java.time.Duration;
import java.util.Date;


/**
 * A class that stores ban information.
 */
public class BanInfo extends AbstractInfo {
    public String ip;
    public Date banExpires;
    public Date banStarts;
    public long duration;
    public boolean isIpBan;

    /**
     * A constructor method for class BanInfo.
     * @param targetName The target's name.
     * @param targetUUID The target's UUID.
     * @param executorName The executor's name.
     * @param executorUUID The executor's UUID.
     * @param reason The reason for unban.
     * @param ip The IP of target.
     * @param banExpires The Date when ban expires.
     * @param banStarts The Date when ban starts.
     * @param duration The long type duration.
     * @param isIpBan If this is an IP ban or not.
     */
    public BanInfo(String targetName, String targetUUID, String executorName, String executorUUID, String reason,
                   String ip, Date banExpires, Date banStarts, long duration, boolean isIpBan) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.executorName = executorName;
        this.executorUUID = executorUUID;
        this.reason = reason;
        this.ip = ip;
        this.banExpires = banExpires;
        this.banStarts = banStarts;
        this.duration = duration;
        this.isIpBan = isIpBan;

        this.translateValues();
    }

    /**
     * A protected method that translates title and content automatically.
     */
    @Override
    protected void translateValues() {
        translatedContent = this.translateString(Settings.forms.get("banReport"));
        translatedTitle = this.translateString(Settings.formTitles.get("banReport"));
    }

    /**
     * A protected method that translates originalString into String using placeholders.
     * @param originalString The original String to replace placeholders.
     * @return The translated String.
     */
    @Override
    protected String translateString(String originalString) {
        String outString = originalString;

        // There are two types of banning. IP ban and normal ban.
        if (isIpBan) {
            outString = outString.replace("%type%", "IP 밴");
            outString = outString.replace("%targetName%", ip);
        }
        else {
            outString = outString.replace("%type%", "일반 밴");
            outString = outString.replace("%targetName%", targetName);
        }

        // Replace other placeholders.
        outString = outString.replace("%reason%", reason);
        outString = outString.replace("%targetUUID%", targetUUID);
        outString = outString.replace("%executorName%", executorName);
        outString = outString.replace("%executorUUID%", executorUUID);
        outString = outString.replace("%ip%", ip);

        // Replace ban specific placeholders.
        if (duration == -1) { // If duration was -1, this is permanent ban.
            outString = outString.replace("%duration%", "영구밴");
            outString = outString.replace("%banExpires%", "해당 없음");
            outString = outString.replace("%banStarts%", Settings.df.format(new Date()));
        } else { // If this was normal ban, calculate duration using Duration.
            long days = Duration.ofSeconds(duration / 1000).toDays(); // Get days of duration.
            long minutes = Duration.ofSeconds((duration / 1000) - days * 86400).toMinutes(); // Get remaining minutes of duration.
            String durationString = String.format("%d일 %d분", days, minutes);

            // Replace placeholders.
            outString = outString.replace("%duration%", durationString);
            outString = outString.replace("%banExpires%", Settings.df.format(banExpires));
            outString = outString.replace("%banStarts%", Settings.df.format(banStarts));
        }
        return outString;
    }
}
