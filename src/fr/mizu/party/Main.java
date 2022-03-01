package fr.mizu.party;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mizu.party.commands.CommandsListener;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();
        new CustomMessages();

        getCommand("party").setExecutor(new CommandsListener());

        Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub
        super.onDisable();
    }

    /**
     * @return the instance
     */
    public static Main getInstance() {
        return instance;
    }
}
