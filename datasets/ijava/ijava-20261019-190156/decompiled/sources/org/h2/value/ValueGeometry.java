package org.h2.value;

import org.h2.api.ErrorCode;
import org.h2.message.DbException;
import org.h2.util.Bits;
import org.h2.util.MathUtils;
import org.h2.util.StringUtils;
import org.h2.util.geometry.EWKBUtils;
import org.h2.util.geometry.EWKTUtils;
import org.h2.util.geometry.GeometryUtils;
import org.h2.util.geometry.JTSUtils;
import org.locationtech.jts.geom.Geometry;

/* loaded from: ijava.jar:org/h2/value/ValueGeometry.class */
public final class ValueGeometry extends ValueBytesBase {
    private static final double[] UNKNOWN_ENVELOPE = new double[0];
    private final int typeAndDimensionSystem;
    private final int srid;
    private double[] envelope;
    private Object geometry;

    private ValueGeometry(byte[] bArr, double[] dArr) {
        super(bArr);
        if (bArr.length < 9 || bArr[0] != 0) {
            throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, StringUtils.convertBytesToHex(bArr));
        }
        this.value = bArr;
        this.envelope = dArr;
        int i = Bits.INT_VH_BE.get(bArr, 1);
        this.srid = (i & EWKBUtils.EWKB_SRID) != 0 ? Bits.INT_VH_BE.get(bArr, 5) : 0;
        this.typeAndDimensionSystem = ((i & 65535) % 1000) + (EWKBUtils.type2dimensionSystem(i) * 1000);
    }

    public static ValueGeometry getFromGeometry(Object obj) {
        try {
            return (ValueGeometry) Value.cache(new ValueGeometry(JTSUtils.geometry2ewkb((Geometry) obj), UNKNOWN_ENVELOPE));
        } catch (RuntimeException e) {
            throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, String.valueOf(obj));
        }
    }

    public static ValueGeometry get(String str) {
        try {
            return (ValueGeometry) Value.cache(new ValueGeometry(EWKTUtils.ewkt2ewkb(str), UNKNOWN_ENVELOPE));
        } catch (RuntimeException e) {
            throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, str);
        }
    }

    public static ValueGeometry get(byte[] bArr) {
        return (ValueGeometry) Value.cache(new ValueGeometry(bArr, UNKNOWN_ENVELOPE));
    }

    public static ValueGeometry getFromEWKB(byte[] bArr) {
        try {
            return (ValueGeometry) Value.cache(new ValueGeometry(EWKBUtils.ewkb2ewkb(bArr), UNKNOWN_ENVELOPE));
        } catch (RuntimeException e) {
            throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, StringUtils.convertBytesToHex(bArr));
        }
    }

    public static Value fromEnvelope(double[] dArr) {
        if (dArr != null) {
            return Value.cache(new ValueGeometry(EWKBUtils.envelope2wkb(dArr), dArr));
        }
        return ValueNull.INSTANCE;
    }

    public Geometry getGeometry() {
        if (this.geometry == null) {
            try {
                this.geometry = JTSUtils.ewkb2geometry(this.value, getDimensionSystem());
            } catch (RuntimeException e) {
                throw DbException.convert(e);
            }
        }
        return ((Geometry) this.geometry).copy();
    }

    public int getTypeAndDimensionSystem() {
        return this.typeAndDimensionSystem;
    }

    public int getGeometryType() {
        return this.typeAndDimensionSystem % 1000;
    }

    public int getDimensionSystem() {
        return this.typeAndDimensionSystem / 1000;
    }

    public int getSRID() {
        return this.srid;
    }

    public double[] getEnvelopeNoCopy() {
        if (this.envelope == UNKNOWN_ENVELOPE) {
            GeometryUtils.EnvelopeTarget envelopeTarget = new GeometryUtils.EnvelopeTarget();
            EWKBUtils.parseEWKB(this.value, envelopeTarget);
            this.envelope = envelopeTarget.getEnvelope();
        }
        return this.envelope;
    }

    public boolean intersectsBoundingBox(ValueGeometry valueGeometry) {
        return GeometryUtils.intersects(getEnvelopeNoCopy(), valueGeometry.getEnvelopeNoCopy());
    }

    public Value getEnvelopeUnion(ValueGeometry valueGeometry) {
        return fromEnvelope(GeometryUtils.union(getEnvelopeNoCopy(), valueGeometry.getEnvelopeNoCopy()));
    }

    @Override // org.h2.value.Value, org.h2.value.Typed
    public TypeInfo getType() {
        return TypeInfo.TYPE_GEOMETRY;
    }

    @Override // org.h2.value.Value
    public int getValueType() {
        return 37;
    }

    @Override // org.h2.value.ValueBytesBase, org.h2.util.HasSQL
    public StringBuilder getSQL(StringBuilder sb, int i) {
        sb.append("GEOMETRY ");
        if ((i & 8) != 0) {
            EWKBUtils.parseEWKB(this.value, new EWKTUtils.EWKTTarget(sb.append('\''), getDimensionSystem()));
            sb.append('\'');
        } else {
            super.getSQL(sb, 0);
        }
        return sb;
    }

    @Override // org.h2.value.Value
    public String getString() {
        return EWKTUtils.ewkb2ewkt(this.value, getDimensionSystem());
    }

    @Override // org.h2.value.ValueBytesBase, org.h2.value.Value
    public int getMemory() {
        return MathUtils.convertLongToInt((this.value.length * 20) + 24);
    }
}
