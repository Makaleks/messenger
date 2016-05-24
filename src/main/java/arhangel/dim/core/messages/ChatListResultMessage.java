package arhangel.dim.core.messages;

import java.util.List;
/**
 * Created by Алексей on 22.05.2016.
 */

public class ChatListResultMessage extends Message {
    private List<Long> chatsIdList;
    private Type type;

    public void setChatsIdList(List<Long> chatsIdList) {
        this.chatsIdList = chatsIdList;
    }

    public List<Long> getChatsIdList() {
        return chatsIdList;
    }

    public ChatListResultMessage(List<Long> chatsIdList) {
        this.chatsIdList = chatsIdList;
        type = Type.MSG_CHAT_LIST_RESULT;
    }
}
