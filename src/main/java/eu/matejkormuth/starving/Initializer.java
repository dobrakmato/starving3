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
package eu.matejkormuth.starving;

import eu.matejkormuth.starving.achievements.AchievementsModule;
import eu.matejkormuth.starving.chat.ChatModule;
import eu.matejkormuth.starving.chemistry.ChemistryModule;
import eu.matejkormuth.starving.cinematics.CinematicsModule;
import eu.matejkormuth.starving.cities.CitiesModule;
import eu.matejkormuth.starving.commands.CommandsModule;
import eu.matejkormuth.starving.configuration.ConfigurationsModule;
import eu.matejkormuth.starving.database.DatabaseModule;
import eu.matejkormuth.starving.explosions.ExplosionsModule;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import eu.matejkormuth.starving.herbology.HerbologyModule;
import eu.matejkormuth.starving.impulses.ImpulsesModule;
import eu.matejkormuth.starving.items.ItemsModule;
import eu.matejkormuth.starving.localities.LocalitiesModule;
import eu.matejkormuth.starving.loot.LootModule;
import eu.matejkormuth.starving.main.MainModule;
import eu.matejkormuth.starving.missions.MissionsModule;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.npc.NpcModule;
import eu.matejkormuth.starving.particles.ParticlesModule;
import eu.matejkormuth.starving.persistence.PersistenceModule;
import eu.matejkormuth.starving.physical.PhysicalModule;
import eu.matejkormuth.starving.remoteconnections.RemoteConnectionsModule;
import eu.matejkormuth.starving.resourcepacks.ResourcePacksModule;
import eu.matejkormuth.starving.rockets.RocketsModule;
import eu.matejkormuth.starving.rollbacker.RollbackerModule;
import eu.matejkormuth.starving.rpg.RPGModule;
import eu.matejkormuth.starving.scripts.ScriptsModule;
import eu.matejkormuth.starving.sounds.SoundsModule;
import eu.matejkormuth.starving.sounds.ambient.SoundsAmbientModule;
import eu.matejkormuth.starving.statusserver.StatusServerModule;
import eu.matejkormuth.starving.time.TimeModule;
import eu.matejkormuth.starving.warps.WarpsModule;
import eu.matejkormuth.starving.weather.WeatherModule;
import eu.matejkormuth.starving.worldgen.WorldGenModule;
import eu.matejkormuth.starving.zombies.ZombiesModule;

/**
 * This class initializes Starving server mod.
 */
public class Initializer {
    /**
     * Calling this initializes all modules required for Starving to work in specified ModuleProvider.
     *
     * @param provider provider to register modules to
     */
    public void initialize(ModuleProvider provider) {
        // Here goes initialization logic.
        provider.register(new AchievementsModule());
        provider.register(new ChatModule());
        provider.register(new ChemistryModule());
        provider.register(new CinematicsModule());
        provider.register(new CitiesModule());
        provider.register(new CommandsModule());
        provider.register(new ConfigurationsModule());
        provider.register(new DatabaseModule());
        provider.register(new ExplosionsModule());
        provider.register(new FileStorageModule());
        provider.register(new HerbologyModule());
        provider.register(new ImpulsesModule());
        provider.register(new ItemsModule());
        provider.register(new LocalitiesModule());
        provider.register(new LootModule());
        provider.register(new MainModule());
        provider.register(new MissionsModule());
        provider.register(new NMSModule());
        provider.register(new NpcModule());
        provider.register(new ParticlesModule());
        provider.register(new PersistenceModule());
        provider.register(new PhysicalModule());
        provider.register(new RemoteConnectionsModule());
        provider.register(new ResourcePacksModule());
        provider.register(new RocketsModule());
        provider.register(new RollbackerModule());
        provider.register(new RPGModule());
        provider.register(new ScriptsModule());
        provider.register(new SoundsAmbientModule());
        provider.register(new SoundsModule());
        provider.register(new StatusServerModule());
        provider.register(new TimeModule());
        provider.register(new WarpsModule());
        provider.register(new WeatherModule());
        provider.register(new WorldGenModule());
        provider.register(new ZombiesModule());
    }
}
