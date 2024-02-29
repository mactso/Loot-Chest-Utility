package com.mactso.lootchestutility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.lootchestutility.config.MyConfig;
import com.mactso.lootchestutility.event.MonsterDropEventHandler;
import com.mactso.lootchestutility.proxy.IHarderFartherCoreProxy;
import com.mactso.lootchestutility.proxy.LocalCoreCalls;
import com.mactso.lootchestutility.proxy.RemoteCoreCalls;
import com.mactso.lootchestutility.utility.Utility;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;


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
	    	} else {
	    		difficultyCallProxy = new LocalCoreCalls ();
	    	}

			
	    }

		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
				Utility.debugMsg(0, MODID + ": Registering Handlers");
				MinecraftForge.EVENT_BUS.register(MonsterDropEventHandler.class);
			
 		} 
		
		
}
