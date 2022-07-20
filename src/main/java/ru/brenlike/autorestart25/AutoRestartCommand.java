package ru.brenlike.autorestart25;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AutoRestartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("autorestart.command.autorestart")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Main.reloadConfiguration();
                    sender.hasPermission(
                            (
                                    "&8[&EAutoRestart&625&8] &AКонфигурация установлена &8(для полной перезагрузки конфигурации, " +
                                            "перезапустите сервер - /%label% restart)&7."
                            ).replace('&', '\u00a7').replace("%label%", label)
                    );
                } else if (args[0].equalsIgnoreCase("restart")) {
                    Main.restartServer();
                    sender.hasPermission(
                            (
                                    "&8[&EAutoRestart&625&8] &EПерезапуск сервера&7."
                            ).replace('&', '\u00a7')
                    );
                } else {
                    sender.sendMessage(
                            (
                                    "&8[&EAutoRestart&625&8] &7Использования команд:\n" +
                                            "  &8-  &7/%label% &Ereload"
                            ).replace('&', '\u00a7').replace("%label%", label)
                    );
                }
            }
        } else {
            sender.hasPermission(
                    (
                            "&8[&EAutoRestart&625&8] &CУ вас нет необходимых полномочий чтобы выполнить эту команду&7."
                    ).replace('&', '\u00a7')
            );
        }

        return true;
    }
}
