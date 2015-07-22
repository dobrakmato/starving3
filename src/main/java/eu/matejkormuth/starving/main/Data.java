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
package eu.matejkormuth.starving.main;

import eu.matejkormuth.starving.persistence.AbstractPersistable;
import eu.matejkormuth.starving.persistence.Persist;
import eu.matejkormuth.starving.persistence.PersistInjector;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Holds all information about Starving player. Bettern name would be
 * <b>PlayerData</b>, but that's too long or <b>PData</b>, but that's too
 * confusing. <i>Fields of this class are serialized using
 * {@link PersistInjector} although the class does not extend
 * {@link AbstractPersistable}</i>.
 * </p>
 * <p>
 * You can obtain player data by calling static {@link Data#of(Player)} method
 * which returns {@link Data} object for specified Player.
 * </p>
 * <p>
 * Example usage will be:
 * <p>
 * <pre>
 * Data.of(player).setInfected(true);
 * </pre>
 * <p>
 * </p>
 */
public class Data {
    private static final Map<OfflinePlayer, Data> cached;
    static Path profilesRoot;

    static {
        cached = new HashMap<>();
    }

    public static Collection<Data> cached() {
        return cached.values();
    }

    private static File dataFileOf(OfflinePlayer player) {
        return profilesRoot.resolve(player.getUniqueId().toString() + ".properties").toFile();
    }

    /**
     * <p>
     * Returns Data object related to specified player. This method first checks
     * if Data object is cached. If not, it either loads data from harddisk or
     * creates a new default Data object.
     * </p>
     * This method never returns null.
     *
     * @param player player object of which data soud be loaded
     * @return Data object which holds all informations related to this player
     */
    public static Data of(OfflinePlayer player) {
        if (cached.containsKey(player)) {
            return cached.get(player);
        } else {
            File f = null;
            if ((f = dataFileOf(player)).exists()) {
                return loadData(f, player);
            } else {
                return createData(player);
            }
        }
    }

    private static Data loadData(File f, OfflinePlayer player) {
        return new Data(f, player);
    }

    private static Data createData(OfflinePlayer player) {
        return new Data(player);
    }

    private OfflinePlayer player;

    // Fields annotated with Persist will be saved to file.

    @Persist(key = "bleedingTicks")
    private int bleedingTicks = 0;
    @Persist(key = "bleedingFlow")
    private float bleedingFlow = 0;
    @Persist(key = "bloodLevel")
    private float bloodLevel = 5000;

    @Persist(key = "stamina")
    private float stamina = 800;
    @Persist(key = "staminaCapacity")
    private float staminaCapacity = 800;

    @Persist(key = "bodyTemperature")
    private float bodyTemperature;

    @Persist(key = "infected")
    private boolean infected = false;

    @Persist(key = "sick")
    private boolean sick = false;

    @Persist(key = "ableToSprint")
    private boolean ableToSprint = true;

    @Persist(key = "hydrationLevel")
    private float hydrationLevel = 1200;
    @Persist(key = "hydrationCapacity")
    private float hydrationCapacity = 1200;

    @Persist(key = "scoped")
    private boolean scoped;
    @Persist(key = "unconscious")
    private boolean unconscious;
    @Persist(key = "hallucinating")
    private boolean hallucinating;

    @Persist(key = "flashlightOn")
    private boolean flashlightOn;

    @Persist(key = "remoteAccessKey")
    private String remoteAccessKey;

    @Persist(key = "resourcePack")
    private String resourcePack = "players";

    @Persist(key = "skillPoints")
    private int skillPoints = 0;

    @Persist(key = "playTime")
    private long playTime = 0;

    // public StarvingScoreboard scoreboard;

    private Data(OfflinePlayer player) {
        this.player = player;

        cached.put(player, this);
    }

    public Data(File f, OfflinePlayer p) {
        PersistInjector.inject(this, f);
        this.player = p;

        cached.put(player, this);
    }

    /**
     * Serializes values on persist-annotated fields to data file saved by
     * player's unique id.
     *
     * @return instance of self
     */
    public Data save() {
        PersistInjector.store(this, dataFileOf(this.player));
        return this;
    }

    /**
     * Removes this Data item from cached player Data-s.
     *
     * @return instance of self
     */
    public Data uncache() {
        cached.remove(this.player);
        return this;
    }

    /**
     * <p>
     * Resets all fields (excluding player field) in this instance of Data to
     * their default state. Note that this method does not remove file on disk
     * nor automatically saves state to disk.
     * </p>
     * <p>
     * Basically this resets player's game data.
     * </p>
     * <p>
     * <b>This method does not resets permanent data! (eg. remoteAccessKey) If
     * you need to reset all data (including permanent properties) please use
     * reset(boolean).</b>
     * </p>
     *
     * @return instance of self
     */
    public Data reset() {
        return reset(false);
    }

    /**
     * <p>
     * Resets all fields (excluding player field) in this instance of Data to
     * their default state. Note that this method does not remove file on disk
     * nor automatically saves state to disk.
     * </p>
     * <p>
     * Basically this resets player's game data.
     * </p>
     *
     * @param resetPermanent whether to reset permanent properties (eg. remoteAccessKey).
     * @return instance of self
     */
    public Data reset(boolean resetPermanent) {
        // TODO: Externalize default values.
        this.ableToSprint = true;
        this.bleedingFlow = 0;
        this.bleedingTicks = 0;
        this.bloodLevel = 5000;
        this.bodyTemperature = 37;
        this.infected = false;
        this.sick = false;
        this.stamina = 800;
        this.staminaCapacity = 800;
        this.hydrationCapacity = 1200;
        this.hydrationLevel = 1200;
        this.unconscious = false;
        this.scoped = false;
        this.hallucinating = false;
        this.flashlightOn = false;
        this.resourcePack = "players";
        this.skillPoints = 0;
        this.playTime = 0;

        // We not reset access key.
        if (resetPermanent) {
            this.remoteAccessKey = null;
        }

        return this;
    }

    // May return null.
    public OfflinePlayer getPlayer() {
        return this.player;
    }

    public int getBleedingTicks() {
        return this.bleedingTicks;
    }

    public void setBleedingTicks(int bleedingTicks) {
        this.bleedingTicks = bleedingTicks;
    }

    public void decrementBleedingTicks() {
        this.bleedingTicks--;
    }

    public float getBleedingFlow() {
        return this.bleedingFlow;
    }

    public void setBleedingFlow(float bleedingFlow) {
        this.bleedingFlow = bleedingFlow;
    }

    public float getBloodLevel() {
        return this.bloodLevel;
    }

    public void setBloodLevel(float bloodLevel) {
        this.bloodLevel = bloodLevel;
    }

    public void incrementBloodLevel(float amount) {
        this.bloodLevel += amount;
    }

    public void decrementBloodLevel(float amount) {
        this.bloodLevel -= amount;
    }

    public boolean isBleeding() {
        return this.bleedingTicks > 0;
    }

    public float getStamina() {
        return this.stamina;
    }

    public float getStaminaCapacity() {
        return this.staminaCapacity;
    }

    public void incrementStamina(float amount) {
        this.stamina += amount;
    }

    public void decrementStamina(float amount) {
        this.stamina -= amount;
    }

    public void setStaminaCapacity(float staminaCapacity) {
        this.staminaCapacity = staminaCapacity;
    }

    public float getBodyTemperature() {
        return this.bodyTemperature;
    }

    public void setBodyTemperature(float bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
        if (infected) {
            this.sick = true;
        }
    }

    public boolean isInfected() {
        return this.infected;
    }

    public void setAbleToSprint(boolean ableToSprint) {
        this.ableToSprint = ableToSprint;
    }

    public boolean isAbleToSprint() {
        return this.ableToSprint;
    }

    public float getHydrationCapacity() {
        return this.hydrationCapacity;
    }

    public void setHydrationCapacity(float hydrationCapacity) {
        this.hydrationCapacity = hydrationCapacity;
    }

    public float getHydrationLevel() {
        return this.hydrationLevel;
    }

    public void setHydrationLevel(float hydrationLevel) {
        this.hydrationLevel = hydrationLevel;
    }

    public void incrementHydrationLevel(float amount) {
        this.hydrationLevel += amount;
    }

    public void setSick(boolean sick) {
        this.sick = sick;
    }

    public boolean isSick() {
        return this.sick;
    }

    public boolean switchScoped() {
        return this.scoped = !this.scoped;
    }

    public boolean isScoped() {
        return this.scoped;
    }

    public boolean isUnconscious() {
        return this.unconscious;
    }

    public void setUnconscious(boolean unconscious) {
        this.unconscious = unconscious;
    }

    public void setHallucinating(boolean halucinating) {
        this.hallucinating = halucinating;
    }

    public boolean isHallucinating() {
        return this.hallucinating;
    }

    public boolean isFlashlightOn() {
        return this.flashlightOn;
    }

    public void setFlashlightOn(boolean flashlightOn) {
        this.flashlightOn = flashlightOn;
    }

    public String getRemoteAccessKey() {
        return this.remoteAccessKey;
    }

    public void setRemoteAccesKey(String remoteAccessKey) {
        this.remoteAccessKey = remoteAccessKey;
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public void incrementPlayTime() {
        this.playTime += 1;
    }
}
