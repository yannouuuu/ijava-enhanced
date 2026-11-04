package org.h2.util;

import java.io.IOException;
import java.net.Socket;
import jdk.net.ExtendedSocketOptions;

/* loaded from: ijava.jar:org/h2/util/Utils10.class */
public final class Utils10 {
    public static boolean getTcpQuickack(Socket socket) throws IOException {
        return ((Boolean) socket.getOption(ExtendedSocketOptions.TCP_QUICKACK)).booleanValue();
    }

    public static boolean setTcpQuickack(Socket socket, boolean z) {
        try {
            socket.setOption(ExtendedSocketOptions.TCP_QUICKACK, Boolean.valueOf(z));
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private Utils10() {
    }
}
