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

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

@UtilityClass
public final class Colors {
    private static String colorize(@Nonnull ChatColor color, @Nonnull String msg) {
        return color + msg;
    }

    public static String aqua(@Nonnull String message) {
        return colorize(ChatColor.AQUA, message);
    }

    public static String black(@Nonnull String message) {
        return colorize(ChatColor.BLACK, message);
    }

    public static String blue(@Nonnull String message) {
        return colorize(ChatColor.BLUE, message);
    }

    public static String darkAqua(@Nonnull String message) {
        return colorize(ChatColor.DARK_AQUA, message);
    }

    public static String darkBlue(@Nonnull String message) {
        return colorize(ChatColor.DARK_BLUE, message);
    }

    public static String darkGrey(@Nonnull String message) {
        return colorize(ChatColor.DARK_GRAY, message);
    }

    public static String darkGreen(@Nonnull String message) {
        return colorize(ChatColor.DARK_GREEN, message);
    }

    public static String darkPurple(@Nonnull String message) {
        return colorize(ChatColor.DARK_PURPLE, message);
    }

    public static String darkRed(@Nonnull String message) {
        return colorize(ChatColor.DARK_RED, message);
    }

    public static String gold(@Nonnull String message) {
        return colorize(ChatColor.GOLD, message);
    }

    public static String gray(@Nonnull String message) {
        return colorize(ChatColor.GRAY, message);
    }

    public static String green(@Nonnull String message) {
        return colorize(ChatColor.GREEN, message);
    }

    public static String lightPurple(@Nonnull String message) {
        return colorize(ChatColor.LIGHT_PURPLE, message);
    }

    public static String magic(@Nonnull String message) {
        return colorize(ChatColor.MAGIC, message);
    }

    public static String red(@Nonnull String message) {
        return colorize(ChatColor.RED, message);
    }

    public static String white(@Nonnull String message) {
        return colorize(ChatColor.WHITE, message);
    }

    public static String yellow(@Nonnull String message) {
        return colorize(ChatColor.YELLOW, message);
    }
}
