import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSerwer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(58901)) {
            System.out.println("Serwer Go aktywny");
            ExecutorService pool = Executors.newFixedThreadPool(100);
            while (true) {
                Serwer serwer_go = new Serwer();
                pool.execute(new Gracz(listener.accept(), 1, serwer_go)); //czarny
                pool.execute(new Gracz(listener.accept(), 2, serwer_go)); //bialy
            }
        }
    }
}
