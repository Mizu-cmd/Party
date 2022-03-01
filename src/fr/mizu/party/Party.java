package fr.mizu.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Party {
    private static List<Party> parties = new ArrayList<>();
    private static HashMap<Player, Party> waiting = new HashMap<>();

    private List<Player> members = new ArrayList<>();

    private Player owner;

    public Party(Player owner){
        this.owner = owner;
        members.add(owner);
        parties.add(this);
    }

    public void join(Player player){
        members.add(player);
    }

    public void leave(Player player){
        members.remove(player);
    }

    @SuppressWarnings("deprecation")
    public void invite(Player target, Player from){
        target.sendMessage("§8⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
        target.sendMessage("");
        target.sendMessage("§9"+from.getDisplayName()+" §bwants you to join their party");
        target.sendMessage("");
        TextComponent deny = new TextComponent(CustomMessages.getFieldWithoutPrefix("deny"));
        TextComponent accept = new TextComponent(CustomMessages.getFieldWithoutPrefix("accept")+" ");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny"));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));
        accept.addExtra(deny);
        target.spigot().sendMessage(accept);
        target.sendMessage("");
        target.sendMessage("§8⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
        waiting.put(target, this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {

            @Override
            public void run() {
                if(!waiting.containsKey(target)) return;
                waiting.remove(target);
                target.sendMessage(CustomMessages.getField("invitation-expired"));
            }
            
        }, 60 * 20);

    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void sendMessage(Player sender, String message){
        for (Player p : members){
            p.sendMessage(CustomMessages.getPrefix()+" §r"+sender.getDisplayName()+" §8» §e"+message);
        }
    }

    public static boolean hasParty(Player player){
        for(Party p : parties){
            if(p.getMembers().contains(player)) return true;
            else return false;
        }
        return false;
    }

    public static Party getParty(Player player){
        for(Party p : parties){
            if(p.getMembers().contains(player)){
                return p;
            };
        }
        return null;
    }


    /**
     * @return the members
     */
    public List<Player> getMembers() {
        return members;
    }

    /**
     * @return the waiting
     */
    public static HashMap<Player, Party> getWaiting() {
        return waiting;
    }

    /**
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }
}
