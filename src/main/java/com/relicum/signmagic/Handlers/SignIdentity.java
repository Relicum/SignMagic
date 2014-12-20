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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SignIdentity
 *
 * @author Relicum
 * @version 0.0.1
 */
@SerializableAs("SignIdentity")
public class SignIdentity implements Identity, ConfigurationSerializable {

    private String signId;
    private boolean enabled;
    private Map<String, Line> lineMap;
    private boolean firstTime = true;


    protected SignIdentity(boolean enabled, String signId, Map<String, Line> lineMap, boolean firstTime) {
        this.enabled = enabled;
        this.signId = signId;
        this.lineMap = lineMap;
        this.firstTime = firstTime;
    }

    public SignIdentity(String signId) {
        this.signId = signId;
        this.lineMap = new HashMap<>(4);

        if (!firstTime) {
            for (LINE_INDEX index : LINE_INDEX.values()) {
                lineMap.put(index.name(), new Line(index));
            }
        }

    }

    public static SignIdentity deserialize(Map<String, Object> map) {

        Object objSignId = map.get("signId"), objEnabled = map.get("enabled"), objLine = map.get("lineMap"), objFirst = map.get("firstTime");

        if (objSignId == null || objEnabled == null || objFirst == null || objLine == null) {
            throw new RuntimeException("Error while deserializing object something was null");
        }

        return new SignIdentity((Boolean) objEnabled, (String) objSignId, (Map<String, Line>) objLine, (Boolean) objFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentity() {
        return signId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled() {
        this.enabled = true;
    }

    @Override
    public void setDisabled() {
        this.enabled = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Line setIdentifier(LINE_INDEX index) {
        if (!this.lineMap.containsKey(index.name())) {

            return this.lineMap.put(index.name(), new Line(index));
        } else
            return this.lineMap.get(index.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIdentifierPatten(LINE_INDEX index, String regex) {
        this.lineMap.get(index.name()).addIdentifierPattern(regex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifierPatten(LINE_INDEX index) {
        return this.lineMap.get(index.name()).getIdentifierPattern();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIdentifier(LINE_INDEX index) {
        this.lineMap.remove(index.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableIdentifier(LINE_INDEX index) {
        this.lineMap.get(index.name()).enableIdentifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableIdentifier(LINE_INDEX index) {
        this.lineMap.get(index.name()).disableIdentifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableAll() {
        for (Iterator<Line> iterator = this.lineMap.values().iterator(); iterator.hasNext(); ) {

            iterator.next().enableIdentifier();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableAll() {
        for (Iterator<Line> iterator = this.lineMap.values().iterator(); iterator.hasNext(); ) {

            iterator.next().disableIdentifier();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIdentity(LINE_INDEX index, String lookup) {
        this.lineMap.get(index.name()).addNewLookup(lookup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIdentity(LINE_INDEX index) {
        this.lineMap.remove(index.name());
        this.lineMap.put(index.name(), new Line(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean registerIdentity() {
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("signId", signId);
        map.put("enabled", enabled);
        map.put("lineMap", lineMap);
        map.put("firstTime", false);

        return map;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignIdentity)) return false;

        SignIdentity that = (SignIdentity) o;

        if (enabled != that.enabled) return false;
        if (!lineMap.equals(that.lineMap)) return false;
        if (!signId.equals(that.signId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = signId.hashCode();
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + lineMap.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("enabled", enabled)
                .append("signId", signId)
                .append("lineMap", lineMap)
                .append("firstTime", firstTime)
                .toString();
    }
}
