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

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.starving.items.*;
import eu.matejkormuth.starving.items.firearms.Dragunov;
import eu.matejkormuth.starving.items.itemmeta.concrete.FirearmItemMetaWrapper;
import eu.matejkormuth.starving.items.transformers.FirearmTransformer;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.nms.NMS;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.sounds.Sound;
import eu.matejkormuth.starving.sounds.SoundsModule;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@NMSHooks(version = "v1_8_R3")
public abstract class Firearm extends Item {
    protected static final Vector HALF_VECTOR = new Vector(0.5, 0.5, 0.5);

    private int clipSize;
    // private int ammo;

    private AmmunitionType ammoType;

    private int fireRate = 1; // per second
    private float noiseLevel = 1; // impulse power
    private float projectileSpeed = 2; // multiplier
    private int reloadTime = 40; // ticks
    private float inaccurancy = 0.5f;
    private float scopedInaccurancy = 0.2f;
    private float recoil = 0.5f;

    protected Sound reloadSound;
    protected Sound fireSound;

    private final NMS nms;
    private final FirearmTransformer firearmTransformer;

    public Firearm(ItemManager itemManager, SoundsModule soundsModule, Mapping mapping,
                   String name, Class<?> soundSourceClass, NMSModule nms) {
        super(mapping, name);
        this.nms = nms.getNms();
        this.firearmTransformer = new FirearmTransformer(itemManager);
        this.setCategory(Category.FIREARMS);
        this.setRarity(Rarity.UNCOMMON);
        this.setMaxStackAmount(1);
    }

    protected void setAmmoType(AmmunitionType ammoType) {
        this.ammoType = ammoType;
    }

    protected void setClipSize(int clipSize) {
        this.clipSize = clipSize;
    }

