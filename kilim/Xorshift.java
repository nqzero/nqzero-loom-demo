/* Copyright (c) 2016, nqzero
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */



/*
    from wikipedia:
    uint64_t x = s[0];
    uint64_t const y = s[1];
    s[0] = y;
    x ^= x << 23; // a
    s[1] = x ^ y ^ (x >> 17) ^ (y >> 26); // b, c
    return s[1] + y;            
*/




/**
 *  a demo and benchmark of kilim continuations used to implement the xorshift PRNG
 *  runs xorshift a number of cycles, printing the nanos per cycle and the xor of the result
 *  the value is printed just to ensure that the JIT actually runs all the code
 *  https://en.wikipedia.org/wiki/Xorshift#xorshift.2B
 */
public class Xorshift extends kilim.Continuation {


    long result;

    void warmup(long num) {
        long warmup = 5000000000L;
        final long start = System.nanoTime();
        long dummy = 0;
        while (System.nanoTime() - start < warmup)
            dummy = dummy ^ loop(num);
        System.out.println("warmup: " + dummy);
    }
    void cycle(long num) {
        final long start = System.nanoTime();
        long val = loop(num);
        long duration = System.nanoTime() - start;
        System.out.format("kilim: %-10.2f nanos/op, %30d\n", 1.0*duration/num, val);
    }
    public long loop(long num) {
        long val = 0;
        for (int ii=0; ii < num; ii++) {
            run();
            val = val ^ result;
        }
        return val;
    }
    public void execute() throws kilim.Pausable {
        long x, y, s0=103, s1=17;
        while (true) {
            x = s0;
            y = s1;
            s0 = y;
            x ^= (x << 23);
            s1 = x ^ y ^ (x >> 17) ^ (y >> 26);
            result = (s1 + y);
            kilim.Fiber.yield();
        }
    }

    public static void main(String[] args) {
        if (kilim.tools.Kilim.trampoline(false,args)) return;
        long cycles = 200000;
        int reps = 10;
        if (args.length == 0) {
            System.out.println("args: number of cycles, number of repeats");
            System.out.format("\t no args provided using defaults: %d %d\n",cycles,reps);
        }
        try { cycles = Long.parseLong(args[0]); } catch (Exception ex) {}
        try { reps = Integer.parseInt(args[1]); } catch (Exception ex) {}

        new Xorshift().warmup(cycles);
        Xorshift xor = new Xorshift();
        
        for (int jj=0; jj < reps; jj++)
            xor.cycle(cycles);
    }
}
