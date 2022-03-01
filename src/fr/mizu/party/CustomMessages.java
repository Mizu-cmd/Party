package fr.mizu.party;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomMessages {
    private static File mFile;
    private static YamlConfiguration mConfiguration;

    public CustomMessages(){

        mFile = new File(Main.getInstance().getDataFolder(), "messages.yml");

        if(!mFile.exists()){
            mFile.getParentFile().mkdir();
            Main.getInstance().saveResource("messages.yml", false);
        }

        mConfiguration = new YamlConfiguration();
        try {
            mConfiguration.load(mFile);
        } catch (IOException | InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void savemConfiguration(){
        try {
            mConfiguration.save(mFile);
            mConfiguration.load(mFile);
        } catch (IOException | InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getField(String field){
        return "§8§l❙ §b§lParty §8§l❘" + " §r" + mConfiguration.getString(field).replace("Â", "");
    }

    public static String getFieldWithoutPrefix(String field){
        return mConfiguration.getString(field).replace("Â", " ");
    }

    public static String getPrefix(){
        return "§8§l❙ §b§lParty §8§l❘";
    }

    /**
     * @return the mConfiguration
     */
    public static YamlConfiguration getmConfiguration() {
        return mConfiguration;
    }
    
}
