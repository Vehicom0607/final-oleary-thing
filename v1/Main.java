import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int player_count = 8;

        // Create the two pairings
        Iterator<int[]> pairing_iterator = CombinatoricsUtils.combinationsIterator(player_count, 2);
        List<int[]> total_matchup_list = new ArrayList<>();
        while (pairing_iterator.hasNext()) {
            final int[] combination = pairing_iterator.next();
            total_matchup_list.add(combination);
        }

        System.out.println("matchup_list.size() = " + total_matchup_list.size());

        List<List<Round>> total_round_list = new ArrayList<>();

        for (int k = 1; k < player_count; k++) {

            // remove the first combo which is always 1 - k
            List<int[]> matchup_list = new ArrayList<>();
            for (int[] match : total_matchup_list) {
                if (match[0] != 0 && match[1] != 0 && match[0] != k && match[1] != k) {
                    matchup_list.add(match);
                }
            }
            if (k == 1) {
                System.out.println("matchup_list.size() = " + matchup_list.size());
            }

            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(matchup_list.size(), (player_count / 2) - 1);
            List<Round> round_list = new ArrayList<>();

            ProgressBar pb = new ProgressBar("Generating Round", (int) combinations(matchup_list.size(), (player_count / 2) - 1));
            pb.start();
            while (iterator.hasNext()) {

                final int[] combination = iterator.next();

                int[] current_round = new int[player_count];
                current_round[0] = 0;
                current_round[1] = k;
                for (int j = 0; j < combination.length; j++) {
                    current_round[2 + 2 * j] = matchup_list.get(combination[j])[0];
                    current_round[3 + 2 * j] = matchup_list.get(combination[j])[1];
                }
                Round new_round = new Round(current_round);
                if (new_round.checkValidity()) {
                    round_list.add(new_round);
                }
                pb.step();
            }
            pb.stop();



            total_round_list.add(round_list);
        }

        long end = System.currentTimeMillis();
        System.out.print("Possibility Generation time is " + ((end - start) / 1000d) + " seconds");

        // Try each combo
        /*
        List<Round> solutions = new ArrayList<>();
        int solutions_per_round = total_round_list.get(0).size();
        Round test_round = total_round_list.get(0).get(0);
        for (int i = 0; i < total_round_list.size(); i++) {
            Round potential = total_round_list.get(0).get(i);
            List<List<Round>> possible = new ArrayList<>();

            for (int j = 0; j < player_count - 2; i++) {
                List<Round> possible_possible = new ArrayList<>(); // god i hate myself for this name

                Iterator<Round> attempt_iterator = total_round_list.get(j).iterator();

                while (attempt_iterator.hasNext()) {
                    Round next = attempt_iterator.next();
                    if (!next.hasSimilarity(potential)) { possible.add(next); }
                }
            }
        }
        */


        }
    public static BigInteger factorial(int num) {
        BigInteger factorial = BigInteger.ONE;

        for (int i = num; i > 0; i--) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }

        return factorial;
    }

    public static long combinations(int num, int choose) {
        return factorial(num).divide( factorial(choose).multiply(factorial(num - choose))).longValue();
    }

    public static long permutations(int num, int choose) {
        return factorial(num).divide(factorial(num - choose)).longValue();
    }
}