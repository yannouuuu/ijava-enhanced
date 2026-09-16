package org.h2.api;

/* loaded from: ijava.jar:org/h2/api/JavaObjectSerializer.class */
public interface JavaObjectSerializer {
    byte[] serialize(Object obj) throws Exception;

    Object deserialize(byte[] bArr) throws Exception;
}
