
import kilim.Fiber;
import kilim.Pausable;




public class XorDeep {
    Continuation ctus[];
    long result;
    int depth;
    boolean debug;
    int ctuNum;
    double sum;
    long times;

    void average() {
        System.out.format("==== kilim average : %-10.2f nanos/op%n", sum/times); 
    }
 
    XorDeep(int depth, int ctuNum, boolean debug) {
        this.depth = depth;
        this.ctuNum = ctuNum;
        this.debug = debug;
        ctus = new Continuation[ctuNum];
        for (int i = 0; i < ctuNum; i++) {
            ctus[i] = new Continuation();
        }
    }

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
        double tmp = 1.0*duration/num;
        sum += tmp;
        times++; 
        System.out.format("loom : %-10.2f nanos/op, %30d\n", tmp, val);
    }
    public long loop(long num) {
        long val = 0;
        for (int ii=0; ii < num; ii++) {
            ctus[ii%ctuNum].run();
            val = val ^ result;
        }
        return val;
    }

    class Continuation extends kilim.Continuation {
    public void execute() throws Pausable {
        //System.out.println("initial d: " + depth);
        recursiveCall(depth); 
    }
    void recursiveCall(int d) throws Pausable {
        //System.out.println("d: " + d);
        while(d > 0) {
            recursiveCall(--d);
        }
        execute2();
    }
    public void execute2() throws Pausable {
        if (debug) {
            new Throwable().printStackTrace();
        }
        long x, y, s0=103, s1=17;
        while (true) {
            x = s0;
            y = s1;
            s0 = y;
            x ^= (x << 23);
            s1 = x ^ y ^ (x >> 17) ^ (y >> 26);
            result = (s1 + y);
            Fiber.yield();
        }
    }
    }
    public static void main(String[] args) {
        if (kilim.tools.Kilim.trampoline(false,args)) return;

        long cycles = 200000;
        int reps = 10;
        int depth = 0;
        boolean debug = false;
        int ctuNum = 1;
        
        try { cycles = Long.parseLong(args[0]); } catch (Exception ex) {}
        try { reps = Integer.parseInt(args[1]); } catch (Exception ex) {}
        try { depth = Integer.parseInt(args[2]); } catch (Exception ex) {}
        try { ctuNum = Integer.parseInt(args[3]); } catch (Exception ex) {}
        try { debug = Boolean.parseBoolean(args[4]); } catch (Exception ex) {}

        if (args.length == 0) {
            System.out.println("args: number of cycles, number of repeats, depth of recursive calls, continuation number, debug");
            System.out.format("\t no args provided using defaults: %d %d %d %d %b\n",cycles, reps, depth, ctuNum, debug);
        } else {
            System.out.format("\t cycle: %d, reps: %d, depth: %d, ctuNum: %d, debug: %b\n",cycles, reps, depth, ctuNum, debug);
        }

        new XorDeep(depth, ctuNum, debug).warmup(cycles);
        XorDeep xor = new XorDeep(depth, ctuNum, debug);
        
        for (int jj=0; jj < reps; jj++) {
            //Xorshift xor = new Xorshift(depth, ctuNum, debug);
            xor.cycle(cycles);
        }
        xor.average();
    }
}
