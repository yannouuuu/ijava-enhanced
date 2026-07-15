package org.h2.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* loaded from: ijava.jar:org/h2/util/AbbaDetector.class */
public class AbbaDetector {
    private static final boolean TRACE = false;
    private static final ThreadLocal<Deque<Object>> STACK = ThreadLocal.withInitial(ArrayDeque::new);
    private static final Map<Object, Map<Object, Exception>> LOCK_ORDERING = new WeakHashMap();
    private static final Set<String> KNOWN_DEADLOCKS = new HashSet();

    /* renamed from: org.h2.util.AbbaDetector$1, reason: invalid class name */
    /* loaded from: ijava.jar:org/h2/util/AbbaDetector$1.class */
    class AnonymousClass1 extends SecurityManager {
        Class<?> clazz = getClassContext()[2];

        AnonymousClass1() {
        }
    }

    public static Object begin(Object obj) {
        if (obj == null) {
            obj = new AnonymousClass1().clazz;
        }
        Deque<Object> deque = STACK.get();
        if (!deque.isEmpty()) {
            if (deque.contains(obj)) {
                return obj;
            }
            while (!deque.isEmpty() && !Thread.holdsLock(deque.peek())) {
                deque.pop();
            }
        }
        if (!deque.isEmpty()) {
            markHigher(obj, deque);
        }
        deque.push(obj);
        return obj;
    }

    private static Object getTest(Object obj) {
        return obj;
    }

    private static String getObjectName(Object obj) {
        return obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj);
    }

    private static synchronized void markHigher(Object obj, Deque<Object> deque) {
        Exception exc;
        Object test = getTest(obj);
        Map<Object, Exception> map = LOCK_ORDERING.get(test);
        if (map == null) {
            map = new WeakHashMap();
            LOCK_ORDERING.put(test, map);
        }
        Exception exc2 = null;
        Iterator<Object> it = deque.iterator();
        while (it.hasNext()) {
            Object test2 = getTest(it.next());
            if (test2 != test) {
                Map<Object, Exception> map2 = LOCK_ORDERING.get(test2);
                if (map2 != null && (exc = map2.get(test)) != null) {
                    String str = test.getClass() + " " + test2.getClass();
                    if (!KNOWN_DEADLOCKS.contains(str)) {
                        RuntimeException runtimeException = new RuntimeException(getObjectName(test) + " synchronized after \n " + getObjectName(test2) + ", but in the past before");
                        runtimeException.initCause(exc);
                        runtimeException.printStackTrace(System.out);
                        KNOWN_DEADLOCKS.add(str);
                    }
                }
                if (!map.containsKey(test2)) {
                    if (exc2 == null) {
                        exc2 = new Exception("Before");
                    }
                    map.put(test2, exc2);
                }
            }
        }
    }
}
