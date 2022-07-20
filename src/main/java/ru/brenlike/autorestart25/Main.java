package ru.brenlike.autorestart25;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Date;

public final class Main extends JavaPlugin implements Listener {
    public long timestamp;
    private int loop;
    public boolean restarting;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        timestamp = 0L;
        restarting = false;
        initConfig();

        loop = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (Bukkit.getOnlinePlayers().size() >= getConfig().getInt("online")) {
                if (timestamp == 0L) {
                    Date now = new Date(System.currentTimeMillis());
                    Date after = new Date(System.currentTimeMillis());
                    after.setMinutes(now.getMinutes() + getConfig().getInt("time"));
                    timestamp = after.getTime();
                }
                if (timestamp >= System.currentTimeMillis()) {
                    restartServer();
                }
            } else {
                timestamp = 0L;
            }
        }, 1, 20);

    }

    @Override
    public void onDisable() {

    }

    private static Main getInstance() {
        return instance;
    }

    private void initConfig() {
        File config = new File(getDataFolder(), "/config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    public static void reloadConfiguration() {
        File config = new File(Main.getInstance().getDataFolder(), "/config.yml");
        if (!config.exists()) {
            Main.getInstance().getConfig().options().copyDefaults(true);
            Main.getInstance().saveDefaultConfig();
        } else {
            Main.getInstance().reloadConfig();
        }
    }

    public static void restartServer() {
        Main.getInstance().timestamp = 0L;
        Main.getInstance().restarting = true;

        Bukkit.getScheduler().cancelTask(Main.getInstance().loop);
        for (Player p:
             Bukkit.getOnlinePlayers()) {
            p.sendMessage(Main.getInstance().getConfig().getString("restart-coming-message")
                    .replace('&', '\u00a7'));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            for (Player p:
                    Bukkit.getOnlinePlayers()) {
                p.kickPlayer(Main.getInstance().getConfig().getString("kick-restart-event")
                        .replace('&', '\u00a7'));
            }
            for (World w:
                 Bukkit.getWorlds()) {
                w.save();
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                Bukkit.spigot().restart();
            }, 20 * 3);
        }, 20 * 6);
    }

}
