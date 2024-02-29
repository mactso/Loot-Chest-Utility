package com.mactso.lootchestutility.utility;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.mactso.lootchestutility.manager.ChestLootManager;
import com.mactso.lootchestutility.manager.LootTableListManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
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