    protected void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    protected void setNoiseLevel(float noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    protected void setProjectileSpeed(float projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    protected void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    protected void setInaccurancy(float inaccurancy) {
        this.inaccurancy = inaccurancy;
    }

    protected void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    protected void setScopedInaccurancy(float scopedInaccurancy) {
        this.scopedInaccurancy = scopedInaccurancy;
    }

    public AmmunitionType getAmmoType() {
        return this.ammoType;
    }

    public int getClipSize() {
        return this.clipSize;
    }

    public int getFireRate() {
        return this.fireRate;
    }

    public float getNoiseLevel() {
        return this.noiseLevel;
    }

    public float getInaccurancy() {
        return this.inaccurancy;
    }

    public float getScopedInaccurancy() {
        return this.scopedInaccurancy;
    }

    public float getProjectileSpeed() {
        return this.projectileSpeed;
    }

    public float getRecoil() {
        return this.recoil;
    }

    public int getReloadTime() {
        return this.reloadTime;
    }

    public Sound getFireSound() {
        return this.fireSound;
    }

    public Sound getReloadSound() {
        return this.reloadSound;
    }

    @Override
    public void onInteractWith(Player player, Entity entity) {
        FirearmItemMetaWrapper wrapper = new FirearmItemMetaWrapper(player.getItemInHand());
        if (wrapper.isBrust()) {
            // Brust mode.
            doFire(player, wrapper);
            DelayedTask.of(() -> doFire(player, wrapper)).schedule(Time.ofTicks(2));
            DelayedTask.of(() -> doFire(player, wrapper)).schedule(Time.ofTicks(4));
            //DelayedTask.of(() -> doFire(player, wrapper)).schedule(Time.ofTicks(6));
        } else {
            // Single shot.
            doFire(player, wrapper);
        }
    }

    @Override
    public InteractResult onInteract(Player player, Action action,
                                     Block clickedBlock, BlockFace clickedFace) {
        if (action == Action.RIGHT_CLICK_AIR) {
            FirearmItemMetaWrapper wrapper = new FirearmItemMetaWrapper(player.getItemInHand());
            // Check mode.
            if (wrapper.isBrust()) {
                // Brust mode.
                doFire(player, wrapper);
                DelayedTask.of(() -> doFire(player, wrapper)).schedule(Time.ofTicks(2));
                DelayedTask.of(() -> doFire(player, wrapper)).schedule(Time.ofTicks(4));
                //DelayedTask.of(() -> doFire(player, wrapper)).schedule(Time.ofTicks(6));
            } else {
                // Single shot.
                doFire(player, wrapper);
            }
        } else if (Actions.isLeftClick(action)) {
            FirearmItemMetaWrapper wrapper = new FirearmItemMetaWrapper(player.getItemInHand());

            // Mode switching.
            if (player.isSneaking()) {
                if (wrapper.isBrust()) {
                    wrapper.setFireMode(FirearmItemMetaWrapper.FIRE_MODE_SINGLE);
                } else {
                    wrapper.setFireMode(FirearmItemMetaWrapper.FIRE_MODE_BRUST);
                }
                ItemStack is = player.getItemInHand();
                wrapper.apply(is);
                nms.sendAboveActionBarMessage(player, ChatColor.YELLOW.toString() + "Fire mode: " + wrapper.getFireMode());
            } else {
                // Just scope the gun.
                ItemStack is = player.getItemInHand();
                toggleScope(player, is);
            }
        }
        return InteractResult.useNone();
    }

    private void doFire(Player player, FirearmItemMetaWrapper wrapper) {
        ItemStack is = player.getItemInHand();


        Vector projectileVelocity = computeAndFire(player);

        // Play fire sound.
        playFireSound(player);

        // Make recoil.
        makeRecoil(player, projectileVelocity);

        // Lower ammo count.
        int ammo = wrapper.getCurrentAmmo();
        if (ammo == 1) {
            // Reload
            this.playReloadSound(player);
            wrapper.setCurrentAmmo(this.getClipSize());
        } else {
            wrapper.setCurrentAmmo(ammo - 1);
        }
        wrapper.apply(is);

        nms.sendAboveActionBarMessage(player, ChatColor.YELLOW.toString() + ammo + "/" + this.clipSize);
    }

    @SuppressWarnings("deprecation")
    @NMSHooks(version = "1_8_R2")
    protected Vector computeAndFire(Player player) {

        Vector randomVec;
        if (this.isScoped()) {
            randomVec = Vector.getRandom().subtract(HALF_VECTOR).multiply(
                    this.scopedInaccurancy);
        } else {
            randomVec = Vector.getRandom().subtract(HALF_VECTOR).multiply(
                    this.inaccurancy);
        }

        // Entity tracing.
        int maxDistance = 64;
        Location loc = player.getEyeLocation();

        // Ray trace.
        Location playerLocation = player.getLocation();
        Vector projectileStart = playerLocation.toVector();
        Vector projectileDirection = player.getEyeLocation().getDirection().normalize();
        int entitiesHit = 0;
        for (LivingEntity e : player.getWorld().getLivingEntities()) {
            // Skip player.
            if (player == e) {
                continue;
            }

            if (e.getType() == EntityType.ARMOR_STAND) {
                continue;
            }

            // Limit entities hit.
            if (entitiesHit >= 2) {
                break;
            }

            double distance = playerLocation.distance(e.getLocation());
            if (distance < maxDistance) {
                Vector projectilePosition = projectileStart.clone().add(
                        projectileDirection.clone().multiply(distance));
                // Check for collision of entity's AABB and point.
                AxisAlignedBB bBox = ((CraftLivingEntity) e).getHandle().getBoundingBox();
                AxisAlignedBB rightBB = new AxisAlignedBB(bBox.a, bBox.b
                        - e.getEyeHeight(), bBox.c, bBox.d, bBox.e
                        - e.getEyeHeight(), bBox.f);
                if (rightBB.a(new Vec3D(projectilePosition.getX(),
                        projectilePosition.getY(),
                        projectilePosition.getZ()))) {
                    // We hit this entity.
                    entitiesHit++;

                    // Do not headshot dead entities.
                    if (e.isDead()) {
                        continue;
                    }

                    // Is this a headshot?
                    if (projectilePosition.getY() - 0.1f > e.getLocation().getY() - 0.3f) {
                        ParticleEffect.BLOCK_CRACK.display(
                                new ParticleEffect.BlockData(
                                        Material.REDSTONE_BLOCK, (byte) 0),
                                0.25f, 0.25f, 0.25f, 1, 80, e.getEyeLocation(),
                                256);
                        player.getWorld().playSound(e.getLocation(),
                                org.bukkit.Sound.HURT_FLESH, 1, 1);

                        for (int i = 0; i < 40; i++) {
                            ParticleEffect.BLOCK_CRACK.display(
                                    new ParticleEffect.BlockData(
                                            Material.REDSTONE_BLOCK, (byte) 0),
                                    player.getVelocity().multiply(5), 1,
                                    e.getEyeLocation().add(
                                            Math.random() - 0.5,
                                            Math.random() - 0.5,
                                            Math.random() - 0.5),
                                    Double.MAX_VALUE);
                        }
                        e.damage(9999999D);
                        // TODO: Improve particle effect on headshot.
                    } else {
                        if (this instanceof Dragunov) {
                            e.damage(19D);
                        } else {
                            e.damage(4);
                        }
                    }

                    // Simulate blood on blocks.
                    Location loc3 = null;
                    for (int x = -2; x < 2; x++) {
                        for (int y = -2; y < 2; y++) {
                            for (int z = -2; z < 2; z++) {

                                if (Math.random() < 0.25) {
                                    loc3 = e.getLocation().clone().add(x, y, z);
                                    if (loc3.getBlock().getType() != Material.AIR) {
                                        nms.displayBloodEffects(loc3);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        // Block raytracing.
        World world = player.getWorld();
        Vector end = loc.toVector().add(
                loc.getDirection().add(randomVec).multiply(64));

        Vector startPosVec = loc.toVector();
        Vec3D startPos = new Vec3D(startPosVec.getX(), startPosVec.getY(),
                startPosVec.getZ());
        Vec3D endPos = new Vec3D(end.getX(), end.getY(), end.getZ());

        MovingObjectPosition hit = ((CraftWorld) world).getHandle().rayTrace(
                startPos, endPos, true, true, true);

        if (hit != null && hit.pos != null) {
            Location blockLoc = new Location(player.getWorld(), hit.pos.a
                    + projectileDirection.getX() / 5,
                    hit.pos.b + projectileDirection.getY() / 5, hit.pos.c
                    + projectileDirection.getZ() / 5);
            ParticleEffect.BLOCK_CRACK.display(
                    new ParticleEffect.BlockData(blockLoc.getBlock().getType(),
                            blockLoc.getBlock().getData()),
                    0.5f, 0.5f, 0.5f, 1, 25, blockLoc, Double.MAX_VALUE);

            // Do not crack some types.
            if (blockLoc.getBlock().getType() == Material.AIR
                    || blockLoc.getBlock().getType() == Material.LEAVES) {
            } else {
                // Break block.
                nms.displayMaterialBreak(blockLoc);
            }
        }

        // Display fire effect.
        ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0, 20,
                player.getEyeLocation().add(
                        player.getEyeLocation().getDirection().multiply(3)),
                Double.MAX_VALUE);

        return player.getEyeLocation().getDirection().add(randomVec).multiply(
                this.projectileSpeed);
    }

    protected void playFireSound(Player player) {
        this.fireSound.play(player.getEyeLocation());
    }

    protected void playReloadSound(Player player) {
        this.reloadSound.play(player.getEyeLocation());
    }

    protected void makeRecoil(Player player, Vector projectileVelocity) {
        Vector recoil = projectileVelocity
                .multiply(-0.01f);
        recoil.add(player.getVelocity());
        recoil.setY(player
                .getVelocity()
                .getY());
        player.setVelocity(recoil);
    }

    /**
     * Toggles scope on specified item stack.
     *
     * @param player player who owns this item stack
     * @param is     item stack
     */
    public void toggleScope(Player player, ItemStack is) {
        this.toggleScope(player, is, 2);
    }

    /**
     * Toggle scoped item off to specified slot. Throws exception is item is not scoped.
     *
     * @param player player who owns the item
     * @param is     item
     * @param slotId slot to scope off the firearm
     */
    public void toggleScopeOff(Player player, ItemStack is, int slotId) {
        // Transform item.
        ItemStack nonScoped = firearmTransformer.fromScoped(is);

        // TODO: Convert to getInventory().setSlot().
        DelayedTask.of(() -> player.getInventory().setItem(slotId, nonScoped)).schedule(Time.ofTicks(1));

        // Remove slowness from scope.
        player.removePotionEffect(PotionEffectType.SLOW);
    }

    protected void toggleScope(Player player, ItemStack is, int slownessLevel) {
        // Scope tha gun.
        if (this.isScoped()) {
            // Transform item.
            ItemStack nonScoped = firearmTransformer.fromScoped(is);

            // TODO: Convert to getInventory().setSlot().
            DelayedTask.of(() -> player.setItemInHand(nonScoped)).schedule(Time.ofTicks(1));

            // Remove slowness from scope.
            player.removePotionEffect(PotionEffectType.SLOW);
        } else {
            // Transform item.
            ItemStack scoped = firearmTransformer.toScoped(is);

            // TODO: Convert to getInventory().setSlot().
            DelayedTask.of(() -> player.setItemInHand(scoped)).schedule(Time.ofTicks(1));

            // Add slowness from scope.
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.SLOW, Time.ofMinutes(30).toTicks(), slownessLevel));
        }
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack raw = super
                .toItemStack();
        FirearmItemMetaWrapper wrapper = new FirearmItemMetaWrapper(raw);
        wrapper.setCurrentAmmo(this
                .getClipSize());
        wrapper.apply(raw);
        return raw;
    }

    @Override
    public ItemStack toItemStack(int amount) {
        ItemStack raw = super
                .toItemStack(amount);
        FirearmItemMetaWrapper wrapper = new FirearmItemMetaWrapper(raw);
        wrapper.setCurrentAmmo(this
                .getClipSize());
        wrapper.setFireMode(FirearmItemMetaWrapper.FIRE_MODE_SINGLE);
        wrapper.apply(raw);
        return raw;
    }

    public boolean isScoped() {
        return false;
    }
}
