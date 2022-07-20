package ru.brenlike.autorestart25;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Handler implements Listener {
    private final Main plugin;

    public Handler(Main main) {
        this.plugin = main;
    }

    @EventHandler
    public void playerLogin(AsyncPlayerPreLoginEvent e) {
        if (plugin.restarting) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, plugin.getConfig().getString("kick-restart-event")
                    .replace('&', '\u00a7'));
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        if (plugin.restarting) {
            e.getPlayer().saveData();
        }
    }
}
