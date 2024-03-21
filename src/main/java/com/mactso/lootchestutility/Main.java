package com.mactso.lootchestutility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mactso.lootchestutility.commands.ModCommands;
import com.mactso.lootchestutility.config.MyConfig;
import com.mactso.lootchestutility.event.MonsterDropEventHandler;
import com.mactso.lootchestutility.proxy.IHarderFartherCoreProxy;
import com.mactso.lootchestutility.proxy.LocalCoreCalls;
import com.mactso.lootchestutility.proxy.RemoteCoreCalls;
import com.mactso.lootchestutility.utility.LootHandler.HFLootModifier;
import com.mactso.lootchestutility.utility.Utility;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;


@Mod("lootcontrolutility")
public class Main {

	    public static final String MODID = "lootcontrolutility"; 
		private static final Logger LOGGER = LogManager.getLogger();
	    public static IHarderFartherCoreProxy difficultyCallProxy;

		public Main()
	    {
	    	System.out.println(MODID + ": Registering Mod.");
	  		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	  		
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC );
	  		
	    	if (ModList.get().isLoaded("harderfarthercore")) {
	    		difficultyCallProxy = new RemoteCoreCalls ();
	    		MyConfig.setUsingCore(true);
	    	} else {
	    		difficultyCallProxy = new LocalCoreCalls ();
	    		MyConfig.setUsingCore(false);

	    	}

			
	    }

		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
				Utility.debugMsg(0, MODID + ": Registering Handlers");
				MinecraftForge.EVENT_BUS.register(MonsterDropEventHandler.class);
			
 		} 
		
		@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	    public static class ModEvents
	    {

		    @SubscribeEvent
		    public static void onRegister(final RegisterEvent event)
		    {
		    	@Nullable
				IForgeRegistry<Object> fr = event.getForgeRegistry();
		    	
		    	@NotNull
				ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
	    		Utility.debugMsg(0, "RegistryKeys " +key.toString());

		    	//		    	if (key.equals(ForgeRegistries.Keys.BLOCKS))
//		    		ModBlocks.register(event.getForgeRegistry());
//		    	else if (key.equals(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES))
//		    		ModBlockEntities.register(event.getForgeRegistry());
//		    	else if (key.equals(ForgeRegistries.Keys.ITEMS))
//		    		ModItems.register(event.getForgeRegistry());
//		    	else if (key.equals(ForgeRegistries.Keys.SOUND_EVENTS))
//		    		ModSounds.register(event.getForgeRegistry());
//		    	else 
		    		if (key.equals(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS)) {
		    		Codec<HFLootModifier> c = HFLootModifier.CODEC.get();
		    		event.getForgeRegistry().register("special", HFLootModifier.CODEC.get());
		    			Utility.debugMsg(0, "Registered " +key.toString());
		    		}
		    }
		    
	    }	
		
		
		
		@Mod.EventBusSubscriber()
		public static class ForgeEvents {
			@SubscribeEvent
			public static void onServerStarting(ServerStartingEvent event) {

				if (MyConfig.isGenerateReports()) {
					Utility.generateLootingTableReports(event);
					Utility.generateBiomeReport(event);
					Utility.generateBiomeTagReport(event);
				}
				Utility.validateOmitBiomeConfig(event);
				
			}

			@SubscribeEvent 		
			public static void onCommandsRegistry(final RegisterCommandsEvent event) {
				Utility.debugMsg(0,"Loot Control Utilty: Registering Mod Command Dispatcher");
				ModCommands.register(event.getDispatcher());			
			}

		
		
		}
		
		
		
}
