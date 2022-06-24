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
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.zertex.cryptolabs.CryptoLabs;
import org.zertex.cryptolabs.listeners.BlockListener;

import java.util.ArrayList;
import java.util.List;

public class ListenerHandler extends Handler {
  @Getter
  private final static ListenerHandler instance = new ListenerHandler();
  private final ArrayList<Listener> listeners = new ArrayList<>(List.of(new BlockListener()));
  private final CryptoLabs plugin = CryptoLabs.getInstance();

  private ListenerHandler() {
  }

  @Override
  public void onEnable() {
    final PluginManager manager = this.plugin.getServer().getPluginManager();
    listeners.forEach(listener -> manager.registerEvents(listener, this.plugin));
  }
}
