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

package com.relicum.signmagic.Handlers;

import com.relicum.signmagic.SignMagic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Name: IdHandler.java Created: 19 December 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class IdHandler implements Listener {

    private Method openSign;
    private String bukkitVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
    private List<String> worldBlackList;
    private Pattern pattern = Pattern.compile("(\\[(?<res>[a-zA-Z_]+)\\])");
    private final SignMagic plugin;

    public IdHandler(SignMagic plugin) {
        this.plugin = plugin;
    }

    private void loadSignHandlers() {


    }

    private void loadPatterns() {


    }

    @SuppressWarnings("ConstantConditions")
    public String identify(String l) {

        final Matcher matcher = pattern.matcher(l);
        //return Pattern.matches("(\\[(?<res>[a-zA-Z_]+)\\])",l);

        final StringBuffer sb = new StringBuffer();

        while (matcher.find()) {

            matcher.appendReplacement(sb, callBack(matcher.group("res")));
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    @SuppressWarnings("ConstantConditions")
    private String callBack(String result) {

        return ChatColor.stripColor(result);
    }


    private void switchColor(Sign bSign) {
        for (int i = 0; i < 4; i++) {
            bSign.setLine(i, bSign.getLine(i).replace("§", "&"));
        }
    }

    private void stripColor(Sign bSign) {
        for (int i = 0; i < 4; i++) {
            bSign.setLine(i, ChatColor.stripColor(bSign.getLine(i)));
        }
    }


    @EventHandler
    public void signPlace(SignChangeEvent event) {

        if (event.getPlayer().hasPermission("signmagic.colorsign")) {

            for (int i = 0; i < 4; i++) {
                event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLines()[i]));
            }
        }
    }


    public Object getWorld(World world) {

        try {

            return getCraftClass("CraftWorld").getMethod("getHandle").invoke(world);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public Class getNmsClass(String className) {

        try {

            return Class.forName("net.minecraft.server." + bukkitVersion + "." + className);
        } catch (Exception e) {

            // e.printStackTrace();
        }
        return null;
    }

    public Class getCraftClass(String className) {

        try {

            return Class.forName("org.bukkit.craftbukkit." + bukkitVersion + "." + className);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }


    //Only works on class like CraftPlayer or CraftEntity that define the handle method
    public Object getHandle(Object object) {

        try {

            Method getHand = object.getClass().getDeclaredMethod("getHandle");
            return getHand.invoke(object, new Object[0]);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void openTheSign(Player player, Location location) {

        Class<?> blockPosition = getNmsClass("BlockPosition");
        Class<?> tileEntitySign = getNmsClass("TileEntitySign");

        try {


            Object bp = blockPosition.getConstructor(int.class, int.class, int.class).newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            Object sign = getNmsClass("World").getMethod("getTileEntity", blockPosition).invoke(getWorld(player.getWorld()), bp);

            sign.getClass().getField("isEditable").set(sign, true);


            openSign = getHandle(player).getClass().getMethod("openSign", tileEntitySign);
            openSign.invoke(getHandle(player), sign);


        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }


    }
}
