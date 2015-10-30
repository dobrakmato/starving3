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
package eu.matejkormuth.starving.zombies;

import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.zombies.behavior.goals.PathfinderGoalNearestFOVVisibleAttackableTarget;
import eu.matejkormuth.starving.zombies.groups.ZombieGroup;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import java.lang.reflect.Field;
import java.util.Random;

@NMSHooks(version = "v1_8_R3")
public class Zombie extends EntityZombie {

    // Random instance.
    private static final Random random = new Random();

    // Attributes.
    private static final IAttribute maxHealth = GenericAttributes.maxHealth;
    private static final IAttribute followRange = GenericAttributes.FOLLOW_RANGE;
    private static final IAttribute knockbackResitence = GenericAttributes.c;
    private static final IAttribute movementSpeed = GenericAttributes.MOVEMENT_SPEED;
    private static final IAttribute attackDamage = GenericAttributes.ATTACK_DAMAGE;

    // Current group.
    private ZombieGroup group;

    /**
     * Calling this constructor spawns custom zombie. No other calls are needed.
     *
     * @param spawnLocation spawn location of this zombie
     */
    public Zombie(Location spawnLocation) {
        super(((CraftWorld) spawnLocation.getWorld()).getHandle());

        // Make zombie fireproof.
        this.fireProof = true;

        // Disable despawning of this zombie.
        this.persistent = true;

        // Set attribute values.
        setAttributes();
        try {
            // Clear old pathfinding configuration.
            clearPathfindingGoals();
        } catch (NoSuchFieldException | SecurityException |
                IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Set new pathfinding configuration.
        setPathfindingGoals();

        // This is needed to make it work.
        this.setLocation(spawnLocation.getX(), spawnLocation.getY(),
                spawnLocation.getZ(), spawnLocation.getYaw(),
                spawnLocation.getPitch());
        // We also need to add this entity to world.
        ((CraftWorld) spawnLocation.getWorld()).getHandle().addEntity(this);
    }

    private void clearPathfindingGoals() throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = PathfinderGoalSelector.class;
        Field list_b = clazz.getDeclaredField("b"); // b
        Field list_c = clazz.getDeclaredField("c"); // c 

        if (!list_b.isAccessible())
            list_b.setAccessible(true);

        if (!list_c.isAccessible())
            list_c.setAccessible(true);

        list_b.set(this.goalSelector, new UnsafeList<>());
        list_c.set(this.goalSelector, new UnsafeList<>());

        list_b.set(this.targetSelector, new UnsafeList<>());
        list_c.set(this.targetSelector, new UnsafeList<>());

        Bukkit.broadcastMessage(" Pathfinding goals cleared.");
    }

    private void setPathfindingGoals() {
        // Follow and Attack EntityHuman
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1, false));
        // Look at player.
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));

        // Random movement
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1));
        // Random look around
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

        // Choose EntityHuman by it's FOV.
        this.targetSelector.a(2, new PathfinderGoalNearestFOVVisibleAttackableTarget<>(this, EntityHuman.class, true, 90, 45));

        Bukkit.broadcastMessage(" Modified pathfinding goals added.");
        // this.targetSelector.a(2, new
        // PathfinderGoalNearestAttackableTarget<EntityHuman>(this,
        // EntityHuman.class, true));
    }

    private void setAttributes() {
        this.getAttributeInstance(maxHealth).setValue(18D + random.nextInt(5));
        this.getAttributeInstance(followRange).setValue(32D + random.nextInt(5));
        this.getAttributeInstance(movementSpeed).setValue(0.2899999988f + random.nextFloat() * 0.0499999892f);
        this.getAttributeInstance(knockbackResitence).setValue(random.nextFloat());
        this.getAttributeInstance(attackDamage).setValue(1D + random.nextInt(3));

        Bukkit.broadcastMessage(" Zombie attributes set.");
    }


    // These method provides non-NMS way to easily access position of Zombie.

    public double getX() {
        return this.locX;
    }

    public double getY() {
        return this.locY;
    }

    public double getZ() {
        return this.locZ;
    }
}
