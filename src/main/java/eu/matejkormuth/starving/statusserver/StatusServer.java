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
package eu.matejkormuth.starving.statusserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import eu.matejkormuth.starving.main.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.jar.Manifest;


public class StatusServer extends RepeatingTask {

    HttpServer server;
    int maxPlayers;
    int currentPlayers;
    String mcVersion;
    String starvingVersionTitle;
    String starvingVersionVersion;
    String starvingVersionBuildNumber;
    String starvingVersionSCMRevision;
    Collection<? extends Player> players;

    @Override
    public void run() {
        maxPlayers = Bukkit.getMaxPlayers();
        players = Bukkit.getOnlinePlayers();
        currentPlayers = players.size();
        mcVersion = Bukkit.getVersion();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(9063), 0);
        server.createContext("/status", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        this.findStarvingVersion();
        this.run();
        this.schedule(40L);
    }

    private void findStarvingVersion() {
        // Output self version.
        URLClassLoader cl = ((URLClassLoader) this.getClass()
                .getClassLoader());
        try {
            URL url = cl.findResource("META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(url.openStream());
            // do stuff with it
            String title = manifest.getMainAttributes().getValue(
                    "Implementation-Title");
            String version = manifest.getMainAttributes().getValue(
                    "Implementation-Version");
            String buildnumber = manifest.getMainAttributes().getValue(
                    "Implementation-Build-Number");
            String scmRevision = manifest.getMainAttributes().getValue(
                    "Implementation-SCM-Revision");
            starvingVersionTitle = title;
            starvingVersionBuildNumber = buildnumber;
            starvingVersionSCMRevision = scmRevision;
            starvingVersionVersion = version;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.cancel();
        server.stop(1);
    }

    class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            JsonObject json = new JsonObject();
            json.addProperty("maxPlayers", maxPlayers);
            json.addProperty("currentPlayers", currentPlayers);
            JsonObject version = new JsonObject();
            version.addProperty("minecraft", mcVersion);
            JsonObject starvingVersion = new JsonObject();
            starvingVersion.addProperty("title", starvingVersionTitle);
            starvingVersion.addProperty("version", starvingVersionVersion);
            starvingVersion.addProperty("build", starvingVersionBuildNumber);
            starvingVersion.addProperty("revision", starvingVersionSCMRevision);
            version.add("starving", starvingVersion);
            json.add("version", version);
            JsonArray playersArray = new JsonArray();
            for (Player p : players) {
                playersArray.add(new JsonPrimitive(p.getName()));
            }
            json.add("players", playersArray);
            String response = json.toString();
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
