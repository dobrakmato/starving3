package eu.matejkormuth.starving.main.commands;

import eu.matejkormuth.starving.main.Colors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            Float speed = Float.valueOf(args[0]);
            if (Math.abs(speed) > 1.0) {
                if (Math.abs(speed) > 10) {
                    sender.sendMessage(Colors.red("Too big!"));
                    return true;
                }

                ((Player) sender).setFlySpeed(speed / 10);
                sender.sendMessage(Colors.green("Speed set to " + speed));
            } else {
                ((Player) sender).setFlySpeed(speed);
                sender.sendMessage(Colors.green("Speed set to " + speed));
            }
        } else {
            sender.sendMessage(Colors.red("Please type in a speed!"));
        }
        return false;
    }
}
