/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.zertex.cryptolabs.handlers.PluginHandler;

public final class CryptoLabs extends JavaPlugin {
  @Getter
  private static CryptoLabs instance;
  private BukkitAudiences adventure;

  public @NonNull BukkitAudiences adventure() {
    if (this.adventure == null) {
      throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
    }
    return this.adventure;
  }

  @Override
  public void onEnable() {
    instance = this;
    this.adventure = BukkitAudiences.create(this);

    // Plugin startup logic
    PluginHandler.getInstance().onEnable();
  }

  @Override
  public void onDisable() {
    if(this.adventure != null) {
      this.adventure.close();
      this.adventure = null;
    }
  }
}
