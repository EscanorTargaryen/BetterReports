/*
 * BetterReports - ReportPlayerCommand.java
 *
 * Copyright (c) 2022 AusTech Development
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.austech.betterreports.commands;

import com.viaversion.viaversion.api.Via;
import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.model.report.menu.creation.ConfirmReportMenu;
import dev.austech.betterreports.model.report.menu.creation.SelectPlayerMenu;
import dev.austech.betterreports.model.report.menu.creation.reason.PlayerReportPagedReasonMenu;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.discord.DiscordManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static dev.austech.betterreports.model.report.ReportManager.checkCooldown;

public class ReportPlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("betterreports.use.player")) {
            MainConfig.Values.LANG_NO_PERMISSION.send(sender);
            return true;
        }

        if (!MainConfig.Values.PLAYER_REPORT_ENABLED.getBoolean()) {
            MainConfig.Values.LANG_UNKNOWN_COMMAND.send(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            final Player target = Bukkit.getPlayer(args[0]);
            // MainConfig.Values.LANG_PLAYER_ONLY.send(sender);

            if (target != null) {

                /*
                - %player_first_join_date%
- %viaversion_player_protocol_version%
- %player_ping%
- %player_ip%
- %player_uuid%
                 */

                String uuid = target.getUniqueId().toString();
                String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String ip = target.getAddress().getHostString();
                String version = com.viaversion.viaversion.api.protocol.version.ProtocolVersion.getProtocol(Via.getAPI().getPlayerVersion(target)).getName();


                final Report report = Report.builder().type(Report.Type.PLAYER).creator(target).reason(String.join(" ", Arrays.asList(args).subList(1, args.length))
                        .replace("%player_first_join_date%",data)
                        .replace("%viaversion_player_protocol_version%",version)
                        .replace("%player_ip%",ip)
                        .replace("%player_uuid%",uuid)
                ).target(target).build();
                DiscordManager.getInstance().sendUnknownReport(report);
            }

            return true;
        }

        if (checkCooldown((Player) sender, Report.Type.PLAYER)) return true;

        if (args.length == 0) {
            if (MainConfig.Values.PLAYER_REPORT_MENUS_SELECT_PLAYER.getBoolean())
                new SelectPlayerMenu().open((Player) sender);
            else
                MainConfig.Values.LANG_USAGE_REPORT_PLAYER.sendUsage(sender);

            return true;
        }

        if (args.length == 1) {
            final Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (MainConfig.Values.PLAYER_REPORT_MENUS_SELECT_REASON.getBoolean())
                    new PlayerReportPagedReasonMenu(((Player) sender), target).open(((Player) sender));
                else
                    MainConfig.Values.LANG_USAGE_REPORT_PLAYER.sendUsage(sender);
            } else {
                MainConfig.Values.LANG_PLAYER_NOT_FOUND.send(sender);
            }

            return true;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            MainConfig.Values.LANG_PLAYER_NOT_FOUND.send(sender);
            return true;
        }

        final Report report = Report.builder().type(Report.Type.PLAYER).creator(((Player) sender)).reason(String.join(" ", Arrays.asList(args).subList(1, args.length))).target(target).build();

        if (MainConfig.Values.PLAYER_REPORT_MENUS_CONFIRM_REPORT.getBoolean())
            new ConfirmReportMenu(((Player) sender), report).open(((Player) sender));
        else
            report.save();

        return true;
    }

}
