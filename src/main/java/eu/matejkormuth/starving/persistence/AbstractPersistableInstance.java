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
package eu.matejkormuth.starving.persistence;

/**
 * Class that injects {@link Persist} annotated configuration values to
 * sub-class automatically in constructor.
 * 
 * @see Persist
 * @see PersistInjector
 * @see AbstractPersistable
 */
public abstract class AbstractPersistableInstance implements
        PersistableInstance {
    /**
     * Id of this instance of this object.
     */
    private int persistableInstanceId;

    /**
     * Main constructor with this implementation:
     * 
     * <pre>
     * public AbstractPersistable() {
     *     PersistInjector.inject(this, instanceId);
     * }
     * </pre>
     * 
     * @param instanceId
     *            id of instance of persistable object to load
     */
    public AbstractPersistableInstance(final int instanceId) {
        // Automatically inject values from configuration.
        this.persistableInstanceId = instanceId;
        PersistInjector.inject(this, this.persistableInstanceId);
    }

    /*
     * (non-Javadoc)
     * @see eu.matejkormuth.rpgdavid.starving.persistence.PersistableInstance#
     * getPersistableInstanceId()
     */
    @Override
    public int getPersistableInstanceId() {
        return this.persistableInstanceId;
    }

    /*
     * (non-Javadoc)
     * @see eu.matejkormuth.rpgdavid.starving.persistence.PersistableInstance#
     * reloadConfiguration()
     */
    @Override
    public void reloadConfiguration() {
        PersistInjector.inject(this, this.persistableInstanceId);
    }

    /*
     * (non-Javadoc)
     * @see eu.matejkormuth.rpgdavid.starving.persistence.PersistableInstance#
     * saveConfiguration()
     */
    @Override
    public void saveConfiguration() {
        PersistInjector.store(this, this.persistableInstanceId);
    }
}
