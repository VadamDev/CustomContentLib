package net.vadamdev.customcontent;

import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.viaapi.tools.commands.PermissionCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
        setAliases(Collections.singletonList("customcontentlib"));

        commonRegistry = CustomContentLib.instance.getCommonRegistry();
    }

    @Override
    public boolean executePermissionCommand(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
                StringBuilder stringBuilder = new StringBuilder("§3CustomContentLib §f» §bHere's the list of registered custom items: ");
                commonRegistry.getCustomItemstacks().forEach((registryName, itemStack) -> stringBuilder.append(registryName + ", "));
                player.sendMessage(stringBuilder.toString());
            }else if(args.length == 1 && args[0].equalsIgnoreCase("dump")) {
                try {
                    Date date = new Date();
                    String fileName = "dump_" + new SimpleDateFormat("MM_dd_yyyy_kk_mm").format(date) + ".txt";
                    File file = new File(CustomContentLib.instance.getDataFolder() + File.separator + fileName);

                    if(!file.exists())
                        file.createNewFile();

                    FileWriter writer = new FileWriter(file);

                    commonRegistry.getCustomItems().forEach(customItem -> {
                        try {
                            writer.append(customItem.getRegistryName() + ":" + customItem.getItemStack().getType().getId()).append("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get"))) {
                ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);

                if(item != null)
                    player.getInventory().addItem(item);
                else
                    player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
            }else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) == null) {
                        ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);
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
                    }else {
                        Player target = Bukkit.getPlayer(args[2]);

                        ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);

                        if(item != null) target.getInventory().addItem(item);
                        else player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                    }
                }else
                    player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);
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
                    }
                }else
                    player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
            }else
                player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
        }else {
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        Player target = Bukkit.getPlayer(args[2]);

                        ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);

                        if(item != null) target.getInventory().addItem(item);
                        else sender.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                    }else sender.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");
                }
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        ItemStack item = commonRegistry.getCustomItemAsItemStack(args[1]);
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
                    }else sender.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String getGlobalPermission() {
        return "customcontentlib.admin";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            return Stream.of("get", "give").filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
        }else if(args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get"))) {
            return commonRegistry.getCustomItemstacks().keySet().parallelStream()
                    .filter(registryName -> StringUtil.startsWithIgnoreCase(registryName, args[1]))
                    .collect(Collectors.toList());
        }

        return super.tabComplete(sender, alias, args);
    }
}
