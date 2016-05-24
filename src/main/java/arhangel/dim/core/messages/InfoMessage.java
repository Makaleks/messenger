package arhangel.dim.core.messages;

import java.util.Objects;
/**
 * Created by Алексей on 20.05.2016.
 */

public class InfoMessage extends Message {
    private long usrId;

    public long getUsrId() {
        return this.usrId;
    }

    public void setUsrId(long usrId) {
        this.usrId = usrId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String toString() {
        return "InfoMessage{" +
                "id='" + getId() + '\'' +
                '}';
    }
}
