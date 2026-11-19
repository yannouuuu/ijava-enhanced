package org.h2.engine;

import org.h2.api.JavaObjectSerializer;
import org.h2.util.TimeZoneProvider;
import org.h2.value.ValueTimestampTimeZone;

/* loaded from: ijava.jar:org/h2/engine/CastDataProvider.class */
public interface CastDataProvider {
    ValueTimestampTimeZone currentTimestamp();

    TimeZoneProvider currentTimeZone();

    Mode getMode();

    JavaObjectSerializer getJavaObjectSerializer();

    boolean zeroBasedEnums();
}
