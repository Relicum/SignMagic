package com.relicum.signmagic.Handlers;

import com.google.common.collect.ImmutableList;

/**
 * Identifier represents identifying a line on a sign to check if an action is required.
 * <p>To start it will mostly be to identify and check other plugins signs to see if we should strip the color when displaying the sign update text GUI.
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface Identifier {

    /**
     * Gets the line index that this identifier is for
     *
     * @return the sign line index
     */
    public String getId();

    /**
     * Is enabled.
     *
     * @return the boolean
     */
    public boolean isEnabled();

    /**
     * Enable identifier.
     */
    public void enableIdentifier();

    /**
     * Disable identifier.
     */
    public void disableIdentifier();

    /**
     * Add identifier pattern this is unique to this lines index number on the sign.
     *
     * @param regex the regex pattern used to check if any actions are required before sign is attempted to be edited.
     */
    public void addIdentifierPattern(String regex);

    /**
     * Update identifier pattern.
     *
     * @param regex the regex
     */
    public void updateIdentifierPattern(String regex);

    /**
     * Add new lookup, add the different identifying names that can appear on ths line.
     * <p>Eg for essentials it would be sell,buy,kit,warp ect. Only add anything if it appears on THIS line.
     *
     * @param lookup the lookup to add which will then be used to identify the sign.
     */
    public void addNewLookup(String lookup);

    /**
     * Remove lookup.
     *
     * @param lookup the lookup
     * @return the boolean
     */
    public boolean removeLookup(String lookup);

    /**
     * Gets an ImmutableList of identifiers, returns all the lookup identifiers in a list.
     *
     * @return the list of identifiers
     */
    public ImmutableList<String> getListOfIdentifiers();

    /**
     * Sets action type.
     *
     * @param actionType the action type
     */
    public void setActionType(ActionType actionType);

    /**
     * Gets action type.
     *
     * @return the action type
     */
    public ActionType getActionType();

}
