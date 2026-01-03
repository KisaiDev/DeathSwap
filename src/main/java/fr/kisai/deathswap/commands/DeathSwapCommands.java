package fr.kisai.deathswap.commands;

import fr.kisai.deathswap.Main;
import fr.kisai.deathswap.game.kits.Kit;
import fr.kisai.deathswap.game.kits.KitsManager;
import fr.kisai.deathswap.game.player.GPlayer;
import fr.kisai.deathswap.utils.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DeathSwapCommands implements CommandExecutor , TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (cmd.getName().equalsIgnoreCase("deathswap")) {
                if (args.length == 0){
                    player.sendMessage(ChatUtil.prefix("&c&lMerci de faire /deathswap <start/kits/stop>"));
                } else {
                    switch (args[0]) {
                        case "start": {

                            if (args.length < 3) {
                                player.sendMessage(ChatUtil.prefix("&cMerci de faire : /deathswap start <player1> <player2> [player3]"));
                                return true;
                            }

                            Player player1 = Bukkit.getPlayer(args[1]);
                            Player player2 = Bukkit.getPlayer(args[2]);
                            Player player3 = null;

                            if (args.length >= 4) {
                                player3 = Bukkit.getPlayer(args[3]);
                            }

                            if (player1 == null || player2 == null || (args.length >= 4 && player3 == null)) {
                                player.sendMessage(ChatUtil.prefix("&cUn des joueurs n'est pas en ligne."));
                                return true;
                            }

                            if (player1.equals(player2) || (player3 != null && (player1.equals(player3) || player2.equals(player3)))) {
                                player.sendMessage(ChatUtil.prefix("&cVous devez choisir des joueurs différents."));
                                return true;
                            }

                            if (player3 != null) {
                                Main.getGameManager().startThree(player1, player2, player3);
                            } else {
                                Main.getGameManager().startTwo(player1, player2);
                            }

                            break;
                        }


                        case "kits":
                            KitsManager kitsManager = Main.getInstance().getKitsManager();
                            if (args.length < 2) {
                                player.sendMessage(ChatUtil.prefix("&c&lVoici les kits disponibles :"));
                                player.sendMessage(" ");

                                for (Kit kit : kitsManager.getKits()) {

                                    String hoverText = "§f" + String.join("\n§7- ", kit.getDescription());

                                    BaseComponent[] component = new ComponentBuilder("§7- §f" + kit.getName())
                                            .event(new HoverEvent(
                                                    HoverEvent.Action.SHOW_TEXT,
                                                    new ComponentBuilder(hoverText).create()
                                            ))
                                            .create();

                                    player.spigot().sendMessage(component);
                                }
                                return true;
                            }

                            Kit kit = kitsManager.getKit(args[1]);

                            if (kit == null) {
                                player.sendMessage(ChatUtil.prefix("&cCe kit n'existe pas."));
                                return true;
                            }

                            GPlayer.get(player).setKits(kit);
                            player.sendMessage(ChatUtil.prefix("&aVous avez bien choisi le kit : &f" + kit.getName()));
                            return true;

                        case "stop":
                            Main.getGameManager().safeStopGame();
                            break;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("deathswap")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> subCommands = new java.util.ArrayList<>();
            subCommands.add("start");
            subCommands.add("kits");
            subCommands.add("stop");

            return filter(args[0], subCommands);
        }


        if (args[0].equalsIgnoreCase("start")) {

            // /deathswap start <player>
            if (args.length >= 2 && args.length <= 4) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> !alreadyUsed(name, args))
                        .filter(name -> name.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            }
        }


        if (args[0].equalsIgnoreCase("kits") && args.length == 2) {
            return Main.getInstance().getKitsManager().getKits().stream()
                    .map(Kit::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        }

        return Collections.emptyList();
    }


    private List<String> filter(String arg, List<String> values) {
        return values.stream()
                .filter(v -> v.toLowerCase().startsWith(arg.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());

    }

    private boolean alreadyUsed(String name, String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}