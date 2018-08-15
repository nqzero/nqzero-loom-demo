


public class HelloCo {
    static final ContinuationScope SCOPE = new ContinuationScope() {};

    int val;
    
    Continuation continuation = new Continuation(SCOPE, () -> {
        for (val=0; val < 100; val++) {
            Continuation.yield(SCOPE);
        }
    });


    void run() {
        while (! continuation.isDone()) continuation.run();
    }
    
    public static void main(String[] args) {
        new HelloCo().run();
    }
    
}
