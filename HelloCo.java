


public class HelloCo {
    static final ContinuationScope SCOPE = new ContinuationScope() {};

    int val;
    
    Continuation continuation = new Continuation(SCOPE, () -> {
        for (val=0; val < 100; val++) {
            System.out.print("hello loom: ");
            Continuation.yield(SCOPE);
        }
    });


    void run() {
        while (! continuation.isDone()) {
            continuation.run();
            System.out.println(val);
        }
    }
    
    public static void main(String[] args) {
        new HelloCo().run();
    }
    
}
