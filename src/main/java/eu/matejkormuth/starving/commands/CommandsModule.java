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

import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.main.NMSHooks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

/**
 * This module provides nice way to dynamically create and register commands in Bukkit. It also provides some useful
 * things as command filters.
 *
 * @see CommandFilters
 */
public class CommandsModule extends Module {

    private CommandMap commandMap;
    private String fallbackPrefix = "starving";

    @Override
    public void onEnable() {
        // Initialize this module.
        commandMap = acquireCommandMap();
    }

    @Override
    public void onDisable() {

    }

    @NMSHooks(version = "v1_8_R3")
    private CommandMap acquireCommandMap() {
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        return craftServer.getCommandMap();
    }

    /**
     * Registers specified name as new command with specified command executor.
     *
     * @param name     name of the new command
     * @param executor executor of the new command
     */
    public void register(String name, CommandExecutor executor) {
        commandMap.register(fallbackPrefix, new DynamicCommand(name, executor));
    }

    /**
     * Registers specified name as new command with specified command executor.
     *
     * @param name     name of the new command
     * @param executor executor of the new command
     */
    public void command(String name, CommandExecutor executor) {
        register(name, executor);
    }

    /**
     * Class that represents dynamically registered command.
     */
    private static class DynamicCommand extends Command {

        private final CommandExecutor executor;

        public DynamicCommand(String name, CommandExecutor executor) {
            super(name);
            this.executor = executor;
        }

        @Override
        public boolean execute(CommandSender commandSender, String label, String[] args) {
            return this.executor.onCommand(commandSender, this, label, args);
        }
    }
}
