package arhangel.dim.core.messages;
/**
 * Created by Алексей on 22.05.2016.
 */

public class ChatListMessage extends Message {

    private Type type;
    private Long senderId;

    public ChatListMessage(Long userId) {
        this.senderId = userId;
        this.type = Type.MSG_CHAT_LIST;
    }

    public Long getUserId() {
        return senderId;
    }

    public void setUserId(Long userId) {
        this.senderId = userId;
    }
}
