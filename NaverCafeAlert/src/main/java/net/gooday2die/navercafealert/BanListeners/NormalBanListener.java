package net.gooday2die.navercafealert.BanListeners;

import net.gooday2die.navercafealert.Common.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;


/**
 * A class that takes care of plugins without support of API.
 * This will monitor each command using EventHandlers and check if they are punishment commands.
 * This class will take care of normal bans without any plugin related.
 */
public class NormalBanListener extends AbstractBanListener implements Listener {
    private final List<String> banKeywords = new ArrayList<>();
    private final List<String> ipBanKeywords = new ArrayList<>();
    private final List<String> warnKeywords = new ArrayList<>();
    private final List<String> muteKeywords = new ArrayList<>();
    private final NormalBanHandler normalBanHandler = new NormalBanHandler();

    /**
     * An enum that stores command types.
     */
    private enum CommandType {
        ban,
        mute,
        warn,
        irrelevant
    }

    /**
     * A constructor method for class NormalBanListener.
     */
    public NormalBanListener() {
        this.mainEventHandler();
        this.generatePunishmentKeywords();
        this.name = "Normal Ban";
    }

    /**
     * A private method that generates punishment keywords.
     * Since this class is intended to be listening to all commands, following list of commands will let this class
     * know that this is a punishment command or not.
     */
    private void generatePunishmentKeywords() {
        this.banKeywords.add("ban");
        this.ipBanKeywords.add("ban-ip");
        this.banKeywords.add("밴");

        this.warnKeywords.add("warn");
        this.warnKeywords.add("경고");

        this.muteKeywords.add("mute");
        this.muteKeywords.add("채팅차단");
    }

    /**
     * A private method that checks which CommandType this command falls into.
     * @param command The command to check.
     * @return A CommandType that matches the provided command.
     */
    private CommandType getCommandType(String command) {
        if (this.banKeywords.contains(command) || this.ipBanKeywords.contains(command)) return CommandType.ban;
        else if (this.muteKeywords.contains(command)) return CommandType.mute;
        else if (this.warnKeywords.contains(command)) return CommandType.warn;
        else return CommandType.irrelevant;
    }

    /**
     * A protected method for registering events to each plugin APIs.
     * This method will register events to Bukkit API that there is a Listener for Events.
     */
    @Override
    protected void mainEventHandler() {
        Bukkit.getPluginManager().registerEvents(this, Settings.thisPlugin);
    }

    /**
     * A public method for PlayerCommandProcessEvent.
     * This method is for user's command.
     * @param event PlayerCommandProcessEvent that was fired.
     */
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().replace("/", ""); // If this was user's command, remove /.
        List<String> items = new ArrayList<>(Arrays.asList(command.split(" "))); // Split whitespaces.
        String issuerName = event.getPlayer().getName();
        String issuerUUID = event.getPlayer().getUniqueId().toString();

