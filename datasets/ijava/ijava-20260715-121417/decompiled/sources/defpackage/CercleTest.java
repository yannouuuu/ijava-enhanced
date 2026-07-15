package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:CercleTest.class */
class CercleTest extends HiddenTest {
    CercleTest() {
    }

    void test_algorithm(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("circonference(1) = 6.28318, diametre(1) = 2.0, volume(1) = 4.188786666666666.\ncirconference(2) = 12.56636, diametre(2) = 4.0, volume(2) = 33.51029333333333.\ncirconference(3) = 18.849539999999998, diametre(3) = 6.0, volume(3) = 113.09723999999999.\ncirconference(4) = 25.13272, diametre(4) = 8.0, volume(4) = 268.08234666666664.\ncirconference(5) = 31.4159, diametre(5) = 10.0, volume(5) = 523.5983333333332.\ncirconference(6) = 37.699079999999995, diametre(6) = 12.0, volume(6) = 904.7779199999999.\ncirconference(7) = 43.98226, diametre(7) = 14.0, volume(7) = 1436.7538266666666.\ncirconference(8) = 50.26544, diametre(8) = 16.0, volume(8) = 2144.658773333333.\ncirconference(9) = 56.54862, diametre(9) = 18.0, volume(9) = 3053.6254799999997.\ncirconference(10) = 62.8318, diametre(10) = 20.0, volume(10) = 4188.786666666666.\ncirconference(11) = 69.11498, diametre(11) = 22.0, volume(11) = 5575.275053333333.\ncirconference(12) = 75.39815999999999, diametre(12) = 24.0, volume(12) = 7238.223359999999.\ncirconference(13) = 81.68133999999999, diametre(13) = 26.0, volume(13) = 9202.764306666666.\ncirconference(14) = 87.96452, diametre(14) = 28.0, volume(14) = 11494.030613333332.\ncirconference(15) = 94.2477, diametre(15) = 30.0, volume(15) = 14137.154999999999.\nSoit un total de 90 multiplications.\n"));
        program.algorithm();
    }
}
