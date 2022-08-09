package net.gooday2die.navercafealert.Common;


/**
 * An abstract class that is for storing information and generating HTML text.
 */
public abstract class AbstractInfo {
    public String translatedTitle;
    public String translatedContent;
    public String targetName;
    public String targetUUID;
    public String executorName;
    public String executorUUID;
    public String reason;

    /**
     * A public method that overrides toString.
     * @return Title : Content format String of translated values.
     */
    @Override
    public String toString() {
        return translatedTitle + " : " + translatedContent;
    }

    /**
     * A protected method that translates title and content automatically.
     */
    protected abstract void translateValues();

    /**
     * A protected method that translates originalString into String using placeholders.
     * @param originalString The original String to replace placeholders.
     * @return The translated String.
     */
    protected abstract String translateString(String originalString);
}
