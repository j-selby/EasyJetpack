package net.jselby.ej.impl;

import net.gravitydevelopment.anticheat.api.AntiCheatAPI;
import net.gravitydevelopment.anticheat.check.CheckType;
import net.jselby.ej.CheatPluginAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AntiCheatImpl extends CheatPluginAdapter {

    @Override
    public void exemptPlayer(Player player, Type type) {
        switch (type) {
            case FLY:
                AntiCheatAPI.exemptPlayer(player, CheckType.FLY);
                break;
            case NOCLIP:
                AntiCheatAPI.exemptPlayer(player, CheckType.VCLIP);
                break;
        }
    }

    @Override
    public void unexemptPlayer(Player player, Type type) {
        switch (type) {
            case FLY:
                AntiCheatAPI.unexemptPlayer(player, CheckType.FLY);
                break;
            case NOCLIP:
                AntiCheatAPI.unexemptPlayer(player, CheckType.VCLIP);
                break;
        }
    }

    @Override
    public boolean isPluginEnabled() {
        return Bukkit.getServer().getPluginManager().getPlugin("AntiCheat") != null;
    }

    @Override
    public boolean isExempted(Player player, Type type) {
        switch (type) {
            case FLY:
                return AntiCheatAPI.isExempt(player, CheckType.FLY);
            case NOCLIP:
                return AntiCheatAPI.isExempt(player, CheckType.VCLIP);
        }
        return false;
    }

}
