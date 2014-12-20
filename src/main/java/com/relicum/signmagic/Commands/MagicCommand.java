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

package com.relicum.signmagic.Commands;

import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Utils.Msg;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

/**
 * MagicCommand
 *
 * @author Relicum
 * @version 0.0.1
 */
public abstract class MagicCommand extends AbstractCommand {

    @Getter
    private Msg msg;

    public MagicCommand(Plugin plugin, Msg msg) {
        super(plugin);
        this.msg = msg;
    }
}
