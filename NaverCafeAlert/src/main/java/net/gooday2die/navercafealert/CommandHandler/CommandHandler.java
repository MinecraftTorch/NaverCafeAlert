package net.gooday2die.navercafealert.CommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * A class that takes care of commands.
 */
public class CommandHandler implements CommandExecutor {
    private final CommandHelper commandHelper = new CommandHelper();
    /**
     * A method that overrides onCommand from CommandExecutor.
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return Always true.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            commandHelper.showHelp(sender);
        } else {
            switch (args[0]) {
                case "reload": // For /nca reload
                    commandHelper.reload(sender);
                    break;
                case "reset": // For /nca reset
                    commandHelper.reset(sender);
                    break;
                case "help": // For /nca help and bare /nca
                default:
                    commandHelper.showHelp(sender);
                    break;
            }
        }
        return true;
    }
}
