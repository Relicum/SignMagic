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
