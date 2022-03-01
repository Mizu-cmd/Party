package fr.mizu.party.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mizu.party.CustomMessages;
import fr.mizu.party.Main;
import fr.mizu.party.Party;

public class CommandsListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(arg2.equalsIgnoreCase("pc")){
            if(!Party.hasParty(player)) return false;
            if (args.length >= 1){
            
                String msg = "";
                for (int i = 0; i < args.length ; i++){
                    msg += args[i]+" ";
                }
                Party.getParty(player).sendMessage(player, msg);
                return false;
            }
        }

        if (args.length < 1){
            player.sendMessage("§m                                §r §f§lParty §m                              ");
            player.sendMessage("§3/party create §8» §fCreate your own party");
            player.sendMessage("§3/party invite §7<player> §8» §fInvite a player to your party");
            player.sendMessage("§3/party chat §7<msg> §8» §fSend a message to everyone in the party");
            player.sendMessage("§3/party leave §8» §fLeave the party you are currently in");
            player.sendMessage("§3/party kick <player> §8» §fKick  a player from your party");
            player.sendMessage("§3/party list §8» §fList all members currently in your party");
            player.sendMessage("§3/party warp §8» §fTeleport the members of your party to your server");
            player.sendMessage("§3/party promote <name> §8» §fPromote a new player to leader");
            player.sendMessage("§m                                                                                ");
        }

        if (args.length == 2){
            if(args[0].equalsIgnoreCase("invite")){
                Player target = Bukkit.getPlayerExact(args[1]);

                if(target == null){
                    player.sendMessage(CustomMessages.getField("invalid-player"));
                    return false;
                }

                if (target.equals(player)){
                    player.sendMessage(CustomMessages.getField("invite-yourself"));
                    return false;
                }
                if (Party.getWaiting().containsKey(target)){
                    player.sendMessage(CustomMessages.getField("allready-invited"));
                    return false;
                }

                if (Party.hasParty(player)){
                    if (Party.getParty(player).getMembers().size() <= 24 && player.hasPermission("party.24")){
                        Party.getParty(player).invite(target, player);
                        player.sendMessage(CustomMessages.getField("invited").replace("{player}", target.getDisplayName()));
                        return false;
                    } else if (Party.getParty(player).getMembers().size() <= 12 && player.hasPermission("party.12")){
                        Party.getParty(player).invite(target, player);
                        player.sendMessage(CustomMessages.getField("invited").replace("{player}", target.getDisplayName()));
                        return false;
                    } else if (Party.getParty(player).getMembers().size() <= 6){
                        Party.getParty(player).invite(target, player);
                        player.sendMessage(CustomMessages.getField("invited").replace("{player}", target.getDisplayName()));
                        return false;
                    } else {
                        player.sendMessage(CustomMessages.getField("party-full"));
                        return false;
                    }
                } else {
                    Party party = new Party(player);
                    player.performCommand("party invite "+target.getDisplayName());
                    return false;
                }
            } else 

            if (args[0].equalsIgnoreCase("reach")){
                Player target = Bukkit.getPlayerExact(args[1]);
                if(!Party.hasParty(player) || !Party.getParty(player).getMembers().contains(target)){
                    player.sendMessage(CustomMessages.getField("invalid-player"));
                    return false;
                }
                if (Main.getInstance().getConfig().getStringList("allowed-worlds").contains(target.getWorld().getName())){
                    player.teleport(target.getWorld().getSpawnLocation());
                    player.sendMessage(CustomMessages.getField("teleported").replace("{player}", target.getDisplayName()));
                }
            } else

            if (args[0].equalsIgnoreCase("promote")){
                Player target = Bukkit.getPlayerExact(args[1]);
                if(!Party.hasParty(player) || !Party.getParty(player).getMembers().contains(target)){
                    player.sendMessage(CustomMessages.getField("invalid-player"));
                    return false;
                }
                if (Party.getParty(player).getOwner().equals(player)){
                    Party.getParty(player).setOwner(target);
                    player.sendMessage(CustomMessages.getField("promote").replace("{player}", target.getDisplayName()));
                    target.sendMessage(CustomMessages.getField("promoted"));
                }
            } else

            if (args[0].equalsIgnoreCase("kick")){
                Player target = Bukkit.getPlayerExact(args[1]);
                if(!Party.hasParty(player) || !Party.getParty(player).getMembers().contains(target)){
                    player.sendMessage(CustomMessages.getField("invalid-player"));
                    return false;
                }
                if (Party.getParty(player).getOwner().equals(player)){
                    Party.getParty(player).leave(target);
                    player.sendMessage(CustomMessages.getField("kick").replace("{player}", target.getDisplayName()));
                    target.sendMessage(CustomMessages.getField("kicked"));
                }
            }
        }

        if (args.length > 1){
            if (args[0].equalsIgnoreCase("chat")){
                if(!Party.hasParty(player)) return false;
                String msg = "";
                for (int i = 1; i < args.length ; i++){
                    msg += args[i]+" ";
                }
                Party.getParty(player).sendMessage(player, msg);

            }
        }

        if (args.length == 1){

            if (args[0].equalsIgnoreCase("create")){
                if (Party.hasParty(player)){
                    player.sendMessage(CustomMessages.getField("allready-in-party"));
                    return false;
                }

                new Party(player);
                player.sendMessage(CustomMessages.getField("party-created"));

            } else 

            if (args[0].equalsIgnoreCase("members")){
                if (!Party.hasParty(player)){
                    player.sendMessage(CustomMessages.getField("not-in-a-party"));
                    return false;
                } else {
                    player.sendMessage(CustomMessages.getPrefix()+" §eParty members");
                    player.sendMessage("§6Leader : §2"+Party.getParty(player).getOwner().getDisplayName());
                    player.sendMessage("§eMembers : ");
                for (Player p : Party.getParty(player).getMembers()){
                    if (!p.equals(Party.getParty(player).getOwner())){
                        player.sendMessage("§6- "+p.getDisplayName());
                    }
                }
            }
        } else 

        if (args[0].equalsIgnoreCase("accept")){
            if(!Party.getWaiting().containsKey(player)){
                player.sendMessage(CustomMessages.getField("no-request"));
                return false;
            }

            Player owner = Party.getWaiting().get(player).getOwner();
            Party.getParty(owner).join(player);
            player.sendMessage(CustomMessages.getField("party-joinded").replace("{leader}", owner.getDisplayName()));
            Party.getWaiting().remove(player);
            owner.sendMessage(CustomMessages.getField("player-joined").replace("{player}", player.getDisplayName()));
        } else

        if (args[0].equalsIgnoreCase("deny")){
            if(!Party.getWaiting().containsKey(player)){
                player.sendMessage(CustomMessages.getField("no-request"));
                return false;
            }

            player.sendMessage(CustomMessages.getField("party-denied").replace("{leader}", Party.getWaiting().get(player).getOwner().getDisplayName()));
            Party.getWaiting().remove(player);
        }

        if(args[0].equalsIgnoreCase("warp")){
            if (!Party.hasParty(player)){
                player.sendMessage(CustomMessages.getField("not-in-a-party"));
                return false;
            }
            if (Party.getParty(player).getOwner().equals(player)){
                for(Player member : Party.getParty(player).getMembers()){
                    if (!member.getWorld().equals(player.getWorld())){
                    member.teleport(player.getWorld().getSpawnLocation());
                    player.sendMessage(CustomMessages.getField("teleported").replace("{player}", player.getDisplayName()));
                    }
                }
            }
        } else

        if(args[0].equalsIgnoreCase("leave")){
            if (!Party.hasParty(player)){
                player.sendMessage(CustomMessages.getField("not-in-a-party"));
                return false;
            }
            Party.getParty(player).leave(player);
            player.sendMessage(CustomMessages.getField("leaved"));
        }

    }

        return false;
    }
    
}
