package plgs.diltz.chattags;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

class PermissionData {
    public static String format;
    public static int weight;

    public void setFormat(String format) {
        this.format = format;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

public class OnPlayerChat implements Listener {
    public static FileConfiguration Config = Main.Config;
    private static int OP_Priority = Config.getInt("OP_Priority");

    public static String format(String toFormat, Player player, String originalMessage) {
        toFormat = toFormat.replaceAll("tag_player_name", player.getName())
                .replaceAll("tag_message", originalMessage);

        return toFormat;
    }

    private static PermissionData getPermissionData(Player player) {
        PermissionData toReturn = new PermissionData();
        toReturn.setFormat(Config.getString("Default"));
        toReturn.setWeight(0);

        Config.getConfigurationSection("Custom").getKeys(false).forEach(key -> {
            String Permission = Config.getString("Custom." + key + ".permission");
            int PermissionWeight = Config.getInt("Custom." + key + ".weight");

            if (player.hasPermission(Permission) && toReturn.weight <= PermissionWeight) {
                toReturn.setWeight(PermissionWeight);
                toReturn.setFormat(Config.getString("Custom." + key + ".format"));
            }
        });

        return toReturn;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PermissionData permData = getPermissionData(player);
        String formattedMessage = format(permData.format, player, event.getMessage());

        if (player.isOp() && Config.getInt("OP_Priority") == 1) {
            formattedMessage = format(Config.getString("OP"), player, event.getMessage());
        }

        event.setFormat(formattedMessage);
    }
}
