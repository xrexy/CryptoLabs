/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.zertex.cryptolabs.CryptoLabs;
import org.zertex.cryptolabs.commands.utils.RegisterAsCommand;
import org.zertex.cryptolabs.handlers.MinersHandler;
import org.zertex.cryptolabs.models.Miner;
import org.zertex.cryptolabs.models.Rarity;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.zertex.cryptolabs.utils.Utils.colorize;

public class CMDCryptoLabs {
  private final CryptoLabs plugin = CryptoLabs.getInstance();

  @RegisterAsCommand(
      command = "/cryptolabs block",
      disallowNonPlayer = true,
      permission = "cryptolabs.block"
  )
  public void onGiveBlock(Player sender, String[] params) {
    try (BukkitAudiences audience = CryptoLabs.getInstance().adventure()) {
      final Audience senderAudience = audience.sender(sender);

      if (sender.getInventory().firstEmpty() == -1) {
        senderAudience.sendMessage(Component.text("You don't have enough space in your inventory!", NamedTextColor.RED));
        return;
      }

      MinersHandler.getInstance().giveMiner(
          sender,
          new Miner(
              Rarity.MYTHIC,
              Material.DROPPER,
              "Default Miner",
              1,
              1
          ));
    }
  }

  private final Function<String, NamespacedKey> namespacedKey = (String key) -> new NamespacedKey(CryptoLabs.getInstance(), key);
}
