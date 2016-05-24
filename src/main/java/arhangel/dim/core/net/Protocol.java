package arhangel.dim.core.net;

import arhangel.dim.core.messages.Message;
import java.io.IOException;

/**
 *
 */
public interface Protocol {

    Message decode(byte[] bytes) throws ProtocolException;

    byte[] encode(Message msg) throws ProtocolException, IOException;

}
