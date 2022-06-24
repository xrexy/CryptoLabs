/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs.models;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

public enum Rarity {
  COMMON(NamedTextColor.WHITE),
  UNCOMMON(NamedTextColor.GREEN),
  RARE(NamedTextColor.AQUA),
  LEGENDARY(NamedTextColor.GOLD),
  MYTHIC(NamedTextColor.LIGHT_PURPLE),
  ARTIFACT(NamedTextColor.RED),
  UNKNOWN(NamedTextColor.GRAY);

  @Getter
  private final NamedTextColor color;

  Rarity(NamedTextColor color) {
    this.color = color;
  }
}