        CommandInfo commandInfo = new CommandInfo(issuerName, issuerUUID, items);
        this.processCommon(commandInfo);
    }

    /**
     * A public method for ServerCommandEvent.
     * This method is for console's command input.
     * @param event ServerCommandEvent that was fired.
     */
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand();
        List<String> items = new ArrayList<>(Arrays.asList(command.split(" "))); // Split whitespaces.

        CommandInfo commandInfo = new CommandInfo("Console", "Console", items);
        this.processCommon(commandInfo);
    }

    /**
     * A private method that processes common parts from player's command and console's command.
     * This will check if this command was a punishment command or not and decides what to do with it.
     * @param commandInfo The CommandInfo of this current issued command.
     */
    private void processCommon(CommandInfo commandInfo) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (commandInfo.items.size() <= 1) return; // Meaning that this command was not valid
                CommandType type = getCommandType(commandInfo.items.get(0));

                switch (type) { // Check type of this command and process it.
                    case ban: // Ban has two types, thus find out if this is ip ban or not.
                        if (ipBanKeywords.contains(commandInfo.items.get(0))) commandInfo.isIPPunishment = true;
                        normalBanHandler.processBan(commandInfo);
                        break;
                    case mute:
                        normalBanHandler.processMute(commandInfo);
                        break;
                    case warn:
                        normalBanHandler.processWarn(commandInfo);
                        break;
                    case irrelevant:
                        break;
                }
            }
        }.runTaskAsynchronously(Settings.thisPlugin);
    }

    /**
     * A private class that stores command information.
     * This will store issuer's information as well as command.
     */
    private static class CommandInfo {
        public String issuerName;
        public String issuerUUID;
        public List<String> items;
        public boolean isIPPunishment;

        /**
         * A constructor method for class CommandInfo
         * @param issuerName The issuer's name.
         * @param issuerUUID The issuer's uuid.
         * @param items The List of String that represents the command.
         */
        public CommandInfo(String issuerName, String issuerUUID, List<String> items) {
            this.issuerName = issuerName;
            this.issuerUUID = issuerUUID;
            this.items = items;
        }
    }

    /**
     * A class that handles normal ban that was issued.
     */
    private static class NormalBanHandler implements IBanHandler {

        /**
         * A method that processes ban by Object provided.
         * This method will to generate BanInfo instance and call PostArticle using the BanInfo created.
         * @param object The Object (CommandInfo) to process ban information.
         */
        @Override
        public void processBan(@Nullable Object object){
            CommandInfo commandInfo = (CommandInfo) object;
            BanList banList;

            // For storing all information.
            assert commandInfo != null;
            String target = commandInfo.items.get(1);
            String ip = "알수없음";
            String targetUUID;
            String targetName;
            Date banStarts;
            Date banExpires;
            boolean isIPBan = commandInfo.isIPPunishment;
            String reason;
            long duration;

            // Check if this is an IP punishment.
            // Then place values accordingly.
            if (commandInfo.isIPPunishment) { // If this is IP ban.
                banList = Bukkit.getBanList(BanList.Type.IP);
                ip = target;
                targetName = "알수없음";
                targetUUID = "알수없음";
            } else { // If this is normal user ban.
                banList = Bukkit.getBanList(BanList.Type.NAME);
                targetName = target;
                try { // Try retrieving UUID from username
                    targetUUID = Utils.translateUsernameToUUID(targetName);
                } catch (Exception e) { // If exception found, just set it unknown.
                    targetUUID = "알수없음";
                }
            }

            // Get BanEntry by the target
            BanEntry banEntry = banList.getBanEntry(target);
            assert banEntry != null;

            // Get information on ban.
            banStarts = banEntry.getCreated();
            banExpires = banEntry.getExpiration();
            reason = banEntry.getReason();

            if (banExpires == null) { // If this is permanent ban, banExpires will be null.
                duration = -1;
            } else // If this was normal ban.
                duration = banExpires.getTime() - banStarts.getTime();

            // Generate BanInfo using the information.
            BanInfo banInfo = new BanInfo(targetName, targetUUID, commandInfo.issuerName, commandInfo.issuerUUID,
                    reason, ip, banExpires, banStarts, duration, isIPBan);

            if (Settings.cafeBanReportEnabled) this.postArticle(banInfo);
        }

        /**
         * A method that processes warn by Object provided.
         * This method will generate WarnInfo instance and call PostArticle using the WarnInfo created.
         * @param object The Object (CommandInfo) to process warn information.
         */
        @Override
        public void processWarn(@Nullable Object object) {
            CommandInfo commandInfo = (CommandInfo) object;

            // For storing all information.
            assert commandInfo != null;
            String targetName = commandInfo.items.get(1);
            String targetUUID;
            String reason;

            try { // Try retrieving UUID from username
                targetUUID = Utils.translateUsernameToUUID(targetName);
            } catch (Exception e) { // If exception found, just set it unknown.
                targetUUID = "알수없음";
            }

            // Generate reason since it is /warn is not a regular command
            try { // Try getting sublist of reasonList from index 2. Since index 1 is the user's name.
                List<String> reasonList = commandInfo.items.subList(2, commandInfo.items.size());
                reason = StringUtils.join(reasonList, " ");
            } catch (IndexOutOfBoundsException e) { // If index 2 was not found or something went wrong, set unknown.
                reason = "알수없음";
            }

            // Generate WarnInfo using the information.
            // Since when we are using just command parsing, it is not possible to get information on
            // IP and if this was ip warn or not including total warn count.
            WarnInfo warnInfo = new WarnInfo(targetName, targetUUID, commandInfo.issuerName, commandInfo.issuerUUID,
                    reason, "알수없음", new Date(), -99, false);

            if (Settings.cafeWarningReportEnabled) this.postArticle(warnInfo);
        }

        /**
         * A method that processes mute by Object provided.
         * This method will generate MuteInfo instance and call PostArticle using the MuteInfo created.
         * @param object The Object (CommandInfo) to process mute information.
         */
        @Override
        public void processMute(@Nullable Object object) {
            CommandInfo commandInfo = (CommandInfo) object;

            // For storing all information.
            assert commandInfo != null;
            String targetName = commandInfo.items.get(1);
            String targetUUID;
            String reason;

            try { // Try retrieving UUID from username
                targetUUID = Utils.translateUsernameToUUID(targetName);
            } catch (Exception e) { // If exception found, just set it unknown.
                targetUUID = "알수없음";
            }

            // Generate reason since it is /warn is not a regular command
            try { // Try getting sublist of reasonList from index 2. Since index 1 is the user's name.
                List<String> reasonList = commandInfo.items.subList(2, commandInfo.items.size());
                reason = StringUtils.join(reasonList, " ");
            } catch (IndexOutOfBoundsException e) { // If index 2 was not found or something went wrong, set unknown.
                reason = "알수없음";
            }

            // Generate mute information using command that was parsed.
            // Since we are just looking at command, we are not able to know following values
            // 1. If this is ip mute or not, 2. Mute expiration date, 3. Mute duration, 4. IP
            // Those 4 information will be set unknown and the mute report will always be set permanent mute.
            MuteInfo muteInfo = new MuteInfo(targetName, targetUUID, commandInfo.issuerName, commandInfo.issuerUUID,
                    reason, "알수없음", null, new Date(), -1, false);
            if (Settings.cafeMuteReportEnabled) this.postArticle(muteInfo);
        }
    }
}
