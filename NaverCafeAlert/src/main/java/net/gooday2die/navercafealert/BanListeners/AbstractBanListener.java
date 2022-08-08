package net.gooday2die.navercafealert.BanListeners;

import litebans.api.Events;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.event.Listener;

public abstract class AbstractBanListener {
    protected String name;

    public AbstractBanListener() {
        this.name = "Abstract";
    }

    public abstract void mainEventHandler();
}
