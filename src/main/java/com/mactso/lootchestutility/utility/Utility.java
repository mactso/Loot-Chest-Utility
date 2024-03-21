package com.mactso.lootchestutility.utility;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.mactso.lootchestutility.config.MyConfig;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class Utility {
	public final static int FOUR_SECONDS = 80;
	public final static int TWO_SECONDS = 40;
	public static final int TICKS_PER_SECOND = 20;
	
	public final static float Pct00 = 0.00f;
	public final static float Pct02 = 0.02f;
	public final static float Pct05 = 0.05f;
	public final static float Pct09 = 0.09f;
	public final static float Pct16 = 0.16f;
	public final static float Pct25 = 0.25f;
	public final static float Pct50 = 0.50f;
	public final static float Pct75 = 0.75f;
	public final static float Pct84 = 0.84f;
	public final static float Pct89 = 0.89f;
	public final static float Pct91 = 0.91f;
	public final static float Pct95 = 0.95f;
	public final static float Pct99 = 0.99f;
	public final static float Pct100 = 1.0f;


	private static final Logger LOGGER = LogManager.getLogger();

	public static void initReports() {
		File fd = new File("config/lootcontrolutility");
		if (!fd.exists())
			fd.mkdir();
		File fb = new File("config/lootcontrolutility/Biomes.rpt");
		if (fb.exists())
			fb.delete();
		
		File fbt = new File("config/lootcontrolutility/BiomeTags.rpt");
		if (fbt.exists())
			fbt.delete();

		File fbl = new File("config/lootcontrolutility/LootTables.rpt");
		if (fbl.exists())
			fbl.delete();

	}
	
	
	public static void debugMsg(int level, String dMsg) {

		if (MyConfig.getDebugLevel() > level - 1) {
			LOGGER.info("L" + level + ":" + dMsg);
		}

	}

	public static void debugMsg(int level, BlockPos pos, String dMsg) {
		if (MyConfig.getDebugLevel() > level - 1) {
			LOGGER.info("L" + level + " (" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "): " + dMsg);
		}
	}

	public static void debugMsg(int level, LivingEntity le, String dMsg) {
		if (MyConfig.getDebugLevel() > level - 1) {
			LOGGER.info("L" + level + " (" 
					+ le.blockPosition().getX() + "," 
					+ le.blockPosition().getY() + ","
					+ le.blockPosition().getZ() + "): " + dMsg);
		}
	}

	public static void sendBoldChat(Player p, String chatMessage, ChatFormatting textColor) {
		MutableComponent component = Component.literal(chatMessage);
		component.setStyle(component.getStyle().withBold(true));
		component.setStyle(component.getStyle().withColor(textColor));
		p.sendSystemMessage(component);
	}

	public static void sendChat(Player p, String chatMessage, ChatFormatting textColor) {
		MutableComponent component = Component.literal(chatMessage);
		component.setStyle(component.getStyle().withColor(textColor));
		p.sendSystemMessage(component);
	}

	public static BlockPos getBlockPosition (Entity e) {
		return e.blockPosition();
	}
	
	public static void updateEffect(LivingEntity e, int amplifier, MobEffect mobEffect, int duration) {
		MobEffectInstance ei = e.getEffect(mobEffect);
		if (amplifier == 10) {
			amplifier = 20; // player "plaid" speed.
		}
		if (ei != null) {
			if (amplifier > ei.getAmplifier()) {
				e.removeEffect(mobEffect);
			}
			if (amplifier == ei.getAmplifier() && ei.getDuration() > 10) {
				return;
			}
			if (ei.getDuration() > 10) {
				return;
			}
			e.removeEffect(mobEffect);
		}
		e.addEffect(new MobEffectInstance(mobEffect, duration, amplifier, true, true));
		return;
	}

	public static boolean populateEntityType(EntityType<?> et, ServerLevel level, BlockPos savePos, int range,
			int modifier) {
		boolean isBaby = false;
		return populateEntityType(et, level, savePos, range, modifier, isBaby);
	}

	public static boolean populateEntityType(EntityType<?> et, ServerLevel level, BlockPos savePos, int range,
			int modifier, boolean isBaby) {
		boolean persistant = false;
		return populateEntityType(et, level, savePos, range, modifier, persistant, isBaby);
	}

	public static boolean populateEntityType(EntityType<?> et, ServerLevel level, BlockPos savePos, int range,
			int modifier, boolean persistant, boolean isBaby) {
		int numZP;
		Mob e;
		numZP = level.random.nextInt(range) - modifier;
		if (numZP < 0)
			return false;
		for (int i = 0; i <= numZP; i++) {
			if (et == EntityType.PHANTOM) {
				e = (Mob) et.spawn(level, savePos.north(2).west(2), MobSpawnType.SPAWNER);
			} else {
				e = (Mob) et.spawn(level, savePos.north(2).west(2), MobSpawnType.NATURAL);
			}
			if (e == null) {
				return false;
			}
			if (persistant) {
				e.setPersistenceRequired();
			}
			if (et == EntityType.ZOMBIFIED_PIGLIN) {
				e.setAggressive(true);
			}
			e.setBaby(isBaby);
		}
		return true;
	}

	
	public static void setLore(ItemStack stack, String inString)
	{
		CompoundTag tag = stack.getOrCreateTagElement("display");
		ListTag list = new ListTag();
		list.add(StringTag.valueOf(inString));
		tag.put("Lore", list);
	}
	
	
	public static ItemResult parseItemKey(StringReader sr) throws CommandSyntaxException
	{
		CompoundTag nbt = null;
		String rl = sr.readUnquotedString();
		if (sr.canRead() && sr.peek() == ':')
		{
			sr.skip();
			rl = rl + ":" + sr.readUnquotedString();
		}
		sr.skipWhitespace();
		if (sr.canRead() && sr.peek() == '{')
		{
			nbt = new TagParser(sr).readStruct();
			if (nbt.isEmpty())
				nbt = null;
		}
		return new ItemResult(new ResourceLocation(rl), nbt);
	}
	
	
	public static class ItemResult
	{
		public final ResourceLocation res;
		public final CompoundTag nbt;

		public ItemResult(ResourceLocation res, @Nullable CompoundTag nbt)
		{
			this.res = res;
			this.nbt = nbt;
		}
	}
	
	public static boolean isNotNearWebs(BlockPos pos, ServerLevel serverLevel) {

		if (serverLevel.getBlockState(pos).getBlock() == Blocks.COBWEB)
			return true;
		if (serverLevel.getBlockState(pos.above()).getBlock() == Blocks.COBWEB)
			return true;
		if (serverLevel.getBlockState(pos.below()).getBlock() == Blocks.COBWEB)
			return true;
		if (serverLevel.getBlockState(pos.north()).getBlock() == Blocks.COBWEB)
			return true;
		if (serverLevel.getBlockState(pos.south()).getBlock() == Blocks.COBWEB)
			return true;
		if (serverLevel.getBlockState(pos.east()).getBlock() == Blocks.COBWEB)
			return true;
		if (serverLevel.getBlockState(pos.west()).getBlock() == Blocks.COBWEB)
			return true;

		return false;
	}

	public static boolean isOutside(BlockPos pos, ServerLevel serverLevel) {
		return serverLevel.getHeightmapPos(Types.MOTION_BLOCKING_NO_LEAVES, pos) == pos;
	}

	public static Item getItemFromString (String name)
	{
		Item ret = Items.AIR;
		try {
			ResourceLocation key = new ResourceLocation(name);
			if (ForgeRegistries.ITEMS.containsKey(key))
			{
				ret = ForgeRegistries.ITEMS.getValue(key);
			}
			else {
				LOGGER.warn("Unknown item: " + name);
			}
		}
		catch (Exception e)
		{
			LOGGER.warn("Bad item: " + name);
		}
		return ret;
	}
	
	
	public static void validateOmitBiomeConfig (ServerStartingEvent event) {
		MinecraftServer server = event.getServer();
		RegistryAccess dynreg = server.registryAccess();
		Registry<Biome> biomeRegistry = dynreg.registryOrThrow(Registries.BIOME);
		for(String omitkey: MyConfig.getOmittedLootBiome()) {
			ResourceLocation key = new ResourceLocation(omitkey);
			if (!biomeRegistry.containsKey(key)) {
				LOGGER.error("LootControlUtility:  Bad Biome in Omit Biome Loot Configuration value (spelled wrong?) : " + omitkey);
			}
			
		}
	}
	
	public static void generateBiomeReport(ServerStartingEvent event) {

		PrintStream p = null;
		try {
			p = new PrintStream(new FileOutputStream("config/lootcontrolutility/Biomes.rpt", false));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (p == null) {
			p = System.out;
		}
		int biomelineNumber = 0;
		MinecraftServer server = event.getServer();
		RegistryAccess dynreg = server.registryAccess();
		Registry<Biome> biomeRegistry = dynreg.registryOrThrow(Registries.BIOME);

		for (Biome b : biomeRegistry) {
			String bn = biomeRegistry.getKey(b).toString();
			Optional<Holder.Reference<Biome>> oBH = biomeRegistry.getHolder(biomeRegistry.getId(b));
			p.println(bn);
		}

		if (p != System.out) {
			p.close();
		}
	}

	public static void generateBiomeTagReport(ServerStartingEvent event) {

		PrintStream p = null;
		try {
			p = new PrintStream(new FileOutputStream("config/lootcontrolutility/BiomeTags.rpt", false));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (p == null) {
			p = System.out;
		}
		int biomelineNumber = 0;
		MinecraftServer server = event.getServer();
		RegistryAccess dynreg = server.registryAccess();
		Registry<Biome> biomeRegistry = dynreg.registryOrThrow(Registries.BIOME);
		List<TagKey<Biome>> bt = biomeRegistry.getTagNames().toList();
		for (TagKey<Biome> tag : bt) {
			p.println(tag.toString());
		}
		biomeRegistry.getTagNames().forEach(p::println);

		if (p != System.out) {
			p.close();
		}
	}
	
	public static void generateLootingTableReports(ServerStartingEvent event) {
		List<String> report = new ArrayList<String>();
		
		PrintStream p = null;
		try {
			p = new PrintStream(new FileOutputStream("config/lootcontrolutility/LootTables.rpt", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (p == null) {
			p = System.out;
		}
		
		LootDataManager ldm = event.getServer().getLootData();
		Collection<ResourceLocation> keys = ldm.getKeys(LootDataType.TABLE);
		for (ResourceLocation k : keys) {
			if (k.getPath().contains("chests")) {
				report.add(k.toString());
			}
		}

		Collections.sort(report);
		for (String str : report) {
				p.println(str);
		}
		
		if (p != System.out) {
			p.close();
		}
	
	}
	
}
