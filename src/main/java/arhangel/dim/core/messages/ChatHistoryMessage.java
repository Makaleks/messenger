package arhangel.dim.core.messages;

/**
 * Created by Алексей on 22.05.2016.
 */

public class ChatHistoryMessage extends Message {
    private Long chatId;
    private Type type;

    public ChatHistoryMessage(Long chatId) {
        this.chatId = chatId;
        type = Type.MSG_CHAT_HIST;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
