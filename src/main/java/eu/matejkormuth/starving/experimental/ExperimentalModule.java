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
package eu.matejkormuth.starving.experimental;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.cinematics.CameraObserver;
import eu.matejkormuth.starving.cinematics.Clip;
import eu.matejkormuth.starving.cinematics.ClipPlayerBuilder;
import eu.matejkormuth.starving.cinematics.observers.*;
import eu.matejkormuth.starving.cinematics.studio.ClipBuilder;
import eu.matejkormuth.starving.commands.CommandsModule;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

public class ExperimentalModule extends Module {

    @Dependency
    private CommandsModule module;

    Material DOSKA = Material.JUKEBOX;
    Material KOLAJ = Material.WORKBENCH;

    private Predicate<Entity> RAILID = new Predicate<Entity>() {
        @Override
        public boolean test(Entity e) {
            if (e instanceof ArmorStand) {
                if ((((ArmorStand) e).getHelmet() != null && ((ArmorStand) e).getHelmet().getType() == DOSKA)) {
                    return true;
                }
            }

            return false;
        }
    };

    @Override
    public void onEnable() {
        module.command("rw1", this::rw1);
        module.command("vlak", this::vlak);
        module.command("c1", this::c1);
    }

    private boolean c1(CommandSender sender, Command command, String s, String[] strings) {
        // Execute in sync.
        DelayedTask.of(() -> {
            Clip clip = ClipBuilder.create("test1")
                    .fps(10)

                    .point(1099.5, 25, 1099.5)
                    .lookAtPoint(1099.5, 25, 1079.5)
                    .duration(Time.ofSeconds(5))
                    .point(1099.5, 25, 1080.5)
                    .duration(Time.ofSeconds(2))
                    .lookAtPoint(1099.5, 25, 1079.5)
                    .point(1099.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                    .point(1095.5, 25, 1099.5)
                    .lookAtPoint(1092.5, 25, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1095.5, 25, 1080.5)
                    .duration(Time.ofSeconds(3))
                    .lookAtPoint(1092.5, 25, 1090.5)
                    .point(1095.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                    .point(1089.5, 25, 1099.5)
                    .lookAtPoint(1089.5, 29, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1089.5, 25, 1080.5)
                    .duration(Time.ofSeconds(3))
                    .lookAtPoint(1089.5, 29, 1090.5)
                    .point(1089.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                    .point(1085.5, 25, 1099.5)
                    .lookAtPoint(1085.5, 23, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1085.5, 25, 1080.5)
                    .duration(Time.ofSeconds(3))
                    .lookAtPoint(1085.5, 23, 1090.5)
                    .point(1085.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                    .point(1081.5, 25, 1099.5)
                    .lookAtPoint(1077.5, 30, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1081.5, 25, 1080.5)
                    .duration(Time.ofSeconds(3))
                    .lookAtPoint(1077.5, 30, 1090.5)
                    .point(1081.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                            // stvorce

                    .point(1074.5, 25, 1099.5)
                    .duration(Time.ofSeconds(4))
                    .point(1074.5, 25, 1080.5)
                    .duration(Time.ofSeconds(4))
                    .point(1059.5, 25, 1080.5)
                    .duration(Time.ofSeconds(4))
                    .point(1059.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                    .point(1056.5, 25, 1099.5)
                    .lookAtPoint(1048.5, 30, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1056.5, 25, 1080.5)
                    .lookAtPoint(1048.5, 30, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1041.5, 25, 1080.5)
                    .lookAtPoint(1048.5, 30, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1041.5, 25, 1099.5)

                    .duration(Time.ofTicks(1))

                            // obojstranna ciara

                    .point(1036.5, 25, 1099.5)
                    .lookAtPoint(1038.5, 25, 1090.5)
                    .duration(Time.ofSeconds(4))
                    .point(1036.5, 25, 1080.5)
                    .duration(Time.ofSeconds(4))
                    .lookAtPoint(1034.5, 25, 1090.5)
                    .point(1036.5, 25, 1099.5)
                    .lookAtPoint(1038.5, 25, 1090.5)
                    .duration(Time.ofSeconds(3))
                    .point(1036.5, 25, 1080.5)
                    .duration(Time.ofSeconds(3))
                    .lookAtPoint(1034.5, 25, 1090.5)
                    .point(1036.5, 25, 1099.5)
                    .lookAtPoint(1038.5, 25, 1090.5)
                    .duration(Time.ofSeconds(2))
                    .point(1036.5, 25, 1080.5)
                    .duration(Time.ofSeconds(2))
                    .lookAtPoint(1034.5, 25, 1090.5)
                    .point(1036.5, 25, 1099)
                    .lookAtPoint(1038.5, 25, 1090.5)
                    .duration(Time.ofSeconds(1))
                    .point(1036.5, 25, 1080.5)
                    .duration(Time.ofSeconds(1))
                    .lookAtPoint(1034.5, 25, 1090.5)
                    .point(1036.5, 25, 1099.5)

                    .lerp()
                    .render();

            CameraObserver obs;
            if (strings.length > 0) {
                if (strings[0].equalsIgnoreCase("asp")) {
                    obs = new ArmorStandPlayerObserver((Player) sender);
                } else if (strings[0].equalsIgnoreCase("as")) {
                    obs = new ArmorStandObserver(((Player) sender).getWorld(), ((Player) sender).getLocation());
                } else if (strings[0].equalsIgnoreCase("is")) {
                    obs = new ItemStackObserver(((Player) sender).getWorld(), ((Player) sender).getLocation());
                } else if (strings[0].equalsIgnoreCase("isp")) {
                    obs = new ItemStackPlayerObserver((Player) sender);
                } else {
                    obs = new PlayerTeleportObserver((Player) sender);
                }
            } else {
                obs = new PlayerTeleportObserver((Player) sender);
            }
            sender.sendMessage("Using " + obs.toString());

            ClipPlayerBuilder.use(clip)
                    .world(Bukkit.getWorld("flatworld"))
                    .observer(obs)
                    .play();
        }).schedule(0L);
        return true;
    }

    private boolean vlak(CommandSender sender, Command command, String s, String[] strings) {
        String suffix = ", ty kokot";
        Player player = ((Player) sender);
        ArmorStand prvakolaj = null;
        for (Entity e : player.getNearbyEntities(5, 5, 5)) {
            if (e instanceof ArmorStand) {
                if (((ArmorStand) e).getHelmet() != null && ((ArmorStand) e).getHelmet().getType() == DOSKA) {
                    prvakolaj = (ArmorStand) e;
                    break;
                }
            }
        }

        if (prvakolaj == null) {
            sender.sendMessage("nemam zadnu kolaj" + suffix);
            return false;
        }

        class Vlak extends RepeatingTask {
            private ArmorStand current;
            private Vector currentForward;
            private Player player;

            public Vlak(ArmorStand kolaj, Player player) {
                this.current = kolaj;
                this.player = player;
                player.setAllowFlight(true);
                player.setFlying(true);
            }

            @Override
            public void run() {
                // Go!!
                Location pLoc = current.getLocation().clone();
                pLoc.add(0, 2, 0);

                float yaw = (float) (Math.toDegrees(current.getHeadPose().getY()));
                pLoc.setYaw(yaw);

                currentForward = pLoc.getDirection().normalize();

                player.sendMessage(ChatColor.DARK_PURPLE + currentForward.toString());
                player.sendMessage(ChatColor.LIGHT_PURPLE + player.getEyeLocation().getDirection().normalize().toString());
                double dot = currentForward.dot(player.getEyeLocation().getDirection().normalize());

                if (dot < 0) {
                    player.sendMessage(ChatColor.YELLOW + "dot => rot");
                    currentForward.multiply(-1);
                }

                player.teleport(pLoc.clone());
                player.teleport(pLoc);
                player.sendMessage("šš šš...");
                player.playSound(player.getEyeLocation(), Sound.FIZZ, 0.1f, 0.1f);
                player.playSound(player.getEyeLocation(), Sound.FIZZ, 0.1f, 1.0f);

                if (Math.random() > 0.8) {
                    player.sendMessage("tú tú tú");
                    player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.1f);
                }

                // Find next rail.
                current = nextRail();

                if (current == null) {
                    player.sendMessage("uz neni kolaj" + suffix);
                    player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.1f);
                    player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.2f);
                    player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.3f);
                    player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.4f);
                    player.sendMessage("konec jazdy vystupujes" + suffix);
                    this.cancel();
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e) {

                }
            }

            private ArmorStand nextRail() {
                ArmorStand next = null;

                double favDist = 9999999;
                double favDot = 0;
                for (Entity e : current.getNearbyEntities(3, 3, 3)) {
                    if (e == current) {
                        continue;
                    }

                    if (RAILID.test(e)) { // is candidate
                        Vector dir = e.getLocation().clone().subtract(current.getLocation()).toVector().normalize();
                        double dot = currentForward.dot(dir);
                        if (dot > 0.1) {
                            if (dir.lengthSquared() < favDist) {
                                favDist = dir.lengthSquared();
                                favDot = dot;
                                next = (ArmorStand) e;
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN + "" + favDot);
                return next;
            }
        }

        player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.1f);
        player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.2f);
        player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.3f);
        player.playSound(player.getEyeLocation(), Sound.HORSE_HIT, 0.5f, 0.4f);
        new Vlak(prvakolaj, player).schedule(1L);

        return true;
    }

    private boolean rw1(CommandSender commandSender, Command command, String s, String[] strings) {

        Location direction = ((Player) commandSender).getEyeLocation();
        direction.setPitch(0); // horizonal (notchfix wtf)

        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f forward = VectorUtils.fromBukkit(direction.getDirection().normalize());
        Vector3f right = forward.cross(up);

        Location start = ((Player) commandSender).getLocation();
        start.setYaw(0);
        start.setPitch(0);

        float kolajs = 20.0f;
        for (int kolaj = 0; kolaj < kolajs; kolaj++) {
            Vector3f cForward = forward.add(right.subtract(forward).multiply(kolaj / kolajs)).normalize();
            Vector3f cLeft = up.cross(cForward).normalize();
            Vector3f cRight = cForward.cross(up).normalize();

            Vector bForward = VectorUtils.toBukkit(cForward);
            Vector bLeft = VectorUtils.toBukkit(cLeft);
            Vector bRight = VectorUtils.toBukkit(cRight);

            summonArmorStand(DOSKA, start.add(bForward), bLeft);
            summonArmorStand(DOSKA, start.add(bForward).clone(), bLeft);
            summonArmorStand(KOLAJ, start.clone().add(bLeft.clone().multiply(0.6f)).add(0, 0.1, 0), bLeft);
            summonArmorStand(KOLAJ, start.clone().add(bRight.clone().multiply(0.6f)).add(0, 0.1, 0), bLeft);
            //summonArmorStand(KOLAJ, start.clone().add(bForward.clone().multiply(0.5f)).add(bLeft.clone().multiply(0.6f)).add(0, 0.1 ,0), bLeft);
            //summonArmorStand(KOLAJ, start.clone().add(bForward.clone().multiply(0.5f)).add(bRight.clone().multiply(0.6f)).add(0, 0.1 ,0), bLeft);
            summonArmorStand(DOSKA, start.add(bForward), bLeft);
        }
        return true;
    }

    public static ArmorStand summonArmorStand(Material head, Location loc, Vector forward) {
        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        EulerAngle eulerAngle = new EulerAngle(0, Math.atan2(forward.getZ(), forward.getX()), 0);
        as.setHeadPose(eulerAngle);
        as.setHelmet(new ItemStack(head));
        as.setVisible(false);
        as.setArms(true);
        as.setGravity(false);
        return as;
    }

    @Override
    public void onDisable() {

    }
}
