package net.gooday2die.navercafealert.BanListeners;

import net.gooday2die.navercafealert.Common.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

import javax.annotation.Nullable;


/**
 * A class that takes care of plugins without support of API.
 * This will monitor each command using EventHandlers and check if they are punishment commands.
 */
public class NormalBanListener extends AbstractBanListener implements Listener {
    /**
     * A constructor method for class NormalBanListener.
     */
    public NormalBanListener() {
        this.mainEventHandler();
        this.name = "Normal Ban";
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
     * @param event PlayerCommandProcessEvent that was fired.
     */
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        System.out.println("EventFired1;");
        System.out.println(event.getPlayer());
    }

    /**
     * A public method for ServerCommandEvent.
     * @param event ServerCommandEvent that was fired.
     */
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        System.out.println("EventFired2;");
        System.out.println(event.getCommand());
    }

    /**
     * A class that handles normal ban that was issued.
     */
    private class NormalBanHandler implements IBanHandler {

        /**
         * A method that processes ban by Object provided.
         * This method will to generate BanInfo instance and call PostArticle using the BanInfo created.
         * @param object The Object to process ban information.
         */
        @Override
        public void processBan(@Nullable Object object) {

        }

        /**
         * A method that processes warn by Object provided.
         * This method will generate WarnInfo instance and call PostArticle using the WarnInfo created.
         * @param object The Object to process warn information.
         */
        @Override
        public void processWarn(@Nullable Object object) {

        }

        /**
         * A method that processes mute by Object provided.
         * This method will generate MuteInfo instance and call PostArticle using the MuteInfo created.
         *
         * @param object The Object to process mute information.
         */
        @Override
        public void processMute(@Nullable Object object) {

        }
    }
}
