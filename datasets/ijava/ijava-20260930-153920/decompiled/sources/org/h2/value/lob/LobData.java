package org.h2.value.lob;

import java.io.InputStream;
import org.h2.store.DataHandler;
import org.h2.value.ValueLob;

/* loaded from: ijava.jar:org/h2/value/lob/LobData.class */
public abstract class LobData {
    public abstract InputStream getInputStream(long j);

    public DataHandler getDataHandler() {
        return null;
    }

    public boolean isLinkedToTable() {
        return false;
    }

    public void remove(ValueLob valueLob) {
    }

    public int getMemory() {
        return 140;
    }
}
