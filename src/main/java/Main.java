import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyRunnable implements Runnable {
    private int i;
    private Kalah kalah;
    private Board board;
    private Side side;
    private List<Integer> results;

    public MyRunnable(int i, Kalah kalah, Board board, Side side, List<Integer> results) {
        this.i = i;
        this.kalah = kalah;
        this.board = board;
        this.side = side;
        this.results = results;
    }

    private int simulateNextMoves(int i, Kalah kalah, Board board, Side side) {
        // aici vine tot codul din simulateNextMoves din python
        return 0;
    }

    @Override
    public void run() {
        int result = simulateNextMoves(this.i, this.kalah, this.board, this.side);

        // IMPORTANT adaugarea rezultatului in mod sincronizat
        synchronized(this.results) {
            this.results.add(result);
        }

        try {
            Main.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    private static ExecutorService executor = Executors.newFixedThreadPool(7);
    public static CyclicBarrier barrier = new CyclicBarrier(8); // numarul de threaduri (7) + 1

    private static Move calculateNextBestMove(Board board, Side side) {
        List<Integer> results = Collections.synchronizedList(new ArrayList<Integer>()); // IMPORTANT

        Kalah kalah = new Kalah(); // Kalah(board) din python

        for (int i = 1; i <= 7; i++) {
            Main.executor.execute(new Thread(new MyRunnable(i, kalah, board, side, results)));
        }

        // IMPORTANT asteptam ca toate threadurile sa-si termine treaba
        try {
            Main.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        // IMPORTANT resetam bariera
        Main.barrier.reset();

        // sorteaza results....

        // elementul cel mai bun din results trebuie returnat
        return null;
    }

    public static void main(String[] args) {
        System.out.println("salut");
        Board board = new Board();
        Side side = new Side();

        while (true) {
            // codul din main...
            Move move = calculateNextBestMove(board, side);

            // bla bla bla
            break;
        }
    }
}
