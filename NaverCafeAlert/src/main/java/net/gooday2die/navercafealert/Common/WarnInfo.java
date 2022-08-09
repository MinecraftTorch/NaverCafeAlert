package net.gooday2die.navercafealert.Common;

import java.util.Date;

public class WarnInfo extends AbstractInfo {
    public String ip;
    public Date issuedDate;

    public WarnInfo(String targetName, String targetUUID, String executorName, String executorUUID, String reason,
                    String ip, Date issuedDate) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.executorName = executorName;
        this.executorUUID = executorUUID;
        this.reason = reason;
        this.ip = ip;
        this.issuedDate = issuedDate;
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

        // Warn might be by IP or username.
        if (ip == null) {
            outString = outString.replace("%target%", targetName);
            outString = outString.replace("%type%", "유저 경고");
            outString = outString.replace("%ip%", "알수없음");
        } else {
            outString = outString.replace("%target%", ip);
            outString = outString.replace("%type%", "IP 경고");
            outString = outString.replace("%ip%", ip);
        }

        // Replace other placeholders.
        outString = outString.replace("%reason%", reason);
        outString = outString.replace("%targetUUID%", targetUUID);
        outString = outString.replace("%executorName%", executorName);
        outString = outString.replace("%executorUUID%", executorUUID);
        outString = outString.replace("%issuedDate%", Settings.df.format(issuedDate));

        return outString;
    }
}
