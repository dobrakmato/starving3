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
package eu.matejkormuth.starving.scripts;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class ScriptExecutor {

    private final String GLOBAL_METHODS = "for(var f in u)\"function\"==typeof u[f]&&(this[f]=function(){var n=u[f];return function(){return n.apply(u,arguments)}}());";

    private ScriptEngineManager manager;
    private ScriptEngine jsEngine;

    public ScriptExecutor() {
        this.manager = new ScriptEngineManager();
        this.jsEngine = this.manager.getEngineByExtension("js");

        try {
            this.createGlobalMethods();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private void createGlobalMethods() throws ScriptException {
        Map<String, Object> map = new HashMap<>();
        // Inject ScriptAPI object.
        map.put("u", new Object()); // TODO: ScriptAPI
        // Register global methods.
        this.jsEngine.eval(GLOBAL_METHODS, new SimpleBindings(map));
    }

    public void execute(String script) {
        try {
            this.jsEngine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
            // Starving.getInstance().debug("Script/" + e.toString());
        }
    }
}
