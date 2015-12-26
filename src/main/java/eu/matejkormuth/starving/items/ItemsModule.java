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
package eu.matejkormuth.starving.items;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.commands.CommandsModule;
import eu.matejkormuth.starving.items.commands.ItemCommandExecutor;
import eu.matejkormuth.starving.items.commands.ItemInvCommandExecutor;
import eu.matejkormuth.starving.items.listeners.PlayerDropRewriteListener;
import eu.matejkormuth.starving.items.listeners.ProjectileHitListener;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.rockets.RocketsModule;
import eu.matejkormuth.starving.sounds.SoundsModule;

public class ItemsModule extends Module {

    @Dependency
    private NMSModule nmsModule; // Needed in firearms
    @Dependency
    private SoundsModule soundsModule;
    @Dependency
    private RocketsModule rocketsModule; // Needed in rocket launcher.
    @Dependency
    private CommandsModule commandsModule;

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        // Initialize child item manager.
        itemManager = new ItemManager(soundsModule, nmsModule, rocketsModule);

        // Initialize listeners.
        listener(itemManager); // ItemManager is also a listener.
        listener(new PlayerDropRewriteListener(itemManager));
        listener(new ProjectileHitListener(soundsModule, nmsModule));

        // Initialize commands.
        commandsModule.command("itemsinv", new ItemInvCommandExecutor(itemManager));
        commandsModule.command("item", new ItemCommandExecutor(itemManager));
    }

    @Override
    public void onDisable() {

    }

    public ItemManager getItemManager() {
        return itemManager;
    }
}
