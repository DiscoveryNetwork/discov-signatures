package nl.parrotlync.discovsignatures.command;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import nl.parrotlync.discovsignatures.util.ChatUtil;
import nl.parrotlync.discovsuite.spigot.DiscovSuite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SignatureCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("discovsignatures.receive")) {
            if (args.length == 0) {
                ChatUtil.sendMessage(sender, "§6DiscovSignatures-1.12.2-v" + DiscovSignatures.getInstance().getDescription().getVersion() + " §7(§aParrotLync§7) - Use /signatures help", false);
                return true;
            }

            if (args[0].equalsIgnoreCase("give")) {
                Player player = (Player) sender;
                ItemStack item = DiscovSignatures.getInstance().getSignatureManager().getItem(player);
                player.getInventory().setItem(7, item);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove") && args.length == 2) {
                Player player = (Player) sender;
                if (DiscovSignatures.getInstance().getSignatureManager().removeSignature(player, Integer.parseInt(args[1]))) {
                    ChatUtil.sendMessage(sender, "§7Signature has been removed", true);
                } else {
                    ChatUtil.sendMessage(sender, "§cSignature removal failed. Please check if you provided a valid number.", true);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("accept")) {
                Player signatureSender = Bukkit.getPlayer(args[1]);
                Player receiver = (Player) sender;
                String nickname = DiscovSuite.getInstance().getNicknameManager().getNickname(signatureSender);
                String signature = DiscovSignatures.getInstance().getRequestManager().getRequest(UUID.fromString(args[2]));
                if (signature != null) {
                    DiscovSignatures.getInstance().getSignatureManager().addSignature(receiver, signature);
                    ChatUtil.sendMessage(receiver, "§7Added a signature from §c" + nickname, true);
                    ChatUtil.sendMessage(signatureSender, "§7Your signature request was §aAccepted.", true);
                } else {
                    ChatUtil.sendMessage(receiver, "§7That request has expired. Please request one again and accept within 60 seconds.", true);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("deny")) {
                Player signatureSender = Bukkit.getPlayer(args[1]);
                Player receiver = (Player) sender;
                String nickname = DiscovSuite.getInstance().getNicknameManager().getNickname(signatureSender);
                DiscovSignatures.getInstance().getRequestManager().getRequest(UUID.fromString(args[2]));
                ChatUtil.sendMessage(receiver, "§7Denied signature request from §c" + nickname, true);
                ChatUtil.sendMessage(signatureSender, "§7Your signature request was §cDenied.", true);
                return true;
            }
        }

        if (sender.hasPermission("discovsignatures.send")) {
            if (args[0].equalsIgnoreCase("send")) {
                Player player = (Player) sender;
                if (Bukkit.getPlayer(args[1]) != null) {
                    String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    String nickname = DiscovSuite.getInstance().getNicknameManager().getNickname(player);
                    String signature = ChatColor.translateAlternateColorCodes('&', message) + "\n\n§r§8§o- " + nickname;
                    UUID uuid = DiscovSignatures.getInstance().getRequestManager().addRequest(signature);
                    ChatUtil.sendMessage(player, "§7Signature request was sent to §c" + Bukkit.getPlayer(args[1]).getName(), true);
                    ChatUtil.sendSignature(player, Bukkit.getPlayer(args[1]), uuid);
                }
                return true;
            }
        }

        return help(sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("give");
            suggestions.add("send");
            suggestions.add("remove");
            return StringUtil.copyPartialMatches(args[0], suggestions, new ArrayList<>());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("send")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    suggestions.add(player.getName());
                }
                return StringUtil.copyPartialMatches(args[1], suggestions, new ArrayList<>());
            }
        }
        return suggestions;
    }

    private boolean help(CommandSender sender) {
        if (sender.hasPermission("discovsignatures.receive")) {
            ChatUtil.sendMessage(sender, "§f+---+ §6Signatures §f+---+", false);
            ChatUtil.sendMessage(sender, "§3/signatures give §7Get your Autograph Book", false);
            ChatUtil.sendMessage(sender, "§3/signatures remove <page> §7Remove a signature from your book", false);
            if (sender.hasPermission("discovsignatures.send")) {
                ChatUtil.sendMessage(sender, "§3/signatures send <player> <message> §7Send your signature to another player", false);
            }
        } else {
            ChatUtil.sendMessage(sender, "§cYou do not have permission to do that!", true);
        }
        return true;
    }
}
