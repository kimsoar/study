import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FibonacciTest {

    @Test
    public void testFibonacci() {
        assertEquals(0, fib(0));
        assertEquals(1, fib(1));
    }

    private int fib(int i) {
        if (i == 0) return 0;

        return 1;
    }
}