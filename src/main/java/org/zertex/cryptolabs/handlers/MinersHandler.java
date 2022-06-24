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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.zertex.cryptolabs.CryptoLabs;
import org.zertex.cryptolabs.models.Miner;

import java.util.List;
import java.util.UUID;

import static org.zertex.cryptolabs.utils.Utils.colorize;
import static org.zertex.cryptolabs.utils.Utils.componentAsLegacy;

public class MinersHandler extends Handler {
  @Getter
  private final static MinersHandler instance = new MinersHandler();
  private static final ObjectMapper mapper = new ObjectMapper();

  private MinersHandler() {
  }

  @Override
  public void onEnable() {

  }

  public void giveMiner(Player player, Miner miner) {
    final ItemStack item = minerToItemStack(miner);
    final ItemMeta meta = item.getItemMeta();
    if(meta == null) return;

    meta.getPersistentDataContainer().set(new NamespacedKey(CryptoLabs.getInstance(), "owner"), PersistentDataType.STRING, player.getUniqueId().toString());

    item.setItemMeta(meta);
    player.getInventory().addItem(item);
  }

  @NotNull
  private ItemStack minerToItemStack(Miner miner) {
    final ItemStack item = new ItemStack(miner.material());
    try {
      final ItemMeta meta = item.getItemMeta();
      if (meta == null) return item;

      meta.setDisplayName(componentAsLegacy(Component.text(miner.displayName(), miner.rarity().getColor())));
      meta.setLore(List.of(
          "",
          colorize("&7Mining Power: &e" + miner.miningPower()),
          colorize("&7Mining Speed: &e" + miner.miningSpeed()),
          "",
          componentAsLegacy(Component.text(miner.rarity().name(), miner.rarity().getColor(), TextDecoration.BOLD))
      ));

      final PersistentDataContainer container = meta.getPersistentDataContainer();

      container.set(new NamespacedKey(CryptoLabs.getInstance(), "uuid"), PersistentDataType.STRING, UUID.randomUUID().toString());
      container.set(new NamespacedKey(CryptoLabs.getInstance(), "data"), PersistentDataType.STRING, mapper.writeValueAsString(miner));

      item.setItemMeta(meta);
    } catch (Exception e) {
      //if (CryptoLabs.getInstance().getConfig().getBoolean("debug"))
      e.printStackTrace();
    }

    return item;
  }
}
