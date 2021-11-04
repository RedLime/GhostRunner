package com.redlimerl.ghostrunner.mixin;

import com.redlimerl.ghostrunner.record.GhostInfo;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.ghostrunner.record.data.Timeline;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.Objects;

@Mixin(ClientAdvancementManager.class)
public class ClientAdvancementManagerMixin {

    @Shadow @Final private AdvancementManager manager;

    @Redirect(method = "onAdvancements", at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;"))
    public Object advancement(Map.Entry<Identifier, AdvancementProgress> entry) {
        Advancement advancement = this.manager.get(entry.getKey());
        AdvancementProgress advancementProgress = entry.getValue();
        assert advancement != null;
        advancementProgress.init(advancement.getCriteria(), advancement.getRequirements());
        if (advancementProgress.isDone() && InGameTimer.INSTANCE.getStatus() != TimerStatus.NONE) {
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
        return entry.getValue();
    }
}
