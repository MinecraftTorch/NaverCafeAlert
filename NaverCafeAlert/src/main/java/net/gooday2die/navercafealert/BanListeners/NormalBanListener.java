package net.gooday2die.navercafealert.BanListeners;

import net.gooday2die.navercafealert.Common.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A class that takes care of plugins without support of API.
 * This will monitor each command using EventHandlers and check if they are punishment commands.
 */
public class NormalBanListener extends AbstractBanListener implements Listener {
    private final List<String> banKeywords = new ArrayList<>();
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
        this.banKeywords.add("ban-ip");
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
        if (this.banKeywords.contains(command)) return CommandType.ban;
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
        this.processCommon(items);
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
        this.processCommon(items);
    }

    /**
     * A private method that processes common parts from player's command and console's command.
     * This will check if this command was a punishment command or not and decides what to do with it.
     * @param items List of String that represents commands.
     */
    private void processCommon(List<String> items) {
        CommandType type = this.getCommandType(items.get(0));

        switch (type) {
            case ban:
                this.normalBanHandler.processBan(items);
                break;
            case mute:
                this.normalBanHandler.processMute(items);
                break;
            case warn:
                this.normalBanHandler.processWarn(items);
                break;
            case irrelevant:
                break;
        }
    }

    /**
     * A class that handles normal ban that was issued.
     */
    private static class NormalBanHandler implements IBanHandler {

        /**
         * A method that processes ban by Object provided.
         * This method will to generate BanInfo instance and call PostArticle using the BanInfo created.
         * @param object The Object to process ban information.
         */
        @Override
        public void processBan(@Nullable Object object) {
            System.out.println("BAN");
            System.out.println(object);
        }

        /**
         * A method that processes warn by Object provided.
         * This method will generate WarnInfo instance and call PostArticle using the WarnInfo created.
         * @param object The Object to process warn information.
         */
        @Override
        public void processWarn(@Nullable Object object) {
            System.out.println("WARN");
            System.out.println(object);
        }

        /**
         * A method that processes mute by Object provided.
         * This method will generate MuteInfo instance and call PostArticle using the MuteInfo created.
         *
         * @param object The Object to process mute information.
         */
        @Override
        public void processMute(@Nullable Object object) {
            System.out.println("MUTE");
            System.out.println(object);
        }
    }
}
