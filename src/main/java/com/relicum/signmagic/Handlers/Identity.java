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
     * Sets enabled.
     */
    public void setEnabled();

    /**
     * Sets disabled.
     */
    public void setDisabled();

    /**
     * Sets identifier for a particular line on the sign that is used to identify its owner (The plugin it belongs to)
     *
     * @param index the line index on the sign. Remember min is zero and three is max. {@link com.relicum.signmagic.Handlers.LINE_INDEX}
     * @return the identifier the identifier {@link com.relicum.signmagic.Handlers.Line}
     */
    public Line setIdentifier(LINE_INDEX index);

    /**
     * Sets Identifier regex pattern to use when searching a line.
     * <p>Each line of the sign can have a different pattern
     *
     * @param index the line index it is found on
     * @param regex the regex used during searching.
     */
    public void setIdentifierPatten(LINE_INDEX index, String regex);

    /**
     * Gets identifier patten.
     *
     * @param index the index
     * @return the identifier patten
     */
    public String getIdentifierPatten(LINE_INDEX index);

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
