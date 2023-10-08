import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FibonacciTest {

    @Test
    public void testFibonacci() {
        int cases[][] = {{0, 0}, {1, 1}};
        for (int i = 0; i < cases.length; i++) {
            assertEquals(cases[i][1], fib(cases[i][0]));
        }
    }

    private int fib(int i) {
        if (i == 0) return 0;

        return 1;
    }
}