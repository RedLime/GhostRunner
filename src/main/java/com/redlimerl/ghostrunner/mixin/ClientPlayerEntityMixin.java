package com.redlimerl.ghostrunner.mixin;

import com.mojang.authlib.GameProfile;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.record.GhostInfo;
import com.redlimerl.ghostrunner.record.PlayerLog;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (InGameTimer.getInstance().getStatus() == TimerStatus.RUNNING) {
            GhostInfo.INSTANCE.update(new PlayerLog(this));
            ReplayGhost.tickGhost();
            if (world.getDifficulty().getId() < GhostRunner.MINIMUM_DIFFICULTY.getId()) {
                GhostRunner.MINIMUM_DIFFICULTY = world.getDifficulty();
                GhostInfo.INSTANCE.getGhostData().setDifficulty(GhostRunner.MINIMUM_DIFFICULTY);
            }
        }
    }
}
