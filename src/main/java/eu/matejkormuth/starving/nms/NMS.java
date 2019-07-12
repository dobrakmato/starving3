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
package eu.matejkormuth.starving.nms;

import eu.matejkormuth.starving.localities.Locality;
import eu.matejkormuth.starving.main.NMSHooks;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@NMSHooks(version = "v1_8_R3")
public class NMS {
    private Random random = new Random();

    // Allow access only trough NMSModule.
    NMS() {
    }

    public void playNamedSoundEffectMaxVolume(Player player, String soundEffectName, Location location) {
        sendPacket(player, new PacketPlayOutNamedSoundEffect(soundEffectName, location.getX(), location.getY(), location.getZ(), Float.MAX_VALUE, 1));
    }

    public void sendResourcePack(Player player, String url, String sha1lower40chars) {
        sendPacket(player, new PacketPlayOutResourcePackSend(url, sha1lower40chars));
    }

    /**
     * Material on which the break or blood effects may not be displayed.
     */
    private List<Material> badMaterials = Arrays.asList(Material.LONG_GRASS, Material.LEAVES, Material.LEAVES_2, Material.AIR);

    public void displayMaterialBreak(Location loc) {
        if (badMaterials.contains(loc.getBlock().getType()) || loc.getBlock().getType().isSolid()) {
            return;
        }

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p.getLocation().distanceSquared(loc) < 16384)
                .forEach(p -> sendPacket(p, new PacketPlayOutBlockBreakAnimation(random.nextInt(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), random.nextInt(4) + 5)));
    }

    public void displayBloodEffects(Location loc) {
        if (badMaterials.contains(loc.getBlock().getType())) {
            return;
        }

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p.getLocation().distanceSquared(loc) < 16384)
                .forEach(p -> sendPacket(p, new PacketPlayOutBlockBreakAnimation(random.nextInt(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), random.nextInt(3) + 1)));
    }

    public void playNamedSoundEffect(Player player, String soundEffectName, Location location, float volume, float pitch) {
        sendPacket(player, new PacketPlayOutNamedSoundEffect(soundEffectName, location.getX(), location.getY(), location.getZ(), volume, pitch));
    }

    public void setPlayerListHeaderFooter(Player player, String header, String footer) {
        IChatBaseComponent hj = IChatBaseComponent.ChatSerializer.a("{'text':'" + header.replace("&", "§") + "'}");
        IChatBaseComponent fj = IChatBaseComponent.ChatSerializer.a("{'text':'" + footer.replace("&", "§") + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass()
                    .getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, hj);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass()
                    .getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, fj);
            footerField.setAccessible(!headerField.isAccessible());

        } catch (Exception e) {
            e.printStackTrace();
        }
        sendPacket(player, packet);
    }

    public void updateTime(Player player, long time) {
        sendPacket(player, new PacketPlayOutUpdateTime(time, time, true));
    }

    public void sendTitle(Player p, Locality loc, int fadeIn, int fadeOut, int stay) {
        sendPacket(p, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatMessage(loc.getName()), fadeIn, stay, fadeOut));
    }

    public void sendAboveActionBarMessage(Player player, String message) {
        sendPacket(player, new PacketPlayOutChat(new ChatMessage(message), (byte) 2));
    }

    public static void sendAnimation(Entity entity, int animationId) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getLocation().distanceSquared(entity.getLocation()) < 1024)
                .forEach(player -> sendPacket(player, new PacketPlayOutAnimation(((CraftEntity) entity).getHandle(), animationId)));
    }

    @NMSHooks(version = "v1_8_R3")
    public static void sendAnimation(net.minecraft.server.v1_8_R3.Entity entity, int animationId) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getLocation().distanceSquared(entity.getBukkitEntity().getLocation()) < 1024)
                .forEach(player -> sendPacket(player, new PacketPlayOutAnimation(entity, animationId)));
    }

    @NMSHooks(version = "v1_8_R3")
    public static net.minecraft.server.v1_8_R3.World getNMSWorld(org.bukkit.World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
