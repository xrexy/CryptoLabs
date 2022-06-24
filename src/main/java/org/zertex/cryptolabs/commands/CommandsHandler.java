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

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.zertex.cryptolabs.CryptoLabs;
import org.zertex.cryptolabs.commands.CMDCryptoLabs;
import org.zertex.cryptolabs.commands.utils.CommandFailReason;
import org.zertex.cryptolabs.commands.utils.CommandFailureHandler;
import org.zertex.cryptolabs.commands.utils.RegisterAsCommand;
import org.zertex.cryptolabs.commands.utils.RegisteredCommand;
import org.zertex.cryptolabs.handlers.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CommandsHandler extends Handler implements CommandExecutor {
  @Getter
  private final static CommandsHandler instance = new CommandsHandler();
  private final Plugin plugin = CryptoLabs.getInstance();

  private final Map<String, RegisteredCommand> registeredCommandTable = new HashMap<>();
  private final Map<String, ArrayList<String>> registeredCommandTabCompleters = new HashMap<>();

  private CommandsHandler() {
  }

  @Override
  public void onEnable() {
    instance.registerCommands(new CMDCryptoLabs());
  }

  private CommandFailureHandler failureHandler = (reason, sender, command) -> {
    switch (reason) {
      case COMMAND_NOT_FOUND -> sender.sendMessage("Can't find the command. Please check your spellings!");
      case NOT_PLAYER -> sender.sendMessage("You must be a player to execute this command!");
      case REDUNDANT_PARAMETER -> sender.sendMessage("You have provided redundant parameters!");
      case NO_PERMISSION -> sender.sendMessage("You have no permission to execute this command!");
      case INSUFFICIENT_PARAMETER -> sender.sendMessage("You have provided insufficient parameters!");
      case REFLECTION_ERROR -> sender.sendMessage("Reflection error!");
      default -> sender.sendMessage("Unknown error (" + reason.name() + ")");
    }
  };

  /**
   * Set your own failure handler.
   *
   * @param failureHandler a CommandFailureHandler instance
   */
  public void setCustomFailureHandler(CommandFailureHandler failureHandler) {
    this.failureHandler = failureHandler;
  }

  /**
   * Register all annotated commands in an instance.
   *
   * @param object object containing annotated commands
   */
  public void registerCommands(Object object) {
    // Find annotated methods in an object
    for (Method method : object.getClass().getMethods()) {
      RegisterAsCommand annotation = method.getAnnotation(RegisterAsCommand.class);

      if (annotation != null) {
        final String[] str = annotation.command().split(" ");
        final String base = str[0].substring(1);

        final PluginCommand basePluginCommand = plugin.getServer().getPluginCommand(base);

        if (basePluginCommand == null) {
          throw new RuntimeException(String.format("Unable to register command base '%s'. Did you put it in plugin.yml?", base));
        } else {
          basePluginCommand.setExecutor(this);

          try {
            if (registeredCommandTabCompleters.containsKey(base)) registeredCommandTabCompleters.get(base).add(str[1]);
            else registeredCommandTabCompleters.put(base, new ArrayList<>(Collections.singletonList(str[1])));
          } catch (Exception e) {
            throw new RuntimeException(String.format("Something went wrong while initializing the tab completer for '%s' - %s", base, e.getMessage()));
          }

          registeredCommandTable.put(annotation.command().substring(1), new RegisteredCommand(method, object, annotation));
        }
      }
    }

    registeredCommandTabCompleters.forEach((base, subcommand) -> Optional.ofNullable(plugin.getServer().getPluginCommand(base))
        .ifPresent((command) -> command.setTabCompleter((sender, command1, label, args) ->
            args.length == 1 ? subcommand.stream().filter((s) -> s.startsWith(args[0])).collect(Collectors.toList()) : null)));
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
    // Try to find a registered command matching player's input
    StringBuilder sb = new StringBuilder();
    for (int i = -1; i <= args.length - 1; i++) {
      if (i == -1)
        sb.append(command.getName().toLowerCase());
      else
        sb.append(" ").append(args[i].toLowerCase());

      for (String usage : registeredCommandTable.keySet()) {
        if (usage.equals(sb.toString())) {
          RegisteredCommand wrapper = registeredCommandTable.get(usage);
          RegisterAsCommand annotation = wrapper.getAnnotation();

          String[] actualParams = Arrays.copyOfRange(args, annotation.command().split(" ").length - 1, args.length);

          // check and handle exceptions

          // check whether the command only allows to be executed by a player and whether user is a player
          if (!(sender instanceof Player) && annotation.disallowNonPlayer()) {
            failureHandler.handleFailure(CommandFailReason.NOT_PLAYER, sender, wrapper);
            return true;
          }

          // check whether user has no permission the command requires
          if (!annotation.permission().equals("") && !sender.hasPermission(annotation.permission())) {
            failureHandler.handleFailure(CommandFailReason.NO_PERMISSION, sender, wrapper);
            return true;
          }

          // check whether user has entered wrong number of parameters
          if (actualParams.length != annotation.parameters() && !annotation.overrideParameterLimit()) {
            if (actualParams.length > annotation.parameters())
              failureHandler.handleFailure(CommandFailReason.REDUNDANT_PARAMETER, sender, wrapper);
            else if (actualParams.length < annotation.parameters())
              failureHandler.handleFailure(CommandFailReason.INSUFFICIENT_PARAMETER, sender, wrapper);
            return true;
          }

          // user is eligible to execute the command. let's go
          try {
            wrapper.getMethod().invoke(wrapper.getInstance(), sender, actualParams);
            return true;
          } catch (IllegalAccessException | InvocationTargetException e) {
            failureHandler.handleFailure(CommandFailReason.REFLECTION_ERROR, sender, wrapper);
            e.printStackTrace();
          }
          return true;
        }
      }
    }

    // If we go here, there are no registered commands matching player's input
    failureHandler.handleFailure(CommandFailReason.COMMAND_NOT_FOUND, sender, null);
    return true;
  }
}