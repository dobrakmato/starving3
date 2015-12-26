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
package eu.matejkormuth.starving.items.base;

import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.starving.items.Category;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mapping;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.sounds.Sounds;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public abstract class MeleeWeapon extends Item {

    private double baseDmg;
    private int uses;
    private int useDurabilityDecrement;

    public MeleeWeapon(Mapping mapping, String name, double baseDmg, int uses) {
        super(mapping, name);
        // increment.
        this.useDurabilityDecrement = this.itemStack.getType().getMaxDurability() / uses;
        this.baseDmg = baseDmg;
        this.uses = uses;
        this.setCategory(Category.MELEE);
        this.setMaxStackAmount(1);
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isLeftClick(action)) {
            Sounds.HAND_SWING.play(player.getEyeLocation());
        }

        return super.onInteract(player, action, clickedBlock, clickedFace);
    }

    public void onAttack(Player damager, LivingEntity entity, double damage) {
        entity.damage(baseDmg);

        // Next part of code seems to make no sense. But for some reason it
        // works.

        // Apply uses.
        short itemDurability = damager.getItemInHand().getDurability();

        if (itemDurability > this.itemStack.getType().getMaxDurability()) {
            // Trigger possible events.
            this.onItemBreak(damager);
            // Remove item.
            DelayedTask.of(() -> damager.setItemInHand(null)).schedule(Time.ofTicks(5));

        } else {
            damager.getItemInHand().setDurability((short) (itemDurability + this.useDurabilityDecrement));
        }
    }

    public void onItemBreak(Player damager) {

    }

    public int getUses() {
        return uses;
    }

    public double getBaseDmg() {
        return baseDmg;
    }
}
