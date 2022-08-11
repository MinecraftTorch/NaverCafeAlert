package net.gooday2die.navercafealert.Common;

import java.util.Date;
import java.util.UUID;

public class WarnInfo extends AbstractInfo {
    public Date issuedDate;
    public int warnCount;

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
            outString = outString.replace("%type%", "유저 경고");
            outString = outString.replace("%ip%", "알수없음");
        } else {
            outString = outString.replace("%target%", ip);
            outString = outString.replace("%type%", "IP 경고");
            outString = outString.replace("%ip%", ip);
        }

        // Replace %targetUUID% placeholder
        try {
            UUID tmpUUID = UUID.fromString(targetUUID);
            outString = outString.replace("%targetUUID%", targetUUID);
        } catch (IllegalArgumentException e) { // If this uuid is not valid, set it unknown.
            outString = outString.replace("%targetUUID%", "알수없음");
        }

        // Replace other placeholders.
        outString = outString.replace("%reason%", reason);
        outString = outString.replace("%executorName%", executorName);
        outString = outString.replace("%executorUUID%", executorUUID);
        outString = outString.replace("%issuedDate%", Settings.df.format(issuedDate));

        // When warnCount could not be found.
        if (warnCount == -99)
            outString = outString.replace("%warnCount%", "알수없음");
        else
            outString = outString.replace("%warnCount%", Integer.toString(warnCount));

        return outString;
    }
}
