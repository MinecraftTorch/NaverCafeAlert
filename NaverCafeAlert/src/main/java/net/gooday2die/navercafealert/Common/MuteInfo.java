package net.gooday2die.navercafealert.Common;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;


/**
 * A class that stores mute information.
 */
public class MuteInfo extends AbstractInfo {
    public Date muteExpires;
    public Date muteStarts;
    public long duration;

    /**
     * A constructor method for class MuteInfo
     * @param targetName The target's name.
     * @param targetUUID The target's UUID.
     * @param executorName The executor's name.
     * @param executorUUID The executor's UUID.
     * @param reason The reason for unban.
     * @param ip The IP of target.
     * @param muteExpires The Date when ban expires.
     * @param muteStarts The Date when ban starts.
     * @param duration The long type duration.
     * @param isIPMute If this was IP mute or not.
     */
    public MuteInfo(String targetName, String targetUUID, String executorName, String executorUUID, String reason,
                    String ip, Date muteExpires, Date muteStarts, long duration, boolean isIPMute) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.executorName = executorName;
        this.executorUUID =executorUUID;
        this.reason = reason;
        this.ip = ip;
        this.muteExpires = muteExpires;
        this.muteStarts = muteStarts;
        this.duration = duration;
        this.isIpPunishment = isIPMute;

        this.translateValues();
    }

    /**
     * A protected method that translates title and content automatically.
     */
    @Override
    protected void translateValues() {
        translatedContent = this.translateString(Settings.forms.get("muteReport"));
        translatedTitle = this.translateString(Settings.formTitles.get("muteReport"));
    }

    /**
     * A protected method that translates originalString into String using placeholders.
     * @param originalString The original String to replace placeholders.
     * @return The translated String.
     */
    @Override
    protected String translateString(String originalString) {
        String outString = originalString;

        // mute might be by IP or username.
        if (!isIpPunishment) {
            outString = outString.replace("%target%", targetName);
            outString = outString.replace("%type%", "유저 채팅차단");
            outString = outString.replace("%ip%", "알수없음");
        } else {
            outString = outString.replace("%target%", ip);
            outString = outString.replace("%type%", "IP 채팅차단");
            outString = outString.replace("%ip%", ip);
        }

        // Replace %targetUUID% placeholder
        try {
            UUID tmpUUID = UUID.fromString(targetUUID);
            outString = outString.replace("%targetUUID%", targetUUID);
        } catch (IllegalArgumentException e) { // If this uuid is not valid, set it unknown.
            outString = outString.replace("%targetUUID%", "알수없음");
        }

        // Replace ban specific placeholders.
        if (duration == -1) { // If duration was -1, this is permanent ban.
            outString = outString.replace("%duration%", "영구 채팅차단");
            outString = outString.replace("%muteExpires%", "해당 없음");
            outString = outString.replace("%muteStarts%", Settings.df.format(new Date()));
        } else { // If this was normal ban, calculate duration using Duration.
            long days = Duration.ofSeconds(duration / 1000).toDays(); // Get days of duration.
            long minutes = Duration.ofSeconds((duration / 1000) - days * 86400).toMinutes(); // Get remaining minutes of duration.
            String durationString = String.format("%d일 %d분", days, minutes);

            // Replace placeholders.
            outString = outString.replace("%reason%", reason);
            outString = outString.replace("%duration%", durationString);
            outString = outString.replace("%executorName%", executorName);
            outString = outString.replace("%executorUUID%", executorUUID);
            outString = outString.replace("%muteExpires%", Settings.df.format(muteExpires));
            outString = outString.replace("%muteStarts%", Settings.df.format(muteStarts));
        }
        return outString;
    }
}
