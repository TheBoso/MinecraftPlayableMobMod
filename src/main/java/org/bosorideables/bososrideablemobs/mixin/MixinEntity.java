package org.bosorideables.bososrideablemobs.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Entity.class)
public abstract class MixinEntity implements Saddleable {
    @Unique
    private boolean _isSaddled;

    @Unique
    private LivingEntity _cachedLivingEntity;

    @Unique
    private Mob _cachedMob;


    @Final
    private static Logger LOGGER;

    @Override
    public boolean isSaddleable() {
        CacheOurEntity();
        return _cachedLivingEntity.isAlive();
    }

    @Shadow
    public abstract List<Entity> getPassengers();


    private void CacheOurEntity()
    {
        if(_cachedLivingEntity == null)
        {
            if((Object) this instanceof LivingEntity living)
            {
             _cachedLivingEntity = living;
            }
        }
    }

    private void CacheOurMob()
    {
        if(_cachedMob == null && _cachedLivingEntity != null && _cachedLivingEntity instanceof Mob mob)
        {
            _cachedMob = mob;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci)
    {
        CacheOurEntity();
        CacheOurMob();
        if(_cachedMob != null)
        {
            if(!(_cachedLivingEntity instanceof AbstractHorse))
            {
                _cachedMob.setNoAi(_cachedLivingEntity.getFirstPassenger() != null);
            }
        }


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



    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public final void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir)
    {
        if(this.isSaddled() && player.getItemInHand(hand).isEmpty())
        {
            if (!player.level().isClientSide)
            {
                CacheOurEntity();
                if(_cachedLivingEntity != null)
                {
                    player.setInvisible(true);

                    player.startRiding(_cachedLivingEntity, true);
                }
            }
            }
    }

    @Override
    public void equipSaddle(ItemStack itemStack, @Nullable SoundSource soundSource) {
        LOGGER.info("SADDLE EQUIPPED");
        _isSaddled = true;
    }

    @Override
    public boolean isSaddled() {
        return _isSaddled;
    }

}
