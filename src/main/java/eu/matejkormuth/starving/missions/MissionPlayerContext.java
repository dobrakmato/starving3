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

import java.util.HashMap;
import java.util.Map;

public class MissionPlayerContext {

    private Map<String, Object> missionStorage;
    
    public MissionPlayerContext() {
        missionStorage = new HashMap<>();
    }

    public boolean isCompleted(Goal goal) {
        return toBool("goals." + goal.getId() + ".completed");
    }

    public void setCompleted(Goal goal, boolean completed) {
        missionStorage.put("goals." + goal.getId() + ".completed", completed);
    }

    public boolean isCompleted(Mission mission) {
        return toBool("missions." + mission.getId() + ".completed");
    }

    public void setCompleted(Mission mission, boolean completed) {
        missionStorage.put("missions." + mission.getId() + ".completed", completed);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) missionStorage.get(key);
    }
    
    public void set(String key, Object value) {
        missionStorage.put(key, value);
    }
    
    public Map<String, Object> getMissionStorage() {
        return missionStorage;
    }
    
    private boolean toBool(String key) {
        if(!missionStorage.containsKey(key)) {
            return false;
        }
        
        Object val = missionStorage.get(key);
        
        if(val == null) {
            return false;
        }
        
        if(val instanceof Number) {
            return ((Number) val).doubleValue() < 0;
        }
        
        if(val instanceof Boolean) {
            return ((Boolean) val).booleanValue();
        }
        
        return true;
    }

}
