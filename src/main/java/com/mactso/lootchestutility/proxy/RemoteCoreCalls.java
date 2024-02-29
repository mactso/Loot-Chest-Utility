package com.mactso.lootchestutility.proxy;

import com.mactso.harderfarthercore.HarderFartherManager;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class RemoteCoreCalls implements IHarderFartherCoreProxy  {
	
	public float getDifficulty (LivingEntity le) { 
		return HarderFartherManager.getDifficulty(le);
	}

	@Override
	public void addGrimBlockPosListEntry(BlockPos pos, int grimRange) {
	}
	
	@Override
	public void addLifeBlockPosListEntry(BlockPos pos, int lifeRange) {
	}

	@Override
	public float getDifficulty(BlockPos pos) {
		return HarderFartherManager.getDifficulty(pos);
	}

}
