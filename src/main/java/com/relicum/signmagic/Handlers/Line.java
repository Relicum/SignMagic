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

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Name: Line.java Created: 19 December 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@SerializableAs("Line")
public class Line implements Identifier, ConfigurationSerializable {

    private String id;
    private boolean enable;
    private String regex;
    private List<String> lookUps;
    private String actionType;


    protected Line(String actionType, boolean enable, String id, List<String> lookUps, String regex) {
        this.actionType = actionType;
        this.enable = enable;
        this.id = id;
        this.lookUps = lookUps;
        this.regex = regex;
    }

    public Line(LINE_INDEX id) {
        this.id = id.name();
        this.lookUps = new ArrayList<>();
        this.enable = false;
        this.actionType = ActionType.LEAVE.name();
        this.regex = " ";

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return enable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableIdentifier() {
        this.enable = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableIdentifier() {
        this.enable = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIdentifierPattern(String regex) {
        Validate.notNull(regex);
        this.regex = regex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateIdentifierPattern(String regex) {
        Validate.notNull(regex);
        this.regex = regex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifierPattern() {
        return this.regex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewLookup(String lookup) {
        Validate.notNull(lookup);
        this.lookUps.add(lookup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeLookup(String lookup) {
        return this.lookUps.remove(lookup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableList<String> getListOfIdentifiers() {
        return ImmutableList.copyOf(lookUps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActionType(ActionType actionType) {
        this.actionType = actionType.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionType getActionType() {
        return ActionType.valueOf(actionType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;

        Line line = (Line) o;

        if (enable != line.enable) return false;
        if (!actionType.equals(line.actionType)) return false;
        if (!id.equals(line.id)) return false;
        if (!regex.equals(line.regex)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (enable ? 1 : 0);
        result = 31 * result + regex.hashCode();
        result = 31 * result + actionType.hashCode();
        return result;
    }

    public static Line deserialize(Map<String, Object> map) {

        Object objId = map.get("id"), objEnable = map.get("enable"),
                objRegex = map.get("regex"), objLookups = map.get("lookups"),
                objAction = map.get("action");

        if (objId == null || objEnable == null || objRegex == null || objLookups == null || objAction == null)
            throw new RuntimeException("Error while deserializing object something was null");

        return new Line((String) objAction, (Boolean) objEnable, (String) objId, (List<String>) objLookups, (String) objRegex);


    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("id", id);
        map.put("enable", enable);
        map.put("regex", regex);
        map.put("lookups", lookUps);
        map.put("action", actionType);

        return map;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("actionType", actionType)
                .append("id", id)
                .append("enable", enable)
                .append("regex", regex)
                .append("lookUps", lookUps)
                .append("enabled", isEnabled())
                .append("listOfIdentifiers", getListOfIdentifiers())
                .append("rialize", serialize())
                .toString();
    }
}
