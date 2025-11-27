package defpackage;

import extensions.File;

/* loaded from: ijava.jar:DumpCSVTest.class */
class DumpCSVTest extends HiddenTest {
    DumpCSVTest() {
    }

    void test_DumpCSV_dump_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "dump", new Class[]{File.class}, program);
    }
}
