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
