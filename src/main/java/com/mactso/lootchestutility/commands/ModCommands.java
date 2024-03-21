package com.mactso.lootchestutility.commands;

import java.text.DecimalFormat;

import com.mactso.lootchestutility.Main;
import com.mactso.lootchestutility.proxy.LocalCoreCalls;
import com.mactso.lootchestutility.config.MyConfig;
import com.mactso.lootchestutility.utility.Utility;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;


public class ModCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal(Main.MODID).requires((source) -> {
			return source.hasPermission(3);
		}).then(Commands.literal("setDebugLevel")
				.then(Commands.argument("debugLevel", IntegerArgumentType.integer(0, 2)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setDebugLevel(p, IntegerArgumentType.getInteger(ctx, "debugLevel"));
				})))
				// update or add a speed value for the block the player is standing on.
				.then(Commands.literal("info").executes(ctx ->  {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							printInfo(p);
							return 1;
				}))
			);
	}
	
	public static int setDebugLevel(ServerPlayer p, int newDebugLevel) {
		MyConfig.setDebugLevel(newDebugLevel);
		printInfo(p);
		return 1;
	}
	
	private static void printInfo(ServerPlayer p) {

		String chatMessage = "\nLooting Control Utility: ";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		if (MyConfig.isUsingCore()) {
			Utility.sendChat(p, "Using HarderFartherCore for difficulty.", ChatFormatting.DARK_GREEN);
		} else {
			Utility.sendChat(p, "Using Looting Control Utility ("+ LocalCoreCalls.CENTER + ") for difficulty.", ChatFormatting.DARK_GREEN);
			Utility.sendChat(p, "Maximum Difficulty reached " + MyConfig.getLootBoostDistance() + " blocks from World Spawn .", ChatFormatting.DARK_GREEN);
		}
		
		float difficulty = 100 * Main.difficultyCallProxy.getDifficulty(p.blockPosition());
		DecimalFormat df = new DecimalFormat("##.##"); 
		String formatted = df.format(difficulty);
		Utility.sendChat(p, "Difficulty at " + p.blockPosition() + " is " + formatted + "% .", ChatFormatting.DARK_GREEN);
	}

}
