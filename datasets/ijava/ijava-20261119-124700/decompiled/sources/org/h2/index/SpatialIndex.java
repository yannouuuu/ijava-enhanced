package org.h2.index;

import org.h2.engine.SessionLocal;
import org.h2.result.SearchRow;

/* loaded from: ijava.jar:org/h2/index/SpatialIndex.class */
public interface SpatialIndex {
    Cursor findByGeometry(SessionLocal sessionLocal, SearchRow searchRow, SearchRow searchRow2, boolean z, SearchRow searchRow3);
}
