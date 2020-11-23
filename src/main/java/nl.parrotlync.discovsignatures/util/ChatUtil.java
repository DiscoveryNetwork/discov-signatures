package nl.parrotlync.discovsignatures.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.parrotlync.discovsignatures.DiscovSignatures;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatUtil {

    public static void sendMessage(CommandSender sender, String msg, boolean withPrefix) {
        if (withPrefix) {
            msg = "§8[§6Signatures§8] " + msg;
        }
        sender.sendMessage(msg);
    }

    public static void sendSignature(Player sender, Player receiver, UUID uuid) {
        String nickname = DiscovSignatures.getInstance().getNicknameManager().getNickname(sender);
        TextComponent main = new TextComponent("§7(§9§l!§7) §c" + nickname + " §7wants to send you their autograph!");

        TextComponent accept = new TextComponent("§8[§2Accept§8]");
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept request").create()));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ds accept " + sender.getName() + " " + uuid));

        TextComponent deny = new TextComponent(" §8[§4Deny§8]");
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny request").create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ds deny " + sender.getName() + " " + uuid));
        accept.addExtra(deny);

        receiver.spigot().sendMessage(main);
        receiver.spigot().sendMessage(accept);
    }
}
