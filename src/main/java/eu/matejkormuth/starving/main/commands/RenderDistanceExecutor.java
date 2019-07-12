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
package eu.matejkormuth.starving.main.commands;

import eu.matejkormuth.bukkit.Worlds;
import eu.matejkormuth.starving.main.Colors;
import eu.matejkormuth.starving.main.NMSHooks;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

@NMSHooks(version = "v1_8_R3")
@Slf4j
public class RenderDistanceExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            World world = ((Player) sender).getWorld();
            int range = Integer.valueOf(args[0]);
            extend(world, range);
            sender.sendMessage(Colors.green("Render distance in world " + world.getName() + " extended to " + range + " chunks!"));
        } else if (args.length == 2) {
            World world = Worlds.by(args[0]);
            int range = Integer.valueOf(args[1]);
            extend(world, range);
            sender.sendMessage(Colors.green("Render distance in world " + world.getName() + " extended to " + range + " chunks!"));
        } else {
            sender.sendMessage(Colors.red("/renderdistance [worldname] <chunks>"));
        }

        return true;
    }

    private void extend(World world, int range) {
        log.info("Extending render distance in {} to {}...", world.getName(), range);
        WorldServer server = ((CraftWorld) world).getHandle();
        server.spigotConfig.viewDistance = range;
        try {
            Field field = WorldServer.class.getDeclaredField("manager");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            ((PlayerChunkMap) field.get(server)).a(range);
            log.info("View distance in world {} extended to {} chunks!", server.worldData.getName(), server.spigotConfig.viewDistance);
        } catch (Exception e) {
            log.error("Failed to extend view distance!", e);
        }
    }
}
