package net.gooday2die.navercafealert.Common;

import java.util.Date;
import java.util.UUID;

/**
 * A class that stores warn information.
 */
public class WarnInfo extends AbstractInfo {
    public Date issuedDate;
    public int warnCount;

    /**
     * A constructor method for class WarnInfo
     * @param targetName The target's name.
     * @param targetUUID The target's UUID.
     * @param executorName The executor's name.
     * @param executorUUID The executor's UUID.
     * @param reason The reason for unban.
     * @param ip The IP of target.
     * @param issuedDate The date when this warning was issued.
     * @param warnCount The total warning count of this user. If this was set -99, it means unknown.
     * @param isIpWarn If this was ip warn or not.
     */
    public WarnInfo(String targetName, String targetUUID, String executorName, String executorUUID, String reason,
                    String ip, Date issuedDate, int warnCount, boolean isIpWarn) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.executorName = executorName;
        this.executorUUID = executorUUID;
        this.reason = reason;
        this.ip = ip;
        this.issuedDate = issuedDate;
        this.warnCount = warnCount;
        this.isIpPunishment = isIpWarn;

        this.translateValues();
    }

    /**
     * A protected method that translates title and content automatically.
     */
    @Override
    protected void translateValues() {
        translatedContent = this.translateString(Settings.forms.get("warningReport"));
        translatedTitle = this.translateString(Settings.formTitles.get("warningReport"));
    }

    /**
     * A protected method that translates originalString into String using placeholders.
     * @param originalString The original String to replace placeholders.
     * @return The translated String.
     */
    @Override
    protected String translateString(String originalString) {
        String outString = originalString;

        // Warn might be by IP or username.
        if (!isIpPunishment) {
            outString = outString.replace("%target%", targetName);
            outString = outString.replace("%type%", "?????? ??????");
            outString = outString.replace("%ip%", "????????????");
        } else {
            outString = outString.replace("%target%", ip);
            outString = outString.replace("%type%", "IP ??????");
            outString = outString.replace("%ip%", ip);
        }

        // Replace %targetUUID% placeholder
        try {
            UUID tmpUUID = UUID.fromString(targetUUID);
            outString = outString.replace("%targetUUID%", targetUUID);
        } catch (IllegalArgumentException e) { // If this uuid is not valid, set it unknown.
            outString = outString.replace("%targetUUID%", "????????????");
        }

        // Replace other placeholders.
        outString = outString.replace("%reason%", reason);
        outString = outString.replace("%executorName%", executorName);
        outString = outString.replace("%executorUUID%", executorUUID);
        outString = outString.replace("%issuedDate%", Settings.df.format(issuedDate));

        // When warnCount could not be found.
        if (warnCount == -99)
            outString = outString.replace("%warnCount%", "????????????");
        else
            outString = outString.replace("%warnCount%", Integer.toString(warnCount));

        return outString;
    }
}
