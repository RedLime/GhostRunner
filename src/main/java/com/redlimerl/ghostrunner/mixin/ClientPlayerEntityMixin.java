package com.redlimerl.ghostrunner.mixin;

import com.mojang.authlib.GameProfile;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.record.GhostInfo;
import com.redlimerl.ghostrunner.record.PlayerLog;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.ghostrunner.record.data.Timeline;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    public ClientPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (InGameTimer.INSTANCE.getStatus() == TimerStatus.RUNNING) {
            GhostInfo.INSTANCE.update(new PlayerLog(this));
            ReplayGhost.tickGhost();
            if (!GhostRunner.isUseF3 && client.options.debugEnabled) {
                GhostRunner.isUseF3 = true;
                GhostInfo.INSTANCE.getGhostData().setUseF3(true);
            }
            if (world.getDifficulty().getId() < GhostRunner.minimumDifficulty.getId()) {
                GhostRunner.minimumDifficulty = world.getDifficulty();
                GhostInfo.INSTANCE.getGhostData().setDifficulty(GhostRunner.minimumDifficulty);
            }
        }
    }
}
