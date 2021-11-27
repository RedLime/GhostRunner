package com.redlimerl.ghostrunner.mixin;

import com.redlimerl.ghostrunner.record.GhostInfo;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.ghostrunner.record.data.Timeline;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(value = ClientAdvancementManager.class, priority = 999)
public class ClientAdvancementManagerMixin {

    @ModifyArgs(method = "onAdvancements", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/AdvancementToast;<init>(Lnet/minecraft/advancement/Advancement;)V"))
    public void advancement(Args args) {
        Advancement advancement = args.get(0);
        if (InGameTimer.getInstance().getStatus() != TimerStatus.NONE && InGameTimer.getInstance().getStatus() != TimerStatus.COMPLETED) {
            if (Objects.equals(advancement.getId(), new Identifier("story/enter_the_nether"))) {
                if (GhostInfo.INSTANCE.setCheckPoint(Timeline.Moment.ENTER_NETHER))
                    ReplayGhost.sendBestCheckPointMessage(Timeline.Moment.ENTER_NETHER);
            } else if (Objects.equals(advancement.getId(), new Identifier("nether/find_bastion"))) {
                if (GhostInfo.INSTANCE.setCheckPoint(Timeline.Moment.ENTER_PIGLIN_BRUTE))
                    ReplayGhost.sendBestCheckPointMessage(Timeline.Moment.ENTER_PIGLIN_BRUTE);
            } else if (Objects.equals(advancement.getId(), new Identifier("nether/find_fortress"))) {
                if (GhostInfo.INSTANCE.setCheckPoint(Timeline.Moment.ENTER_NETHER_FORTRESS))
                    ReplayGhost.sendBestCheckPointMessage(Timeline.Moment.ENTER_NETHER_FORTRESS);
            } else if (Objects.equals(advancement.getId(), new Identifier("story/enter_the_end"))) {
                if (GhostInfo.INSTANCE.setCheckPoint(Timeline.Moment.ENTER_THE_END))
                    ReplayGhost.sendBestCheckPointMessage(Timeline.Moment.ENTER_THE_END);
            }
        }
    }
}
