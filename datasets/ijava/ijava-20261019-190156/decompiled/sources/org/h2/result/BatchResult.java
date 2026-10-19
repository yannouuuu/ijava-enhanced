package org.h2.result;

import java.sql.SQLException;
import java.util.List;

/* loaded from: ijava.jar:org/h2/result/BatchResult.class */
public class BatchResult {
    private final long[] updateCounts;
    private final ResultInterface generatedKeys;
    private final List<SQLException> exceptions;

    public BatchResult(long[] jArr, ResultInterface resultInterface, List<SQLException> list) {
        this.updateCounts = jArr;
        this.generatedKeys = resultInterface;
        this.exceptions = list;
    }

    public long[] getUpdateCounts() {
        return this.updateCounts;
    }

    public ResultInterface getGeneratedKeys() {
        return this.generatedKeys;
    }

    public List<SQLException> getExceptions() {
        return this.exceptions;
    }
}
