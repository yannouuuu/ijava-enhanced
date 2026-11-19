package org.h2.index;

import org.h2.result.Row;
import org.h2.result.SearchRow;

/* loaded from: ijava.jar:org/h2/index/Cursor.class */
public interface Cursor {
    Row get();

    SearchRow getSearchRow();

    boolean next();

    boolean previous();
}
