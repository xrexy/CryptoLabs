/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs.handlers;

import lombok.Getter;
import org.zertex.cryptolabs.CryptoLabs;
import org.zertex.cryptolabs.commands.CommandsHandler;

public class PluginHandler extends Handler {
  @Getter
  private final static PluginHandler instance = new PluginHandler();

  private PluginHandler() {
  }

  @Override
  public void onEnable() {
    // Register commands
    CommandsHandler.getInstance().onEnable();

    // Register listeners
    ListenerHandler.getInstance().onEnable();

    // Register miners
    MinersHandler.getInstance().onEnable();
  }
}
