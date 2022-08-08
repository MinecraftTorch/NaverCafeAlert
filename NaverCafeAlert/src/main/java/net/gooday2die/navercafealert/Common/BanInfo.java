package net.gooday2die.navercafealert.Common;

import java.util.Date;

public class BanInfo {
    public String targetName;
    public String targetUUID;
    public String executorName;
    public String executorUUID;
    public String reason;
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
    }

    /**
     * A method that overrides toString()
     * @return Returns fields in order as String.
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s", targetName, targetUUID,
                executorName, executorUUID, reason, ip, banExpires, banStarts, duration, isIpBan);
    }
}
