package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:VerlanTest.class */
class VerlanTest extends HiddenTest {
    VerlanTest() {
    }

    private void _test(Program program, String str, String str2) {
        provideInput(program, str);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str2));
        program.algorithm();
    }

    void testLoucheIsChelou(Program program) {
        _test(program, "louche", "chelou\n");
    }

    void testBonbonIsBonbon(Program program) {
        _test(program, "bonbon", "bonbon\n");
    }

    /* renamed from: testContrétIsTrécon, reason: contains not printable characters */
    void m9testContrtIsTrcon(Program program) {
        _test(program, "contré", "trécon\n");
    }
}
