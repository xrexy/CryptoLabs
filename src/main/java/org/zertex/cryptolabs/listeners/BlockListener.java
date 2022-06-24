/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.zertex.cryptolabs.CryptoLabs;


public class BlockListener implements Listener {
  private final CryptoLabs plugin = CryptoLabs.getInstance();

  @EventHandler
  void onBlockBreak(FurnaceSmeltEvent event) {
    final Block block = event.getBlock();
    BlockState blockState = block.getState();

    if (blockState instanceof TileState tileState) {
      PersistentDataContainer container = tileState.getPersistentDataContainer();
      NamespacedKey key = new NamespacedKey(plugin, "smeltCount");

      if (container.has(key, PersistentDataType.INTEGER)) {
        int currentAmount = container.get(key, PersistentDataType.INTEGER);
        container.set(key, PersistentDataType.INTEGER, currentAmount + 1);
      } else {
        container.set(key, PersistentDataType.INTEGER, 1);
      }

      Bukkit.getLogger().warning("Smelted " + container.get(key, PersistentDataType.INTEGER) + " times");

      tileState.update();
    }
  }
    @EventHandler
    void click (InventoryClickEvent event){
      if(event.getCurrentItem() != null) {
        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        Bukkit.getLogger().warning(container.getKeys().toString());
      }
    }
}
