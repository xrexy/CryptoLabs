/*
 * Copyright (c) 2022. Zertex Development <zertexdevelopment@gmail.com>
 * All rights reserved.
 *
 * This file is part of the CryptoLabs project.
 *
 * The CryptoLabs project can not be copied and/or distributed without the express
 * permission of a holder from Zertex Development <zertexdevelopment@gmail.com>.
 */

package org.zertex.cryptolabs.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class Utils {
  public static String colorize(String s) {
    return ChatColor.translateAlternateColorCodes('&', s);
  }

  public static String stripColor(String s) {
    return ChatColor.stripColor(colorize(s));
  }

  public static String componentAsLegacy(Component c) {
    return colorize(LegacyComponentSerializer.legacyAmpersand().serialize(c));
  }

}
