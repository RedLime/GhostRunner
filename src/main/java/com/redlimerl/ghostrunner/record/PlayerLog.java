package com.redlimerl.ghostrunner.record;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class PlayerLog {

    EntityPose pose;
    Identifier world;
    Double x;
    Double y;
    Double z;
    Float pitch;
    Float yaw;

    private static <T extends Number & Comparable<? super T>> String roundUp(T value) {
        return value == null ? "" : String.format("%.2f", value.doubleValue());
    }

    private static <T extends Number & Comparable<? super T>> String roundUp(T value, T value2) {
        return value == null || value.equals(value2) ? "" : String.format("%.2f", value.doubleValue());
    }

    public static PlayerLog of(String string) {
        String[] data = string.split("\\|");
        if (data.length < 7) return new PlayerLog(null, null, null, null, null, null, null);

        return new PlayerLog(
                Objects.equals(data[0], "") ? null : EntityPose.valueOf(data[0]),
                Objects.equals(data[1], "") ? null : new Identifier(data[1]),
                Objects.equals(data[2], "") ? null : Double.valueOf(data[2]),
                Objects.equals(data[3], "") ? null : Double.valueOf(data[3]),
                Objects.equals(data[4], "") ? null : Double.valueOf(data[4]),
                Objects.equals(data[5], "") ? null : Float.valueOf(data[5]),
                Objects.equals(data[6], "") ? null : Float.valueOf(data[6])
        );
    }

    public PlayerLog(EntityPose pose, Identifier world, Double x, Double y, Double z, Float pitch, Float yaw) {
        this.pose = pose;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public PlayerLog(PlayerEntity player) {
        this.pose = player.getPose();
        this.world = player.world.getRegistryKey().getValue();
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.pitch = player.pitch;
        this.yaw = player.yaw;
    }

    @Override
    public String toString() {
        return (pose == null ? "" : pose.name()) + "|" +
                (world == null ? "" : world.getPath()) + "|" +
                roundUp(x) + "|" + roundUp(y) + "|" + roundUp(z) + "|" +
                roundUp(pitch) + "|" + roundUp(yaw);
    }

    public String toString(PlayerLog target) {
        return (pose == null || Objects.equals(target.pose, pose) ? "" : pose.name()) + "|" +
                (world == null || Objects.equals(target.world, world) ? "" : world.getPath()) + "|" +
                roundUp(x) + "|" + roundUp(y) + "|" + roundUp(z) + "|" +
                roundUp(pitch) + "|" + roundUp(yaw);
    }
}
