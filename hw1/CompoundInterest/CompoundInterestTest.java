import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(0, CompoundInterest.numYears(2022));
        assertEquals(10, CompoundInterest.numYears(2032));
        assertEquals(1, CompoundInterest.numYears(2023));
        assertEquals(100, CompoundInterest.numYears(2122));
    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).
        // The variable below can be used when you write your tests.
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2024), tolerance);
        assertEquals(7.744, CompoundInterest.futureValue(10, -12, 2024), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(295712.29, CompoundInterest.futureValueReal(1000000, 0, 2062, 3), tolerance);

    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(3641, CompoundInterest.totalSavings(1000, 2025, 10), tolerance);

    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(3439.94, CompoundInterest.totalSavingsReal(1000, 2025, 10, 3), tolerance);
        assertEquals(2136095.7, CompoundInterest.totalSavingsReal(10000, 2062, 10, 3), tolerance);
    }



    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
