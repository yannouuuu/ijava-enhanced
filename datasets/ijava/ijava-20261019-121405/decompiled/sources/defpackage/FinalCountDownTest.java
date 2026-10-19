package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:FinalCountDownTest.class */
class FinalCountDownTest extends HiddenTest {
    FinalCountDownTest() {
    }

    void test_premiers_nombres_du_decompte(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("1000 995 990 985 980 975 970 965 960 955 950 945 940 935 930 925 920 915 910 905 900 895 890 885 880 875 870 865 860"));
        program.algorithm();
    }
}
