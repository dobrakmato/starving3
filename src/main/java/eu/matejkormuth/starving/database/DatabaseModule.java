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
package eu.matejkormuth.starving.database;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.configuration.ConfigurationsModule;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseModule extends Module {

    private static final Logger log = LoggerFactory.getLogger(DatabaseModule.class);

    @Dependency
    private ConfigurationsModule configurationsModule;

    private YamlConfiguration configuration;

    // Database connection.
    private Database database;
    // All registered entities.

    @Override
    public void onEnable() {
        configuration = configurationsModule.loadOrCreate("database");

        setupDatabase();
    }

    private void setupDatabase() {
        try {
            database = new Database(configuration.getString("url"), configuration.getString("user"),
                    configuration.getString("password"));
        } catch (SQLException e) {
            log.error("Can't connect to database!", e);
        }

        List<Class<? extends Entity>> entityRegister = new ArrayList<>();
        EntityRegister.collectTo(entityRegister);
        // Trigger <clinit>s to initialize entityDatas.
        for (Class<? extends Entity> entityClass : entityRegister) {
            try {
                entityClass.getDeclaredField("data").get(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error("Can't trigger <clinit> on " + entityClass.getName(), e);
            }
        }
    }

    @Override
    public void onDisable() {
        configurationsModule.save("database", configuration);
    }


}
