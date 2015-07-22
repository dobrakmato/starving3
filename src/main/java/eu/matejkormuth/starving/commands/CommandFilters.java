/**
 * Starving - Bukkit API server mod with Zombies.
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.matejkormuth.starving.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;

/**
 * This class provides useful methods to filter access to commands.
 * <p>
 * Consider for example this line:
 * <pre>commandsModule.command("setwarp", CommandFilters.opOnly(this::setWarp));</pre>
 * This prevents non-ops from accessing command setwarp.
 */
public class CommandFilters {
    /**
     * Ensures that only OP(s) will be able to execute specified command (command executor). All non-OP senders will
     * be provided with nice "You don't have enough permissions to use this command!" message.
     *
     * @param executor command executor or method reference
     * @return command executor
     */
    public static CommandExecutor opOnly(CommandExecutor executor) {
        return (commandSender, command, label, args) -> {
            if (commandSender.isOp()) {
                return executor.onCommand(commandSender, command, label, args);
            } else {
                commandSender.sendMessage(ChatColor.RED + "You don't have enough permissions to use this command!");
                return true;
            }
        };
    }
}
