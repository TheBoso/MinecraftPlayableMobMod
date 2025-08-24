package org.bosorideables.bososrideablemobs.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MixinMob extends Entity {

    public MixinMob(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    public void getControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
        var passengerList = this.getPassengers();
        if (passengerList == null) {
            cir.setReturnValue(null);
        } else if (passengerList.isEmpty()) {
            cir.setReturnValue(null);
        } else if (passengerList.get(0) instanceof LivingEntity living) {
            cir.setReturnValue(living);
        }

    }

    /*
    @Nullable
    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    public void getControllingPassenger(CallbackInfoReturnable<LivingEntity> cir)
    {
        if(this.getPassengers().isEmpty())
        {
            cir.setReturnValue(null);
        }
        else if(this.getPassengers().get(0) instanceof LivingEntity living)
        {
            cir.setReturnValue(living);
        }
    }
     */
}
