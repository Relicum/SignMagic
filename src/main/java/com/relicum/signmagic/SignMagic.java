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

import com.google.common.collect.ImmutableList;
import com.relicum.ipsum.Commands.CommandRegister;
import com.relicum.signmagic.Handlers.LINE_INDEX;
import com.relicum.signmagic.Handlers.Line;
import com.relicum.signmagic.Handlers.SignIdentity;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SignMagic is a direct Fork from LibraryAddict the credits go to him,
 * <p>I have just updated the reflection for 1.8, but have taken over the maintenance.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class SignMagic extends JavaPlugin implements Listener {

    public static SignMagic instance;
    private Method openSign;
    private String bukkitVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
    private List<String> worldBlackList;
    private Pattern pattern;
    private boolean sneak;
    private boolean creativeRequired;
    private boolean worldLimited;
    private boolean useIdentifier;
    private SignIdentity signIdentity;
    @Getter
    private String metaId = "SMAG-";


    public void onEnable() {
        org.bukkit.configuration.serialization.ConfigurationSerialization.registerClass(Line.class);
        org.bukkit.configuration.serialization.ConfigurationSerialization.registerClass(SignIdentity.class);

        instance = this;

        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);

        //FIRST LOAD
        if (!getDataFolder().exists()) {

            getLogger().info("Initialising setting for first time load");
            initialSetup();
            getLogger().info("Initialising complete, it is advisable to restart your server");
        } else {

            ConfigurationSection section = getConfig().getConfigurationSection("signmagic");
            sneak = section.getBoolean("sneakClick");
            creativeRequired = section.getBoolean("creativeOnly");
            worldLimited = section.getBoolean("perWorld");
            useIdentifier = section.getBoolean("useIdentifier");

        }


        //LOAD RESTRICTED WORLDS
        if (worldLimited) {
            worldBlackList = ImmutableList.copyOf(getConfig().getStringList("blacklist"));
        }

        //LOAD IDENTIFIER SYSTEM
        if (!useIdentifier) {
            signIdentity = (SignIdentity) getConfig().get("signmagic.identify.essentials");
            this.pattern = Pattern.compile(signIdentity.getIdentifierPatten(LINE_INDEX.ZERO));
        }

        //REGISTER PREFIX COMMANDS
        CommandRegister cmd = new CommandRegister(this);

        Matcher matcher = pattern.matcher("[kit]");

        System.out.println(matcher.toString());
        if (matcher.matches()) {

            System.out.println("Yes it matches");
        } else
            System.out.println("Booooooo no match");

        try {
            for (Method method : getNmsClass("EntityPlayer").getMethods()) {
                if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getSimpleName().equals("TileEntitySign")) {
                    openSign = method;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /*
        SignIdentity signIdentity = (SignIdentity) getConfig().get("test.ESSENTIALS");
        signIdentity.setIdentifier(LINE_INDEX.ZERO);
        signIdentity.addIdentity(LINE_INDEX.ZERO,"kit");
        signIdentity.addIdentity(LINE_INDEX.ZERO,"buy");
        signIdentity.addIdentity(LINE_INDEX.ZERO,"sell");
        signIdentity.enableIdentifier(LINE_INDEX.ZERO);

        signIdentity.setIdentifier(LINE_INDEX.FIRST);
        signIdentity.addIdentity(LINE_INDEX.FIRST, "Zombie");
        signIdentity.addIdentity(LINE_INDEX.FIRST, "SG");
        // getConfig().set("test." + signIdentity.getIdentity(),signIdentity);

        // Line line = (Line) getConfig().get("test.line");
       line.enableIdentifier();
        line.addIdentifierPattern("(\\[(?<res>[a-zA-Z_]+)\\])");
        line.setActionType(ActionType.LEAVE);
        line.addNewLookup("kit");
        line.addNewLookup("buy");
        line.addNewLookup("sell");*/

        // List<String> tmp = line.getListOfIdentifiers();
        //System.out.println(tmp.toString());

        // getConfig().set("test.line",line);

        //  saveConfig();

    }

    public void onDisable() {
        saveConfig();
    }

    public static SignMagic getInstance() {
        return instance;
    }

    private void initialSetup() {

        ConfigurationSection section = getConfig().createSection("signmagic");
        section.addDefault("sneakClick", false);
        section.addDefault("creativeOnly", false);
        section.addDefault("perWorld", false);
        section.addDefault("useIdentifier", false);
        ConfigurationSection identify = section.createSection("identify");
        identify.addDefault("enable", false);
        SignIdentity si = new SignIdentity("essentials");
        si.setIdentifier(LINE_INDEX.ZERO);
        si.setIdentifierPatten(LINE_INDEX.ZERO, "(\\[(?<res>[a-zA-Z_]+)\\])");
        si.addIdentity(LINE_INDEX.ZERO, "kit");
        si.addIdentity(LINE_INDEX.ZERO, "buy");
        si.addIdentity(LINE_INDEX.ZERO, "sell");
        si.setIdentifier(LINE_INDEX.FIRST);
        si.setIdentifier(LINE_INDEX.SECOND);
        si.setIdentifier(LINE_INDEX.THIRD);
        si.disableAll();
        identify.addDefault("essentials", si);
        saveConfig();
        reloadConfig();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent event) {

        if (getConfig().getBoolean("blacklistEnabled")) {
            if (worldBlackList.contains(event.getPlayer().getWorld().getName()))
                return;

        }

        if (getConfig().getBoolean("creativeOnly") && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;

        if (!event.isCancelled() && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && (event.getPlayer().isSneaking() == getConfig().getBoolean("sneakClick"))
                && event.getClickedBlock().getState() instanceof Sign) {

            if (event.getPlayer().hasPermission("signmagic.editsign")) {
                event.setCancelled(true);

                if (event.getPlayer().hasPermission("signmagic.colorsign")) {
                    Sign bSign = (Sign) event.getClickedBlock().getState();

                    List<String> lss = Arrays.asList(bSign.getLines());

                    boolean res = false;

                    if (lss.get(0).length() != 0) {

                        res = identify(lss.get(0));

                    }
                    if (res) {
                        stripColor(bSign);
                        System.out.println("We have a match");
                    } else {
                        switchColor(bSign);
                        System.out.println("No match");
                    }

                    bSign.update();
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                    public void run() {
                        try {

                            openTheSign(event.getPlayer(), event.getClickedBlock().getLocation());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, 2);
            }
        }
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

    @SuppressWarnings("ConstantConditions")
    public boolean identify(String l) {

        final Matcher matcher = pattern.matcher(l);
        return matcher.find();
/*        final StringBuffer sb =new StringBuffer();

        while (matcher.find()){

            matcher.appendReplacement(sb,callBack(matcher.group("res")));
        }

        matcher.appendTail(sb);

        return sb.toString();*/
    }

    @SuppressWarnings("ConstantConditions")
    private String callBack(String result) {

        return ChatColor.stripColor(result);
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
