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

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public
class RegisteredCommand {
    private final Method method;
    private final Object instance;
    private final RegisterAsCommand annotation;
}
