package org.bosorideables.bososrideablemobs.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface IBosoRideable
{
    void tickRiddenLogic(Player player, Vec3 travelVector);
}
