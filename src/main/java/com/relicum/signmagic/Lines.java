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

package com.relicum.signmagic;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: Lines.java Created: 19 December 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Lines {

    private List<String> lines;

    public Lines(List<String> theLines) {
        if (theLines.size() != 4)
            throw new IllegalArgumentException("The list of lines must have a length of 4");
        this.lines = new ArrayList<>(theLines.size());

        try {
            for (int i = 0; i < 4; i++) {
                this.lines.add(i, validate(theLines.get(i)));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    private String validate(String line) {

        if (line.toCharArray().length > 16) {

            throw new IllegalArgumentException("Number of character per line can not exceed 16");

        }
        if (line.toCharArray().length != 0)
            return ChatColor.translateAlternateColorCodes('&', line);
        else
            return " ";
    }

    public String[] getLines() {

        return lines.toArray(new String[lines.size()]);
    }
}
