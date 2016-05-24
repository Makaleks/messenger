package arhangel.dim.core.messages;

import java.util.List;
/**
 * Created by Алексей on 22.05.2016.
 */

public class ChatCreateMessage extends Message {
    private List<Long> userIdList;
    private Type type;

    public ChatCreateMessage(List<Long> userIdList) {
        this.userIdList = userIdList;
        this.type = Type.MSG_CHAT_CREATE;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }
}
