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
import org.bukkit.entity.Player;

public class DeathSwapCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (cmd.getName().equalsIgnoreCase("deathswap")) {
                if (args.length == 0){
                    player.sendMessage(ChatUtil.prefix("&c&lMerci de faire /deathswap <start/kits/stop>"));
                } else {
                    switch (args[0]) {
                        case "start":

                            if (args.length < 3) {
                                player.sendMessage(ChatUtil.prefix("&cMerci de faire : /deathswap start <player1> <player2>"));
                                return true;
                            }

                            Player player1 = Bukkit.getPlayer(args[1]);
                            Player player2 = Bukkit.getPlayer(args[2]);

                            if (player1 == null || player2 == null) {
                                player.sendMessage(ChatUtil.prefix("&cUn des joueurs n'est pas en ligne."));
                                return true;
                            }

                            if (player1.equals(player2)) {
                                player.sendMessage(ChatUtil.prefix("&cVous devez choisir deux joueurs différents."));
                                return true;
                            }

                            Main.getGameManager().startTwo(player1, player2);
                            break;
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

                       /* case "kits":
                            if (args.length < 2) {
                                player.sendMessage(ChatUtil.prefix("&c&lVoici les kits disponibles :"));
                                player.sendMessage(" ");

                                for (KitsRegisters kit : KitsRegisters.values()) {
                                    BaseComponent[] kits = new ComponentBuilder("§7- §f"+ kit.getName()).event(new HoverEvent(
                                            HoverEvent.Action.SHOW_TEXT,
                                            new ComponentBuilder("§f" + Arrays.toString(kit.getKitsCreator().getDescription())).create()
                                    )).create();
                                    player.spigot().sendMessage(kits);
                                    //player.sendMessage("§7- §f" + kit.getName());
                                }
                                return true;
                            }

                            KitsRegisters kit = KitsRegisters.getByName(args[1]);

                            if (kit == null) {
                                player.sendMessage(ChatUtil.prefix("&cCe kit n'existe pas."));
                                return true;
                            }

                            GPlayer.get(player).setKits(kit);
                            player.sendMessage(ChatUtil.prefix("&aVous avez bien choisi le kit : &f" + kit.getName()));
                            break;*/
                        case "stop":
                            Main.getGameManager().safeStopGame();
                            break;
                    }
                }

            }
        }
        return false;
    }
}