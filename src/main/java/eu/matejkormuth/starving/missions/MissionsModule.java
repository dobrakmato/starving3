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
package eu.matejkormuth.starving.missions;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.cinematics.CinematicsModule;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MissionsModule extends Module {

    private static final Logger log = LoggerFactory.getLogger(MissionsModule.class);

    @Dependency
    private CinematicsModule cinematicsModule;

    @Dependency
    private FileStorageModule fileStorageModule;

    /**
     * Map of all missions.
     */
    private Map<Integer, Mission> allMissions = new HashMap<>();

    @Override
    public void onEnable() {
        StorageFactory.fileStorageModule = fileStorageModule;

        log.info("Trying to initialize all Alpha missions...");
        AlphaMissions.init(this);
    }

    @Override
    public void onDisable() {
        StorageFactory.fileStorageModule = null;
    }

    public CinematicsModule getCinematicsModule() {
        return cinematicsModule;
    }

    /**
     * Adds specific mission to game.
     *
     * @param mission mission to add
     */
    public void addMission(@Nonnull Mission mission) {
        if (allMissions.containsKey(mission.getId())) {
            log.error("Mission ID must be unique! Mission with specific id is already registered!");
            log.error(" Tried to register: {} with id {}.", mission.getName(), mission.getId());
            log.error(" Already registered: {} with id {}.", allMissions.get(mission.getId()).getName(),
                    allMissions.get(mission.getId()).getId());
            log.error("Mission {} was not registered!", mission.getName());
            return;
        }

        allMissions.put(mission.getId(), mission);
    }
}
