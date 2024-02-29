package com.mactso.lootchestutility.proxy;

import com.mactso.lootchestutility.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class LocalCoreCalls implements IHarderFartherCoreProxy {

	BlockPos CENTER = BlockPos.containing(0,128,0);
	
	public float getDifficulty (LivingEntity le) {
		return 0.0f;
	}

	@Override
	public void addGrimBlockPosListEntry(BlockPos pos, int range) {
		/* not used in looting mod */
	}
	
	@Override
	public void addLifeBlockPosListEntry(BlockPos pos, int lifeRange) {
		/* not used in looting mod */
	}

	@Override
	public float getDifficulty(BlockPos pos) {
		// return difficulty based on grim citadel list only.
		double maxDistance = MyConfig.getLootBoostDistance();
		double difficulty = ( CENTER.distManhattan(pos)) / maxDistance ;
		return (float) difficulty;
	}
	

	
}
