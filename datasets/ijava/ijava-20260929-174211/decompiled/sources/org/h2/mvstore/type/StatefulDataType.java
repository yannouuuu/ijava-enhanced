package org.h2.mvstore.type;

import java.nio.ByteBuffer;
import org.h2.mvstore.WriteBuffer;

/* loaded from: ijava.jar:org/h2/mvstore/type/StatefulDataType.class */
public interface StatefulDataType<D> {

    /* loaded from: ijava.jar:org/h2/mvstore/type/StatefulDataType$Factory.class */
    public interface Factory<D> {
        DataType<?> create(ByteBuffer byteBuffer, MetaType<D> metaType, D d);
    }

    void save(WriteBuffer writeBuffer, MetaType<D> metaType);

    Factory<D> getFactory();
}
