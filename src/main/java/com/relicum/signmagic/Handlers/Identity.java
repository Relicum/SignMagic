package com.relicum.signmagic.Handlers;

/**
 * Identity is an represents an individual plugin that you need to preform actions on its signs before editing the sign.
 */
public interface Identity {

    /**
     * Gets identity this needs to be unique and human understandable, use the plugins name.
     *
     * @return the identity
     */
    public String getIdentity();

    /**
     * Is enabled.
     *
     * @return true if SignMagic needs to preform pre edit checks on another plugins signs.
     */
    public boolean isEnabled();

    /**
     * Sets identifier for a particular line on the sign that is used to identify its owner (The plugin it belongs to)
     *
     * @param index      the line index on the sign. Remember min is zero and three is max. {@link com.relicum.signmagic.Handlers.LINE_INDEX}
     * @param identifier the identifier
     */
    public void setIdentifier(LINE_INDEX index, Identifier identifier);

    /**
     * Remove identifier.
     *
     * @param index the index
     */
    public void removeIdentifier(LINE_INDEX index);

    /**
     * Enable identifier.
     *
     * @param index the index
     */
    public void enableIdentifier(LINE_INDEX index);

    /**
     * Disable identifier.
     *
     * @param index the index
     */
    public void disableIdentifier(LINE_INDEX index);

    /**
     * Enable all identifiers for this plugin
     */
    public void enableAll();

    /**
     * Disable all identifiers for this plugin
     */
    public void disableAll();

    /**
     * Add identity.
     *
     * @param index  the line index it is found on
     * @param lookup the value to search for eg kit, buy, sell etc
     */
    public void addIdentity(LINE_INDEX index, String lookup);

    /**
     * Remove identity.
     *
     * @param index the index
     */
    public void removeIdentity(LINE_INDEX index);

    /**
     * Register identity.
     *
     * @return true if successfully registered with handler.
     */
    public boolean registerIdentity();


}
