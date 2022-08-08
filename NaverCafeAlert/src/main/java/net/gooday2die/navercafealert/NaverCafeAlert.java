package net.gooday2die.navercafealert;

import litebans.api.Events;
import net.gooday2die.navercafealert.BanListeners.AbstractBanListener;
import net.gooday2die.navercafealert.BanListeners.LiteBans;
import net.gooday2die.navercafealert.NaverAPI.CafeAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class NaverCafeAlert extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CafeAPI cafeAPI = null;
        AbstractBanListener abstractBanListener = new LiteBans(cafeAPI);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
