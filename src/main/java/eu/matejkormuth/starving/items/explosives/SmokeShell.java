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
package eu.matejkormuth.starving.items.explosives;

import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.Rarity;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.main.bukkitfixes.FlagMetadataValue;
import eu.matejkormuth.starving.sounds.Sound;
import eu.matejkormuth.starving.sounds.SoundsModule;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.block.Action;

import eu.matejkormuth.bukkit.Actions;

public class SmokeShell extends Item {

    /**
     * Metadata key.
     */
    public static final String SMOKE_SHELL_FLAG = "isSmokeShell";

    private Sound soundThrow;
    private Sound soundBreak;
    
    public SmokeShell(SoundsModule soundsModule) {
        super(Mappings.SMOKESHELL, "Smoke Shell");
        this.setMaxStackAmount(8);
        this.setRarity(Rarity.UNCOMMON);

        soundThrow = soundsModule.createSound("explosives.smokeshell.throw");
        soundBreak = soundsModule.createSound("explosives.smokeshell.break");
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            ThrownPotion potion = (ThrownPotion) player.getWorld().spawnEntity(
                    player.getEyeLocation().add(player.getEyeLocation().getDirection()),
                    EntityType.SPLASH_POTION);
            potion.setVelocity(player.getEyeLocation().getDirection().multiply(1.3f));
            potion.setShooter(player);
            potion.setMetadata(SMOKE_SHELL_FLAG, new FlagMetadataValue());
            
           soundThrow.play(player, player.getLocation(), 1f, 1f);
        }
        return InteractResult.useOne();
    }
}
