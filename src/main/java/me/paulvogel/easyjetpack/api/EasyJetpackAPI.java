package me.paulvogel.easyjetpack.api;

import me.paulvogel.easyjetpack.JetpackManager;

public class EasyJetpackAPI {
    private static JetpackManager mgr = null;

    public EasyJetpackAPI(JetpackManager manager) {
        if (mgr != null) {
            throw new IllegalStateException(
                    "EasyJetpackAPI can only be instanced once!");
        }
        EasyJetpackAPI.mgr = manager;
    }

    /**
     * Obtains a instance of the JetpackManager, for plugins to register and
     * modify Jetpacks
     *
     * @return A instance of the JetpackManager
     * @throws NullPointerException Thrown if the plugin hasn't been enabled yet
     */
    public static JetpackManager getManager() {
        if (mgr == null) {
            throw new IllegalStateException(
                    "EasyJetpack hasn't been loaded yet!");
        }
        return mgr;
    }
}
