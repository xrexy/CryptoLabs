/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs.commands.utils;

public enum CommandFailReason {
    INSUFFICIENT_PARAMETER, // needs more parameters
    REDUNDANT_PARAMETER,    // too many parameters
    NO_PERMISSION,          // sender has no permission to execute the command
    NOT_PLAYER,             // sender is not a player but the command is for players only
    COMMAND_NOT_FOUND,      // cannot find a command matching sender's request
    REFLECTION_ERROR        // a reflection error occurred during processing command
}
