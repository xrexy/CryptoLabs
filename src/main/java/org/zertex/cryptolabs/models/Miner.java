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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Material;

public record Miner(
    Rarity rarity,
    Material material,
    @JsonProperty("display_name") String displayName,
    @JsonProperty("mining_power") int miningPower,
    @JsonProperty("mining_speed") int miningSpeed
) {
}
