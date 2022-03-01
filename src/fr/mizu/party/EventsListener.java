package fr.mizu.party;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if (Party.hasParty(player)){
            if (Party.getParty(player).getOwner().equals(player)){
                Player member = Party.getParty(player).getMembers().get(new Random().nextInt(Party.getParty(player).getMembers().size()));
                Party.getParty(player).setOwner(member);
                member.sendMessage(CustomMessages.getField("promoted"));
            }
            Party.getParty(player).leave(player);
        }
    }
}
