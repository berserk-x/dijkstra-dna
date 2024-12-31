import java.util.*;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


abstract class NeedlemanWunsch {
    protected final String seqA;
    protected final String seqB;

    public NeedlemanWunsch(String seqA, String seqB) {
        this.seqA = seqA;
        this.seqB = seqB;
    }

    abstract int score(int i, int j);
    abstract int penalty(boolean edge);

    public int align() {
        int m = seqA.length();
                int n = seqB.length();

                int[][] D = new int[m + 1][n + 1];

                for (int i = 0; i <= m; i++) {
                    D[i][0] = i * penalty(i == m);
                }

                for (int j = 0; j <= n; j++) {
                    D[0][j] = j * penalty(j == n);
                }

                for (int i = 1; i <= m; i++) {
                    for (int j = 1; j <= n; j++) {
                        int match = D[i - 1][j - 1] + score(i - 1, j - 1);
                        int delete = D[i - 1][j] + penalty(j == n); 
                        int insert = D[i][j - 1] + penalty(i == m);
                        D[i][j] = Math.max(match, Math.max(delete, insert));
                    }
                }

                return D[m][n];
    }
}

class Scheme3 extends NeedlemanWunsch {
    public Scheme3(String seqA, String seqB) {
        super(seqA, seqB);
    }

    @Override
    int score(int i, int j) {
        if (seqA.charAt(i) == seqB.charAt(j)) {
            return 3;
        } else {
            return -1;
        }
    }

    @Override
    int penalty(boolean edge) {
        if (edge) return 0; else return -2;
    }
}

public class lab8quiz {
    public static Optional<String> loadDNA(String filename) {
        Optional<String> answer = Optional.empty();
        try (Scanner scanner = new Scanner(new File(filename))) {
            answer = Optional.of(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public static void main(String[] args) {
       Optional<String> A = loadDNA("artyan.dna");
        Optional<String> B = loadDNA("mother.dna");

        A.ifPresentOrElse(
            seqA -> B.ifPresentOrElse(
            seqB -> {
                    Scheme3 scheme3 = new Scheme3(seqA, seqB);
                    int alignmentScore = scheme3.align();
                    System.out.println("Alignment Score: " + alignmentScore);
                    },
                    () -> System.out.println("Error loading sequence B.")
                   ),
                    () -> System.out.println("Error loading sequence A.")
               );
    }
}
