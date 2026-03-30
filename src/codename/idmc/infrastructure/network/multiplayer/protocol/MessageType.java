package codename.idmc.infrastructure.network.multiplayer.protocol;

public enum MessageType {

    JOIN_LOBBY,
    LEAVE_LOBBY,

    CHAT_MESSAGE,
    CURSOR_MOVED,

    UPDATE_PLAYER,
    UPDATE_GAME_SETTINGS,

    LOBBY_STATE,
    START_GAME,

    ERROR
}