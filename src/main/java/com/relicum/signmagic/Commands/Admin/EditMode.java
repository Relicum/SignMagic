/*
 * SignMagic is a development API for Minecraft Sign Editing, developed
 * originally by libraryaddict now by Relicum
 * Copyright (C) 2014.  Chris Lutte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.relicum.signmagic.Commands.Admin;

import com.google.common.collect.ImmutableList;
import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Utils.Msg;
import com.relicum.signmagic.Commands.MagicCommand;
import com.relicum.signmagic.SignMagic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

/**
 * Name: EditMode.java Created: 20 December 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Command(aliases = {"editmode"}, perm = "signmagic.admin.mode", desc = "Toggle on and off edit mode", isSub = true,
        parent = "sma", min = 0, max = 1, useTab = true)
public class EditMode extends MagicCommand {

    private String metaPrefix = "SMAG-";

    public EditMode(Plugin plugin, Msg msg) {
        super(plugin, msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            getMsg().sendMessage(player, "This will display your current edit status");
            return true;
        }
        if (player.hasMetadata(metaPrefix)) {

            //CHECK TO SEE IF 5 MINS IS UP
            if (player.getMetadata(metaPrefix).get(0).asLong() < System.currentTimeMillis()) {

                getMsg().sendMessage(player, "Your 5 mins in edit mode expired, toggle on to edit");

                return true;

            }
            //IF IT EXPIRED ADD 5 MORE MINS
            if (args[1].equalsIgnoreCase("on")) {
                player.setMetadata(metaPrefix, new FixedMetadataValue(SignMagic.getInstance(), System.currentTimeMillis() * 300000));

                getMsg().sendMessage(player, "You have toggled on Sign edit mode");
                getMsg().sendMessage(player, "Your session will last 5 minutes");

                return true;
            }

            if (args[1].equalsIgnoreCase("off")) {


            }


            player.removeMetadata(metaPrefix, SignMagic.getInstance());
            getMsg().sendMessage(player, "You have the meta and perms good to go");
            return true;
        }

        player.setMetadata(metaPrefix, new FixedMetadataValue(SignMagic.getInstance(), System.currentTimeMillis() + 300000));


        return false;
    }

    @Override
    public List<String> tabComp(int length) {

        if (length == 2) {
            return ImmutableList.of("on", "off");
        }

        return Collections.emptyList();
    }
}
