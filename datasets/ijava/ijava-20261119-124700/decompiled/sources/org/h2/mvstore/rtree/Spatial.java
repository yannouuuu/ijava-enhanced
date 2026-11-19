package org.h2.mvstore.rtree;

/* loaded from: ijava.jar:org/h2/mvstore/rtree/Spatial.class */
public interface Spatial {
    float min(int i);

    void setMin(int i, float f);

    float max(int i);

    void setMax(int i, float f);

    Spatial clone(long j);

    long getId();

    boolean isNull();

    boolean equalsIgnoringId(Spatial spatial);
}
