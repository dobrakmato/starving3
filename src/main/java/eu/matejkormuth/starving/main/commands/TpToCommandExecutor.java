package eu.matejkormuth.starving.main.commands;

import eu.matejkormuth.starving.main.Colors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpToCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 2) {
            World w = ((Player) commandSender).getWorld();
            ((Player) commandSender).teleport(new Location(w,
                    Float.valueOf(args[0]),
                    w.getHighestBlockYAt(Float.valueOf(args[0]).intValue(), Float.valueOf(args[1]).intValue()),
                    Float.valueOf(args[1])));
            commandSender.sendMessage(Colors.green("Success!"));
        } else if (args.length == 3) {
            World w = ((Player) commandSender).getWorld();
            ((Player) commandSender).teleport(new Location(w,
                    Float.valueOf(args[0]),
                    Float.valueOf(args[1]),
                    Float.valueOf(args[2])));
            commandSender.sendMessage(Colors.green("Success!"));
        } else {
            commandSender.sendMessage(Colors.red("/tpto <x> [y] <z>"));
        }
        return true;
    }
}
