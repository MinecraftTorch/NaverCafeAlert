package net.gooday2die.navercafealert.BanListeners;

import litebans.api.Events;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.event.Listener;

public abstract class AbstractBanListener {
    protected String name;
    protected CafeAPI cafeAPI;

    public AbstractBanListener(CafeAPI cafeAPI) {
        this.name = "Abstract";
        this.cafeAPI = cafeAPI;
    }

    public abstract void mainEventHandler();
}
