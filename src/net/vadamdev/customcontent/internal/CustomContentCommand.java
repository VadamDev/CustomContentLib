package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.api.CustomContentAPI;
import net.vadamdev.customcontent.internal.menu.CustomContentMenu;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.viapi.tools.commands.PermissionCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author VadamDev
 */
public class CustomContentCommand extends PermissionCommand {
    private final CommonRegistry commonRegistry;

    public CustomContentCommand() {
        super("customcontent");
        setPermission("customcontentlib.admin");
        setAliases(Collections.singletonList("customcontentlib"));

        commonRegistry = CustomContentPlugin.instance.getCommonRegistry();
    }

    @Override
    public boolean executePermissionCommand(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            final Player player = (Player) sender;

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("gui") || args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("items")) {
                    if(!testPermission(player, "customcontentlib.admin.menu"))
                        return false;

                    CustomContentMenu.get().open(player);
                    return true;
                }
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    final ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);

                    if(item != null)
                        player.getInventory().addItem(item);
                    else
                        player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");

                    return true;
                }else if(args[0].equalsIgnoreCase("resources")) {
                    if(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("zip")) {
                        player.sendMessage("§3CustomContentLib §f» §7Started resourcepack creation... See console for more informations");
                        CustomContentAPI.get().getModelFactory().bakeModels();
                        player.sendMessage("§3CustomContentLib §f» §aResourcepack created !");

                        return true;
                    }
                }
            }else if(args.length == 3 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get"))) {
                if(Bukkit.getPlayer(args[2]) == null) {
                    final ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);
                    if(item == null) {
                        player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                        return true;
                    }

                    try {
                        item.setAmount(Integer.parseInt(args[2]));
                    }catch(Exception ignored) {
                        player.sendMessage("§3CustomContentLib §f» §c" + args[2] + " is not a number !");
                        return true;
                    }

                    player.getInventory().addItem(item);
                    return true;
                }else {
                    final ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);

                    if(item != null)
                        Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    else
                        player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");

                    return true;
                }
            }else if(args.length == 4 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get"))) {
                if(Bukkit.getPlayer(args[2]) != null) {
                    final ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);
                    if (item == null) {
                        player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                        return true;
                    }

                    try {
                        item.setAmount(Integer.parseInt(args[3]));
                    } catch (Exception ignored) {
                        player.sendMessage("§3CustomContentLib §f» §c" + args[3] + " is not a number !");
                        return true;
                    }

                    Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    return true;
                }else
                    player.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");
            }
        }

        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                if(Bukkit.getPlayer(args[2]) != null) {
                    final ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);

                    if(item != null)
                        Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    else
                        sender.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                }else
                    sender.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");

                return true;
            }
        }else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                if(Bukkit.getPlayer(args[2]) != null) {
                    final ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);
                    if(item == null) {
                        sender.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                        return true;
                    }

                    try {
                        item.setAmount(Integer.parseInt(args[3]));
                    }catch(Exception ignored) {
                        sender.sendMessage("§3CustomContentLib §f» §c" + args[3] + " is not a number !");
                        return true;
                    }

                    Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                }else
                    sender.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");
            }

            return true;
        }

        sender.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            return Stream.of("get", "give", "resources").filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                return commonRegistry.getCustomItemstacks().keySet().parallelStream()
                        .filter(registryName -> StringUtil.startsWithIgnoreCase(registryName, args[1]))
                        .collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("resources")) {
                return Arrays.asList("create", "zip");
            }
        }

        return super.tabComplete(sender, alias, args);
    }
}
