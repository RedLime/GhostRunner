package com.redlimerl.ghostrunner.record;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.entity.GhostEntity;
import com.redlimerl.ghostrunner.record.data.Timeline;
import com.redlimerl.ghostrunner.util.Utils;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ReplayGhost {

    //고스트 스킨 목록
    private static final HashMap<UUID, Identifier> skins = new HashMap<>();
    public static void addPlayerSkin(UUID uuid, Identifier id) {
        skins.put(uuid, id);
    }
    public static Identifier getPlayerSkin(UUID uuid) {
        return skins.getOrDefault(uuid, DefaultSkinHelper.getTexture());
    }

    //선택한 고스트 목록
    private static final HashMap<Long, ArrayList<UUID>> selectedGhosts = new HashMap<>();
    public static void toSelectGhosts(Long seed, UUID... uuids) {
        ArrayList<UUID> uuidList = new ArrayList<>();
        int c = 0;
        for (UUID uuid : uuids) {
            if (c > 4) break;
            uuidList.add(uuid);
            c++;
        }
        selectedGhosts.put(seed, uuidList);
    }
    public static ArrayList<UUID> getSelectedGhosts(Long seed) {
        return selectedGhosts.getOrDefault(seed, new ArrayList<>());
    }
    public static void removeInSelectedGhosts(Long seed, UUID... uuids) {
        for (UUID uuid : uuids) {
            if (selectedGhosts.containsKey(seed)) {
                selectedGhosts.get(seed).remove(uuid);
            }
        }
    }

    //체크 포인트 메세지
    public static void sendBestCheckPointMessage(Timeline.Moment moment) {
        if (!SpeedRunOptions.getOption(RunnerOptions.TOGGLE_CHECKPOINT_MESSAGE)) return;

        long bestTime = 0;
        for (ReplayGhost replayGhost : ghostList) {
            long time = replayGhost.ghostInfo.getCheckPoint(moment);
            if (time != 0 && (bestTime == 0 || bestTime > time)) {
                bestTime = time;
            }
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        long nowTime = InGameTimer.getInstance().getInGameTime();
        if (bestTime == 0) {
            player.sendMessage(
                    new TranslatableText("ghostrunner.message.checkpoint_new")
                            .append(new LiteralText(" [" + InGameTimer.timeToStringFormat(nowTime) + "]")
                                    .formatted(Formatting.YELLOW)), false
            );
        } else {
            boolean isFast = bestTime > nowTime;
            player.sendMessage(
                    new TranslatableText("ghostrunner.message.checkpoint_" + (isFast ? "faster" : "slower") + "_than_ghost")
                            .append(new LiteralText(" [" + InGameTimer.timeToStringFormat(nowTime)).formatted(Formatting.YELLOW))
                            .append(new LiteralText(" (" + (isFast ? "-" : "+") + " " + (InGameTimer.timeToStringFormat(Math.abs(nowTime - bestTime))) + ")")
                                    .formatted(isFast ? Formatting.GREEN : Formatting.RED))
                            .append(new LiteralText("]").formatted(Formatting.YELLOW)), false);
        }
    }

    private static final ArrayList<ReplayGhost> ghostList = new ArrayList<>();

    public static void insertBrains(Long seed) {
        ghostList.clear();

        if (!selectedGhosts.containsKey(seed) || selectedGhosts.get(seed).isEmpty()) {
            return;
        }

        for (UUID uuid : selectedGhosts.get(seed)) {
            GhostInfo ghostInfo = GhostInfo.fromData(uuid);
            if (ghostInfo.getGhostData().getSeed() == seed && Objects.equals(ghostInfo.getGhostData().getClientVersion(), GhostRunner.CLIENT_VERSION)) {
                ghostList.add(new ReplayGhost(ghostInfo));
                Utils.downloadPlayerSkin(ghostInfo.getGhostData().getGhostUserUuid());
            }
        }
    }

    public static void tickGhost() {
        if (ghostList.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.player.clientWorld == null) return;
        ClientWorld playerWorld = client.player.clientWorld;

        for (ReplayGhost replay : ghostList) {
            PlayerLog playerLog = replay.ghostInfo.pollPlayerLog();
            if (playerLog == null) {
                replay.remove();
                continue;
            }

            if (playerLog.world != null && replay.lastWorld != playerLog.world) replay.lastWorld = playerLog.world;

            if (replay.ghost == null) {
                if (!Objects.equals(replay.lastWorld.toString(), playerWorld.getRegistryKey().getValue().toString())) {
                    continue;
                }
                replay.summon(playerWorld, playerLog);
            }

            if (!Objects.equals(replay.lastWorld.toString(), playerWorld.getRegistryKey().getValue().toString())
                    || !Objects.equals(replay.ghost.world.getRegistryKey().getValue().toString(), playerWorld.getRegistryKey().getValue().toString())) {
                replay.remove();
                continue;
            }

            replay.ghost.updateTrackedPositionAndAngles(
                    playerLog.x == null ? replay.ghost.getX() : playerLog.x,
                    playerLog.y == null ? replay.ghost.getY() : playerLog.y,
                    playerLog.z == null ? replay.ghost.getZ() : playerLog.z,
                    playerLog.yaw == null ? replay.ghost.yaw : playerLog.yaw,
                    playerLog.pitch == null ? replay.ghost.pitch : playerLog.pitch, 1, true);
            replay.ghost.setHeadYaw(playerLog.yaw == null ? replay.ghost.yaw : playerLog.yaw);
            if (playerLog.pose != null) replay.ghost.setPose(playerLog.pose);
        }
    }


    /* =================*/

    private GhostEntity ghost = null;
    private Identifier lastWorld = null;
    private final GhostInfo ghostInfo;
    protected ReplayGhost(GhostInfo ghostInfo) {
        this.ghostInfo = ghostInfo;
    }

    private void summon(ClientWorld world, PlayerLog log) {
        GhostEntity entity = new GhostEntity(GhostRunner.GHOST_ENTITY_TYPE, world);
        entity.refreshPositionAndAngles(log.x == null ? 0 : log.x, log.y == null ? 0 : log.y, log.z == null ? 0 : log.z, 0f, 0f);
        entity.setTargetSkinUuid(ghostInfo.getGhostData().getGhostUserUuid());
        world.addEntity(entity.getEntityId(), entity);
        ghost = entity;
    }

    private void remove() {
        if (ghost != null) {
            ghost.remove();
            ghost = null;
        }
    }
}
