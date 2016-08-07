package me.paulvogel.easyjetpack.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import me.paulvogel.easyjetpack.CheatPluginAdapter;
import net.gravitydevelopment.anticheat.api.AntiCheatAPI;
import net.gravitydevelopment.anticheat.check.CheckType;

public class AntiCheatImpl extends CheatPluginAdapter {

    

    @Override
    public void exemptPlayer(Player player, Type type) {
        switch (type) {
            case FLY:
                AntiCheatAPI.exemptPlayer(player, CheckType.FLY, null);
                break;
            case NOCLIP:
                AntiCheatAPI.exemptPlayer(player, CheckType.VCLIP, null);
                break;
        }
    }

    @Override
    public void unexemptPlayer(Player player, Type type) {
        switch (type) {
            case FLY:
                AntiCheatAPI.unexemptPlayer(player, CheckType.FLY, null);
                break;
            case NOCLIP:
                AntiCheatAPI.unexemptPlayer(player, CheckType.VCLIP, null);
                break;
        }
    }

    @Override
    public boolean isPluginEnabled() {
        return Bukkit.getServer().getPluginManager().getPlugin("AntiCheatReloaded") != null;
    }

    public boolean isExempted(Player player, Type type) {
        switch (type) {
            case FLY:
                return AntiCheatAPI.isExempt(player, CheckType.FLY);
            case NOCLIP:
                return AntiCheatAPI.isExempt(player, CheckType.VCLIP);
		default:
			break;
        }
        return false;
    }



    

}
