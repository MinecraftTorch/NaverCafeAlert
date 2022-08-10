package net.gooday2die.navercafealert.BanListeners;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import org.bukkit.event.EventHandler;

public class AdvancedBan extends AbstractBanListener {
    /**
     * A protected method for registering events to each plugin APIs.
     * Since all plugins support different APIs, this method should do following things.
     * 1. Let ban plugin's API know that there is a plugin trying to use their API.
     * 2. Hook up the ban plugin's API so that this plugin is able to use their API.
     */
    @Override
    protected void mainEventHandler() {

    }

    @EventHandler
    public void onPunishmentEvent(PunishmentEvent event) {
        Punishment punishment = event.getPunishment();
        PunishmentType type = punishment.getType();

        System.out.println(type);
    }

}
