package org.h2.command.dml;

import java.util.HashSet;
import org.h2.command.query.AllColumnsForPlan;
import org.h2.engine.DbObject;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.expression.ExpressionVisitor;
import org.h2.table.TableFilter;

/* loaded from: ijava.jar:org/h2/command/dml/Delete.class */
public final class Delete extends FilteredDataChangeStatement {
    @Override // org.h2.command.dml.FilteredDataChangeStatement
    public /* bridge */ /* synthetic */ void setFetch(Expression expression) {
        super.setFetch(expression);
    }

    public Delete(SessionLocal sessionLocal) {
        super(sessionLocal);
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0105, code lost:
    
        if (r0.fireBeforeRow(r6.session, r15, null) == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0065, code lost:
    
        if (r0 < 0) goto L8;
     */
    @Override // org.h2.command.dml.DataChangeStatement
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long update(org.h2.result.ResultTarget r7, org.h2.table.DataChangeDeltaTable.ResultOption r8) {
        /*
            Method dump skipped, instructions count: 428
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.h2.command.dml.Delete.update(org.h2.result.ResultTarget, org.h2.table.DataChangeDeltaTable$ResultOption):long");
    }

    @Override // org.h2.command.Prepared
    public StringBuilder getPlanSQL(StringBuilder sb, int i) {
        this.targetTableFilter.getPlanSQL(sb.append("DELETE FROM "), false, i);
        return appendFilterCondition(sb, i);
    }

    @Override // org.h2.command.dml.DataChangeStatement
    void doPrepare() {
        if (this.condition != null) {
            this.condition.mapColumns(this.targetTableFilter, 0, 0);
            this.condition = this.condition.optimizeCondition(this.session);
            if (this.condition != null) {
                this.condition.createIndexConditions(this.session, this.targetTableFilter);
            }
        }
        TableFilter[] tableFilterArr = {this.targetTableFilter};
        this.targetTableFilter.setPlanItem(this.targetTableFilter.getBestPlanItem(this.session, tableFilterArr, 0, new AllColumnsForPlan(tableFilterArr)));
        this.targetTableFilter.prepare();
    }

    @Override // org.h2.command.Prepared
    public int getType() {
        return 58;
    }

    @Override // org.h2.command.dml.DataChangeStatement
    public String getStatementName() {
        return "DELETE";
    }

    @Override // org.h2.command.Prepared
    public void collectDependencies(HashSet<DbObject> hashSet) {
        ExpressionVisitor dependenciesVisitor = ExpressionVisitor.getDependenciesVisitor(hashSet);
        if (this.condition != null) {
            this.condition.isEverything(dependenciesVisitor);
        }
    }
}
