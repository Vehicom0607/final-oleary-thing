import org.paukov.combinatorics3.Generator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int player_count = 12;

        // Create the two pairings
        int first = 0;
        int second = 0;
        int total_combos = player_count;
        List<int[]> total_matchup_list = new ArrayList<>();
        for (int i = 0; i < combinations(player_count, 2); i++) {

            // Add matchup
            int[] new_matchup = {first, second};
            total_matchup_list.add(new_matchup);

            // Increment
            second += 1;
            if (second == player_count) {
                first++;
                total_combos -= 1;
                second = player_count-total_combos + 1;
            }
        }

        System.out.println("matchup_list.size() = " + total_matchup_list.size());

        List<List<Round>> total_round_list = new ArrayList<>();

        for (int k = 1; k < player_count; k++)  {

            // remove the first combo which is always 1 - k
            List<int[]> matchup_list = new ArrayList<>();
            for (int[] match : total_matchup_list) {
                if (match[0] != 0 && match[1] != 0 && match[0] != k && match[1] != k) {
                    matchup_list.add(match);
                }
            }
            System.out.println("matchup_list.size() = " + matchup_list.size());

            int[] increment = new int[(player_count-2)/2];
            for (int i = 0; i < increment.length; i++) {
                increment[i] = i;
            }
            List<Round> round_list = new ArrayList<>();
            System.out.println("combinations(battle_list.size(), 2) = " + combinations(matchup_list.size(), 2));
            for (int i = 0; i < combinations(matchup_list.size(), 3); i++) {
                int[] current_round = new int[player_count];
                current_round[0] = 0;
                current_round[1] = k;
                for (int j = 0; j < increment.length; j++) {
                    System.out.print("increment[j] = " + increment[j]);
                    System.out.println(" increment[j+1] = " + increment[j]);

                    current_round[2 + 2*j] = matchup_list.get(increment[j])[0];
                    current_round[3 + 2*j] = matchup_list.get(increment[j])[1];
                }
                Round new_round = new Round(current_round);
                if (new_round.checkValidity()) {
                    round_list.add(new_round);
                }

                // Increment
                increment = incrementer(increment, matchup_list.size());
            }
            total_round_list.add(round_list);


        }

        long end = System.currentTimeMillis();
        System.out.print("Possibility Generation time is " + ((end - start) / 1000d) + " seconds");
        /*
        // Try each combo
        List<Round> solutions = new ArrayList<>();
        Round test_round = total_round_list.get(0).get(0);
        for (int i = 0; i < total_round_list.size(); i++) {

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

    public static int[] incrementer(int[] increment, int total_size) {
        if (increment[increment.length - 1] != total_size - 1) {
            increment[increment.length - 1]++;
        } else {
            for (int i = 1; i < increment.length; i++) {
                if (increment[i] == total_size - 1) {
                    increment[i-1]++;
                    increment[i] = increment[i - 1] + 1;
                }
            }
        }
        return increment;
    }
}