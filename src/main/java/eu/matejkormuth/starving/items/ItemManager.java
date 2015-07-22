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

import eu.matejkormuth.starving.items.base.*;
import eu.matejkormuth.starving.items.blocks.*;
import eu.matejkormuth.starving.items.blocks.oakstairs.*;
import eu.matejkormuth.starving.items.clothing.*;
import eu.matejkormuth.starving.items.consumables.MagicMushroom;
import eu.matejkormuth.starving.items.drinks.Fanta;
import eu.matejkormuth.starving.items.drinks.RedBull;
import eu.matejkormuth.starving.items.drinks.Sprite;
import eu.matejkormuth.starving.items.explosives.*;
import eu.matejkormuth.starving.items.firearms.*;
import eu.matejkormuth.starving.items.firearms.scoped.*;
import eu.matejkormuth.starving.items.food.CannedMeat;
import eu.matejkormuth.starving.items.food.CannedVegetables;
import eu.matejkormuth.starving.items.food.Raspberry;
import eu.matejkormuth.starving.items.food.Strawberry;
import eu.matejkormuth.starving.items.medical.Bandage;
import eu.matejkormuth.starving.items.medical.Patch;
import eu.matejkormuth.starving.items.medical.Splint;
import eu.matejkormuth.starving.items.melee.*;
import eu.matejkormuth.starving.items.misc.*;
import eu.matejkormuth.starving.items.ranged.Crossbow;
import eu.matejkormuth.starving.items.ranged.LoadedCrossbow;
import eu.matejkormuth.starving.main.Data;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.rockets.RocketsModule;
import eu.matejkormuth.starving.sounds.SoundsModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemManager implements Listener {

    private static final Logger log = LoggerFactory.getLogger(ItemManager.class);
    private final Set<Item> items;

    private SoundsModule soundsModule;
    private NMSModule nmsModule;
    private RocketsModule rocketsModule;

    public ItemManager(SoundsModule soundsModule, NMSModule nmsModule, RocketsModule rocketsModule) {
        this.soundsModule = soundsModule;
        this.nmsModule = nmsModule;
        this.rocketsModule = rocketsModule;

        log.info("Initializing ItemManager...");
        this.items = new HashSet<>();
        // Register all items.
        this.registerAll();
        this.registerAdditionalRecipes();
        log.info("We have {} different registered items!", this.items.size());
    }

    private void registerAdditionalRecipes() {
        // TODO: Register chemical crafting recipes.
        // TODO: Register water crafting recipes.
    }

    private void registerAll() {
        this.register(new Parachute());
        this.register(new GalvanicCell());
        this.register(new Transmitter());
        this.register(new Toolset());
        this.register(new MagicMushroom());
        this.register(new DisinfectionTablets());
        this.register(new Flashlight());

        this.registerFood();
        this.registerFirearms();
        this.registerRanged();
        this.registerMelee();
        this.registerMedical();
        this.registerDrinks();
        this.registerClothing();
        this.registerBlocks();
        this.registerExplosives();
    }

    private void registerExplosives() {
        this.register(new C4());
        this.register(new Detonator());
        this.register(new FlareGun());
        this.register(new Grenade(soundsModule));
        this.register(new Molotov(soundsModule));
        this.register(new Petard(soundsModule));
        this.register(new RPG7(rocketsModule));
        this.register(new SmokeShell(soundsModule));
    }

    private void registerMelee() {
        this.register(new IronPipe());
        this.register(new IronPipeWithMetalRods());
        this.register(new IronPipeWithMetaRodsAndKnife());
        this.register(new Knife());
        this.register(new WoodenStick());
        this.register(new WoodenStickWithMetalRods());
        this.register(new WoodenStickWithMetalRodsAndKnife());
        this.register(new Axe());
        this.register(new Machete());
    }

    private void registerFood() {
        this.register(new CannedMeat());
        this.register(new CannedVegetables());
        this.register(new Raspberry());
        this.register(new Strawberry());
    }

    private void registerBlocks() {
        this.registerBlocksWithData();
    }

    private void registerBlocksWithData() {
        this.register(new LogD12());
        this.register(new LogD13());
        this.register(new LogD14());
        this.register(new LogD15());

        this.register(new Log2D12());
        this.register(new Log2D13());

        this.register(new OakStairsD1());
        this.register(new OakStairsD2());
        this.register(new OakStairsD3());
        this.register(new OakStairsD4());
        this.register(new OakStairsD5());
        this.register(new OakStairsD6());
        this.register(new OakStairsD7());
        this.register(new OakStairsD8());
    }

    private void registerRanged() {
        this.register(new Crossbow(this));
        this.register(new LoadedCrossbow(this));
    }

    private void registerFirearms() {
        // Unscoped variations.
        this.register(new AK47(this, soundsModule, nmsModule));
        this.register(new Glock(this, soundsModule, nmsModule));
        this.register(new Dragunov(this, soundsModule, nmsModule));
        this.register(new M16(this, soundsModule, nmsModule));
        this.register(new Mossberg500(this, soundsModule, nmsModule));
        this.register(new MP5(this, soundsModule, nmsModule));
        this.register(new Revolver(this, soundsModule, nmsModule));
        this.register(new NickyAnaconda(this, soundsModule, nmsModule));

        // Scoped variations.
        this.register(new ScopedAK47(this, soundsModule, nmsModule));
        this.register(new ScopedGlock(this, soundsModule, nmsModule));
        this.register(new ScopedDragunov(this, soundsModule, nmsModule));
        this.register(new ScopedM16(this, soundsModule, nmsModule));
        this.register(new ScopedMossberg500(this, soundsModule, nmsModule));
        this.register(new ScopedMP5(this, soundsModule, nmsModule));
        this.register(new ScopedRevolver(this, soundsModule, nmsModule));
        this.register(new ScopedNickyAnaconda(this, soundsModule, nmsModule));
    }

    private void registerDrinks() {
        this.register(new Fanta());
        this.register(new Sprite());
        this.register(new RedBull());
    }

    private void registerClothing() {
        this.register(new Boots());
        this.register(new BulletproofVest());
        this.register(new CamoflageHelmet());
        this.register(new CamoflageThickPants());
        this.register(new CamoflageThickShirt());
        this.register(new Cap());
        this.register(new Hat());
        this.register(new Jeans());
        this.register(new RemVest());
        this.register(new RubberShoes());
        this.register(new Sandals());
        this.register(new Shield());
        this.register(new Shirt());
        this.register(new Shoes());
        this.register(new Shorts());
        this.register(new ThickPants());
        this.register(new ThickShoes());
        this.register(new TShirt());
        this.register(new Windbreaker());
        this.register(new WinterPants());
    }

    private void registerMedical() {
        this.register(new Bandage());
        this.register(new Patch());
        this.register(new Splint());
    }

    private void register(final Item item) {
        if (this.items.contains(item)) {
            // Do not register more then once.
            return;
        }
        // If is item craftable, register recipe.
        if (item instanceof Craftable) {
            Bukkit.addRecipe(((Craftable) item).getRecipe());
        }
        // Add to set.
        this.items.add(item);
    }

    public ItemStack newItemStack(Class<? extends Item> clazz) {
        return this.newItemStack(clazz, 1);
    }

    public ItemStack newItemStack(Class<? extends Item> clazz, int amount) {
        for (Item i : this.items) {
            if (i.getClass().equals(clazz)) {
                return i.toItemStack(amount);
            }
        }
        return null;
    }

    public Item findItem(final ItemStack itemStack) {
        // Special case for chemicals.
        if (this.isChemical(itemStack)) {
            // Try to look at known chemical compounds.
            // Sorry chemicals are no longer implemented.
        }

        // For every other item.
        for (Item item : this.items) {
            if (item.matches(itemStack)) {
                return item;
            }
        }
        return null;
    }

    public boolean isItemOf(final ItemStack itemStack,
                            Class<? extends Item> type) {
        Item i = this.findItem(itemStack);
        if (i == null) {
            return false;
        }
        // TODO: This might have bad performance.
        return i.getClass().equals(type);
    }

    private boolean isChemical(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        if (itemStack.getType() != Material.POTION) {
            return false;
        }
        if (itemStack.hasItemMeta()) {
            List<String> l = itemStack.getItemMeta().getLore();
            return l.size() > 1 && l.get(0).contains("chemical");
        }
        return false;
    }

    public List<Item> getItems() {
        return new ArrayList<>(this.items);
    }

    @EventHandler
    private void onInteract(final PlayerInteractEvent event) {
        Item item = this.findItem(event.getItem());

        if (item instanceof BlockWithData) {
            return;
        }

        if (item != null) {
            InteractResult result = item.onInteract(event.getPlayer(),
                    event.getAction(), event.getClickedBlock(),
                    event.getBlockFace());

            if (result.getUsedAmount() > event.getItem().getAmount()) {
                throw new IllegalArgumentException("Used amount is bigger then itemStack amount!");
            }

            // Simulate use.
            if (result.isUsed()) {
                // If use all.
                if (result.getUsedAmount() == -1) {
                    DelayedTask.of(() -> event.getPlayer().getInventory().setItemInHand(null))
                            .schedule(Time.ofTicks(2));

                } else {
                    if (event.getItem().getAmount() <= 1) {
                        DelayedTask.of(() -> event.getPlayer().getInventory().setItemInHand(null))
                                .schedule(Time.ofTicks(2));
                    } else {
                        event.getItem().setAmount(event.getItem().getAmount() - result.getUsedAmount());
                    }
                }
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInteractWith(final PlayerInteractEntityEvent event) {
        Item item = this.findItem(event.getPlayer().getItemInHand());

        if (item instanceof BlockWithData) {
            return;
        }

        if (item != null) {
            item.onInteractWith(event.getPlayer(), event.getRightClicked());
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onConsume(final PlayerItemConsumeEvent event) {
        Item item = this.findItem(event.getItem());
        if (item != null) {
            if (item instanceof ConsumableItem) {
                ((ConsumableItem) item).onConsume(event.getPlayer());

                if (event.getPlayer().getItemInHand().getAmount() > 1) {
                    event.getPlayer().getItemInHand().setAmount(
                            event.getPlayer().getItemInHand().getAmount());
                } else {
                    event.getPlayer().setItemInHand(null);
                }

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        Item item = this.findItem(event.getItemInHand());
        if (item != null) {
            if (item instanceof BlockWithData) {
                ((BlockWithData) item).onPlaced(event.getPlayer(),
                        event.getBlockPlaced());
            }
        }
    }

    @EventHandler
    private void onAttack(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (((Player) event.getDamager()).getItemInHand() != null) {
                Item item = this.findItem(((Player) event.getDamager()).getItemInHand());
                if (item != null) {
                    if (item instanceof MeleeWeapon) {
                        ((MeleeWeapon) item).onAttack(
                                (Player) event.getDamager(),
                                (LivingEntity) event.getEntity(),
                                event.getDamage());
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    // Used to unscope firearms, when changing slots.
    @EventHandler
    private void onSelectedSlotChanged(final PlayerItemHeldEvent event) {
        Data data = Data.of(event.getPlayer());

        if (data.isScoped()) {
            Item item = findItem(event.getPlayer().getInventory().getItem(event.getPreviousSlot()));
            if (item instanceof Firearm) {
                // Unscope him.
                item.onInteract(event.getPlayer(), Action.LEFT_CLICK_AIR, null, null);
            }
        }
    }

    // Crafting and max. stack amount emulation.

    @EventHandler
    private void onItemStackMerge(final InventoryClickEvent event) {
        // Max stack emulation.
        if (event.getCursor() != null) {
            // Beware: event.getCurrentItem() returns undocumented null, when
            // throwing items out of inventory.
            if (event.getCurrentItem() == null) {
                // We have nothing to merge.
                return;
            }

            // If merging stacks of same type.
            if (event.getCurrentItem().getType()
                    .equals(event.getCursor().getType())) {
                Item item1 = this.findItem(event.getCurrentItem());
                Item item2 = this.findItem(event.getCursor());
                // Check if both are custom.
                if (item1 != null && item2 != null) {
                    int totalAmount = event.getCurrentItem().getAmount()
                            + event.getCursor().getAmount();
                    // Check if this merge exceeds maxStackAmount.
                    if (item1.getMaxStackAmount() > totalAmount) {
                        // Disable this merge.
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
