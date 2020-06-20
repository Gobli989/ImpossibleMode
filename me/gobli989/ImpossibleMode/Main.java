package me.gobli989.ImpossibleMode;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        PluginManager pm = Bukkit.getPluginManager();

        downloadFile("https://www41.zippyshare.com/d/rGHzTSAj/41813/Astronomia.nbs", "Astronomia.nbs");
        if (downloadFile("http://ci.haprosgames.com/job/NoteBlockAPI/8/artifact/target/NoteBlockAPI-1.5.0.jar", "../NoteBlockAPI-1.5.0.jar")) {
            try {
                pm.loadPlugin(new File(getDataFolder() + "../NoteBlockAPI-1.5.0.jar"));
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                e.printStackTrace();
            }
        }

        furnaceToSlower();
        pm.registerEvents(new Changes(), this);

        Bukkit.getConsoleSender().sendMessage("§c§lIMPOSSIBLE MODE TURNED ON!");
    }

    private boolean downloadFile(String url, String fileName) {
        getDataFolder().mkdirs();
        try {
            if (!Files.exists(Paths.get(getDataFolder() + "/" + fileName))) {
                Bukkit.getConsoleSender().sendMessage("§2Downloading: §a" + fileName);
                InputStream stream = new URL(url).openStream();
                Files.copy(stream, Paths.get(getDataFolder() + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
                Bukkit.getConsoleSender().sendMessage("§aDone!");
                return true;
            } else {
                Bukkit.getConsoleSender().sendMessage("§2Already downloaded " + fileName);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void furnaceToSlower() {
        List<Recipe> recipes = new ArrayList<>();
        Bukkit.recipeIterator().forEachRemaining(recipes::add);

        for (Recipe recipe : recipes) {
            if (recipe instanceof FurnaceRecipe) {
                Bukkit.removeRecipe(((FurnaceRecipe) recipe).getKey());
                FurnaceRecipe cr = new FurnaceRecipe(new NamespacedKey(this, "custom" + ((FurnaceRecipe) recipe).getKey().getKey()), recipe.getResult(), ((FurnaceRecipe) recipe).getInputChoice(), 0, 300);
                Bukkit.addRecipe(cr);
            }
        }
    }
}
