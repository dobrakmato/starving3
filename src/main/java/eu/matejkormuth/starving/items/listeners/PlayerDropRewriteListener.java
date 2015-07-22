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
package eu.matejkormuth.starving.items.listeners;

import eu.matejkormuth.starving.items.ItemManager;
import eu.matejkormuth.starving.items.base.Firearm;
import eu.matejkormuth.starving.loot.spawns.ArmorStandLootSpawn;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.bukkitfixes.FlagMetadataValue;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.EulerAngle;

public class PlayerDropRewriteListener implements Listener {

    private ItemManager itemManager;

    public PlayerDropRewriteListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    private void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);

            ArmorStand armorStand = (ArmorStand) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARMOR_STAND);
            armorStand.setVelocity(event.getPlayer().getEyeLocation().getDirection());
            armorStand.setItemInHand(event.getItemDrop().getItemStack());
            armorStand.setVisible(false);
            armorStand.setArms(true);
            armorStand.setBasePlate(false);
            float minusY = 0.8f;

            if (itemManager.findItem(event.getItemDrop().getItemStack()) instanceof Firearm) {
                armorStand.setRightArmPose(new EulerAngle(0, 0, Math.PI / 2));
                minusY = 1.27f;
            }

            // Mark ArmorStand as lootable.
            armorStand.setMetadata(ArmorStandLootSpawn.METADATA_KEY, new FlagMetadataValue());

            float finalMinusY = minusY;

            DelayedTask.of(() -> {
                armorStand.teleport(armorStand.getLocation().subtract(0, finalMinusY, 0));
                armorStand.setGravity(false);
            }).schedule(30L);
        }
    }
}
