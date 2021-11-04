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
    public static void getPlayerSkin(UUID uuid) {
        skins.getOrDefault(uuid, DefaultSkinHelper.getTexture());
    }

    //인게임 고스트 엔티티 목록
    private static final HashMap<GhostInfo, GhostEntity> ghosts = new HashMap<>();
    public static void clearAllGhosts() {
        ghosts.clear();
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

    public static void sendBestCheckPointMessage(Timeline.Moment moment) {
        //if (!Options.getCheckPointToggle()) return;

        long bestTime = 0;
        for (GhostInfo ghostInfo : ghosts.keySet()) {
            long time = ghostInfo.getCheckPoint(moment);
            if (time != 0 && (bestTime == 0 || bestTime > time)) {
                bestTime = time;
            }
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        long nowTime = InGameTimer.INSTANCE.getInGameTime();
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

    public static void insertBrains(Long seed) {
        clearAllGhosts();

        if (!selectedGhosts.containsKey(seed) || selectedGhosts.get(seed).isEmpty()) {
            return;
        }

        for (UUID uuid : selectedGhosts.get(seed)) {
            GhostInfo ghostData = GhostInfo.fromData(uuid);
            if (ghostData.getGhostData().getSeed() == seed && Objects.equals(ghostData.getGhostData().getClientVersion(), GhostRunner.CLIENT_VERSION)) {
                ghosts.put(ghostData, null);
                Utils.downloadPlayerSkin(MinecraftClient.getInstance(), ghostData.getGhostData().getGhostUserUuid());
            }
        }
    }

    public static void tickGhost() {
        if (ghosts.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.player.clientWorld == null) return;
        ClientWorld world = client.player.clientWorld;

        ghosts.forEach((ghostInfo, ghostEntity) -> {
            PlayerLog playerLog = ghostInfo.pollPlayerLog();
            if (playerLog == null) {
                if (ghostEntity != null) ghostEntity.remove();
                ghosts.put(ghostInfo, null);
            } else {
                playerLog.world = playerLog.world != null ? playerLog.world : ghostEntity != null ? ghostEntity.world.getRegistryKey().getValue() : null;
                if (ghostEntity == null) {
                    GhostEntity ghost = summon(world, ghostInfo.getGhostData().getGhostUserUuid(), playerLog);
                    ghost.setInvisible(true);
                    ghosts.put(ghostInfo, ghost);
                } else {
                    if (ghostEntity.world.getDimension() != world.getDimension()) {
                        ghostEntity.remove();
                        ghosts.put(ghostInfo, summon(world, ghostInfo.getGhostData().getGhostUserUuid(), playerLog));
                    } else {
                        if (playerLog.world != world.getRegistryKey().getValue()) {
                            ghostEntity.setInvisible(true);
                        } else {
                            ghostEntity.setInvisible(false);
                            ghostEntity.updateTrackedPositionAndAngles(
                                    playerLog.x == null ? ghostEntity.getX() : playerLog.x,
                                    playerLog.y == null ? ghostEntity.getY() : playerLog.y,
                                    playerLog.z == null ? ghostEntity.getZ() : playerLog.z,
                                    playerLog.yaw == null ? ghostEntity.yaw : playerLog.yaw,
                                    playerLog.pitch == null ? ghostEntity.pitch : playerLog.pitch, 1, true);
                            ghostEntity.setHeadYaw(playerLog.yaw == null ? ghostEntity.yaw : playerLog.yaw);
                            if (playerLog.pose != null) ghostEntity.setPose(playerLog.pose);
                        }
                    }
                }
            }
        });
    }

    private static GhostEntity summon(ClientWorld world, UUID targetUUID, PlayerLog log) {
        GhostEntity entity = new GhostEntity(GhostRunner.GHOST_ENTITY_TYPE, world);
        entity.refreshPositionAndAngles(log.x == null ? 0 : log.x, log.y == null ? 0 : log.y, log.z == null ? 0 : log.z, 0f, 0f);
        entity.setTargetSkinUuid(targetUUID);
        entity.setInvisible(true);
        world.addEntity(entity.getEntityId(), entity);
        return entity;
    }
}
