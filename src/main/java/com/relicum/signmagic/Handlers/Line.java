package com.relicum.signmagic.Handlers;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
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
}
