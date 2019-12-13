import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    }
}

public class Main {
    private static Move calculateNextBestMove(Board board, Side side) {
        List<Integer> results = Collections.synchronizedList(new ArrayList<Integer>()); // IMPORTANT
        List<Thread> threads = new ArrayList<Thread>();
        Kalah kalah = new Kalah(); // Kalah(board) din python

        for (int i = 1; i <= 7; i++) {
            threads.add(new Thread(new MyRunnable(i, kalah, board, side, results)));
            threads.get(i - 1).start(); // pornim noul thread
        }

        for (int i = 0; i < 7; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
