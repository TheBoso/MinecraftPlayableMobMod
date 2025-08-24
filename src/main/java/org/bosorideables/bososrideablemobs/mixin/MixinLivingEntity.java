package org.bosorideables.bososrideablemobs.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bosorideables.bososrideablemobs.interfaces.IBosoRideable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IBosoRideable, PlayerRideableJumping {

    //  Something in this class is causing a crash...
    @Shadow
    public float yBodyRot;

    @Shadow
    public float yHeadRot;
    @Shadow
    public abstract void travel(Vec3 travelVector);

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    private float speed;

    @Shadow
    public float zza;


    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tickRidden", at = @At("HEAD"))
    public void tickRidden(Player player, Vec3 travelVector, CallbackInfo ci) {
        tickRiddenLogic(player, travelVector);
    }

    @Unique
    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    @Inject(method = "getRiddenSpeed", at = @At("HEAD"), cancellable = true)
    protected void getRiddenSpeed(Player player, CallbackInfoReturnable<Float> cir)
    {
        cir.setReturnValue((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
    }



    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    protected void getRiddenInput(Player player, Vec3 travelVector, CallbackInfoReturnable<Vec3> cir) {
       /*
        if (this.onGround() && this.playerJumpPendingScale == 0.0F && this.isStanding() && !this.allowStandSliding) {
            return Vec3.ZERO;
        }
        */
            float f = player.xxa * 0.5F;
            float f1 = player.zza;
            if (f1 <= 0.0F)
            {
                f1 *= 0.25F;
            }

            LOGGER.info("Vec3: " + f + ", " + 0.0F + ", " + f1);

            cir.setReturnValue(new Vec3((double)f, (double)0.0F, (double)f1));
        }


    @Override
    public void tickRiddenLogic(Player player, Vec3 travelVector) {
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();


     //   travel(new Vec3(1, travelVector.y, travelVector.z));

        if (this.isControlledByLocalInstance()) {
            if (travelVector.z <= (double) 0.0F) {
            }

            if (this.onGround()) {
                //  this.setIsJumping(false);
                //   if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
                //      this.executeRidersJump(this.playerJumpPendingScale, travelVector);
            }

        }
    }

    @Override
    public void onPlayerJump(int var1) {

    }

    @Override

    public boolean canJump() {
        return this.onGround();
    }

    @Override

    public void handleStartJump(int var1) {

    }

    @Override

    public void handleStopJump() {

    }

    @Override

    public int getJumpCooldown() {
        return 0;
    }



}

