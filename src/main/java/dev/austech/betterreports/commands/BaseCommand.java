/*
 * BetterReports - BaseCommand.java
 *
 * Github: https://github.com/AusTechDev/
 * Spigot Profile: https://www.spigotmc.org/members/austech.919057/
 * Discord Server: https://austech.dev/to/support
 *
 * MIT License
 *
 * Copyright (c) 2022 Timmy109.
 * Copyright (c) 2022 Contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dev.austech.betterreports.commands;

import dev.austech.betterreports.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class BaseCommand {

    static boolean base(CommandSender sender, String[] args) {

        if (!(sender.hasPermission("betterreports.use"))) {
            sender.sendMessage(Common.color(Common.getConfig().getString("no-permission-message")));
            return true;
        }

        if (sender.hasPermission("betterreports.admin") && args.length == 0) {
            String[] adminHelp = Common.getConfig().getString("admin-help-message").split("\\n");
            Arrays.stream(adminHelp).forEach(msg -> sender.sendMessage(Common.color(msg)));
            return true;
        } else if (args.length != 0) {
            return false;
        }

        String[] playerHelp = Common.getConfig().getString("player-help-message").split("\\n");
        Arrays.stream(playerHelp).forEach(msg -> sender.sendMessage(Common.color(msg)));

        return true;
    }
}
