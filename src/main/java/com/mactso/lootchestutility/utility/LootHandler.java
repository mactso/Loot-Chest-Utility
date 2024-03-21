package com.mactso.lootchestutility.utility;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import com.mactso.lootchestutility.config.MyConfig;
import com.mactso.lootchestutility.manager.ChestLootManager;
import com.mactso.lootchestutility.manager.LootTableListManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class LootHandler
{
 
    public static class HFLootModifier extends LootModifier
    {

        public static final Supplier<Codec<HFLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, HFLootModifier::new)));


        public HFLootModifier(final LootItemCondition[] conditionsIn) {
            super(conditionsIn);

        }

        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        	if (!LootTableListManager.isBonusLootTable(context.getQueriedLootTableId())) {
//        		System.out.println("false:" + context.getQueriedLootTableId());
        		return generatedLoot;
        	}
        	
        	
        	Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        	if (origin == null) { // can't calculate difficulty without coordinates
//        		System.out.println("No Origin for loot:" + context.getQueriedLootTableId());
        		return generatedLoot;        	
        	}
        	
        	Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        	Level serverlevel = entity.level();

        	// this should never happen.
        	if (serverlevel.isClientSide() ) {
        		return generatedLoot;
        	}
        	
    		MinecraftServer server = serverlevel.getServer();
    		RegistryAccess dynreg = server.registryAccess();
    		Registry<Biome> biomeRegistry = dynreg.registryOrThrow(Registries.BIOME);
    		ResourceLocation biomeKey = biomeRegistry.getKey(serverlevel.getBiome(entity.blockPosition()).value());
    		String biome = biomeKey.toString();
    		if (MyConfig.isOmittedLootBiome(biome)) {
    			return generatedLoot;
    		}
    		
        	ItemStack stack = ChestLootManager.doGetLootStack(context.getLevel(), origin);
//    		System.out.println("Adding Bonus Loot:" + context.getQueriedLootTableId() + ": " +stack.getItem().toString());
        	generatedLoot.add(stack);
			return generatedLoot;

        }


        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC.get();
        }

        

    }
  
       
}