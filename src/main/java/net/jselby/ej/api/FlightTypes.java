package net.jselby.ej.api;

/**
 * Enum containing various movement types
 *
 * @author James
 */
public enum FlightTypes {
    /**
     * Jetpack toggle via creative mode
     *
     * @Deprecated Not implemented yet
     */
    @Deprecated
    CREATIVE_TOGGLE,
    /**
     * Jetpack toggle via pressing crouch
     */
    CROUCH,
    /**
     * Jetpack toggle via holding crouch
     */
    CROUCH_CONSTANT,
    /**
     * Generally for fall boots, allows these to cancel fall damage
     */
    FALLING,
    /**
     * Called on the onInteractEvent
     */
    INTERACT,
    /**
     * Called when you are damaged by a player
     */
    DAMAGED_BY_PLAYER,
    /**
     * Called when you are damaged by a mob
     */
    DAMAGED_BY_MOB,
    /**
     * Called when you are drowning
     */
    DROWNING,
    /**
     * Called when a player completes a Anvil transaction
     */
    ANVIL,
    /**
     * Called for jetpacks requiring a timer
     */
    TIMER
}
