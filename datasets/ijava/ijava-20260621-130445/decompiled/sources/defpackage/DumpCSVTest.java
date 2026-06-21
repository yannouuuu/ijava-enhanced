package defpackage;

import extensions.CSVFile;

/* loaded from: ijava.jar:DumpCSVTest.class */
class DumpCSVTest extends HiddenTest {
    DumpCSVTest() {
    }

    void test_DumpCSV_dump_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "dump", new Class[]{CSVFile.class}, program);
    }
}
