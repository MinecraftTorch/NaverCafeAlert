package net.gooday2die.navercafealert.BanListeners;


/**
 * A abstract class for ban listener.
 */
public abstract class AbstractBanListener {
    protected String name; // The name of current listener.

    /**
     * A constructor method for class AbstractBanListener.
     */
    public AbstractBanListener() {
        this.name = "Abstract";
    }

    /**
     * A protected method for registering events to each plugin APIs.
     * Since all plugins support different APIs, this method should do following things.
     * 1. Let ban plugin's API know that there is a plugin trying to use their API.
     * 2. Hook up the ban plugin's API so that this plugin is able to use their API.
     */
    protected abstract void mainEventHandler();
}
