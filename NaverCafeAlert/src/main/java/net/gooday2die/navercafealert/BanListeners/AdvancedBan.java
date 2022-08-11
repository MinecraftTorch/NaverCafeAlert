package net.gooday2die.navercafealert.BanListeners;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.gooday2die.navercafealert.Common.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;


/**
 * A public class that takes care of AdvancedBan plugin.
 */
public class AdvancedBan extends AbstractBanListener implements Listener {
    /**
     * A protected method for registering events to each plugin APIs.
     * Since all plugins support different APIs, this method should do following things.
     * 1. Let ban plugin's API know that there is a plugin trying to use their API.
     * 2. Hook up the ban plugin's API so that this plugin is able to use their API.
     */

    private AdvancedBanHandler apiListener;

    /**
     * A constructor method for class AdvancedBan.
     */
    public AdvancedBan() {
        this.apiListener  = new AdvancedBanHandler();
        this.mainEventHandler();
    }

    /**
     * A method that sets registerEvent as this class for plugin.
     * This will let Bukkit know that there is a plugin that is listening to Events.
     */
    @Override
    protected void mainEventHandler() {
        Settings.thisPlugin.getServer().getPluginManager().registerEvents(this, Settings.thisPlugin);
    }

    /**
     * A method that is an EventHandler for PunishmentEvent.
     * @param event The PunishmentEvent that AdvancedBan fired.
     */
    @EventHandler
    public void onPunishmentEvent(PunishmentEvent event) {
        System.out.println("event fired");
        Punishment punishment = event.getPunishment();
        PunishmentType type = punishment.getType();
        System.out.println(type);
    }

    /**
     * A method that is an EventHandler for PunishmentEvent.
     * @param event The PunishmentEvent that AdvancedBan fired.
     */
    @EventHandler
    public void onRevokePunishmentEvent(RevokePunishmentEvent event) {
        System.out.println("event fired");
        Punishment punishment = event.getPunishment();
        PunishmentType type = punishment.getType();
        System.out.println(type);
    }

    /**
     * A class that processes everything related to AdvancedBan.
     */
    private static class AdvancedBanHandler implements IBanHandler {

        /**
         * A method that processes ban by Object provided.
         *
         * @param o The Object to process ban information.
         */
        @Override
        public void processBan(@Nullable Object o) {

        }

        /**
         * A method that processes warn by Object provided.
         *
         * @param o The Object to process warn information.
         */
        @Override
        public void processWarn(@Nullable Object o) {

        }

        /**
         * A method that processes mute by Object provided.
         *
         * @param o The Object to process mute information.
         */
        @Override
        public void processMute(@Nullable Object o) {

        }
    }
}
