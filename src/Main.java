import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        int player_count = 8;

        // Create the two pairings
        List<int[]> total_matchup_list = getCombinations(player_count, 2);

        System.out.println("matchup_list.size() = " + total_matchup_list.size());
        List<List<int[]>> round_list = new ArrayList<>();

        total_matchup_list = removeWithPlayer(total_matchup_list, 0);
        List<int[]> useful_matchup_list = new ArrayList<>(total_matchup_list);
        for (int i = 1; i < player_count; i++) {
            useful_matchup_list = removeWithPlayer(total_matchup_list, i);
            round_list.add(getRounds(useful_matchup_list, (player_count/2) - 1, player_count, i));
        }

        List<List<int[]>> bracket_list = new ArrayList<>();
        bracket_list = getBracket(round_list, player_count);

        System.out.println("bracket_list = " + bracket_list.size());

        long tracker = 0;
        List<List<int[]>> solutions = new ArrayList<>();
        for (List<int[]> round : bracket_list) {
            if (checkBracketValidity(round)) {
                solutions.add(round);
                System.out.println("Found");
            }
            tracker++;
        }


        // Print out in a file
        try {
            FileWriter write = new FileWriter(String.format("Solutions with %d.txt", player_count));
            for (List<int[]> solution : solutions) {
                write.write("SOLUTION: + \n");
                for (int[] solution_match: solution) {
                    write.write(Arrays.toString(solution_match) + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("rip");
            e.printStackTrace();
        }



        // Get Runtime
        long end = System.currentTimeMillis();

        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");


    }

    public static List<int[]> getCombinations(int players, int n) {
        Iterator<int[]> pairing_iterator = CombinatoricsUtils.combinationsIterator(players, n);
        List<int[]> comb = new ArrayList<>();
        while (pairing_iterator.hasNext()) {
            final int[] combination = pairing_iterator.next();
            comb.add(combination);
        }
        return comb;
    }

    public static List<int[]> getRounds(List<int[]> players, int n, int total_players, int prefix) {
        Iterator<int[]> pairing_iterator = CombinatoricsUtils.combinationsIterator(players.size(), n);
        List<int[]> comb = new ArrayList<>();
        while (pairing_iterator.hasNext()) {
            final int[] combination = pairing_iterator.next();
            int[] player = new int[total_players];
            player[0] = 0;
            player[1] = prefix;
            for (int i = 1; i <= n; i++) {
                player[i*2] =     players.get(combination[i - 1])[0];
                player[i*2 + 1] = players.get(combination[i - 1])[1];
            }
            if (checkRoundValidity(player)) {
                comb.add(player);
            }

        }
        return comb;
    }

    public static List<List<int[]>> getBracket(List<List<int[]>> rounds, int players) {
        int[] counter = new int[players - 1];
        int round_len = rounds.get(0).size();
        List<List<int[]>> comb = new ArrayList<>();
        while (counter[0] < round_len) {
            List<int[]> temp_bracket = new ArrayList<>();
            for (int i = 0; i < rounds.size(); i++) {
                temp_bracket.add(rounds.get(i).get(counter[i]));
            }
            comb.add(temp_bracket);

            if (counter[counter.length - 1] != round_len - 1) {
                counter[counter.length - 1]++;
            } else {
                counter[counter.length - 1] = 0;
                counter[counter.length - 2]++;
                for (int i = counter.length - 2; i > 0; i--) {
                    if (counter[i] == round_len) {
                        counter[i] = 0;
                        counter[i - 1]++;
                    }
                }
            }

        }
        return comb;
    }

    public static List<int[]> removeWithPlayer(List<int[]> matchups, int n) {
        List<int[]> temp = new ArrayList<>();
        for (int[] i : matchups) {
            if (i[0] != n && i[1] != n) {
                temp.add(i);
            }
        }
        return temp;
    }

    public static boolean checkRoundValidity(int[] round) {
        int[] check = new int[round.length];
        int[] verify = new int[round.length];
        Arrays.fill(verify, 1);
        for (int i : round) {
            check[i]++;
        }
        return Arrays.equals(check, verify);
    }

    public static boolean checkBracketValidity(List<int[]> bracket) {
        int len = bracket.get(0).length;
        int[][] teammates = new int[len][len];
        int[][] opponents = new int[len][len];
        for (int k = 0; k < len; k++) {
            teammates[k][k]++;
            opponents[k][k] += 2;
        }
        for (int[] round: bracket) {
            for (int j = 0; j < round.length; j++) {
                if (j % 2 == 0) {
                    teammates[round[j]][round[j+1]]++;
                } else {
                    teammates[round[j]][round[j-1]]++;
                }
                if (j % 4 == 0 || j % 4 == 1) {
                    opponents[round[j]][round[j+2 - (j%2)]]++;
                    opponents[round[j]][round[j+3 - (j%2)]]++;
                } else {
                    opponents[round[j]][round[j-1 - (j%2)]]++;
                    opponents[round[j]][round[j-2 - (j%2)]]++;
                }
            }
        }

        // Verify

        if (Arrays.stream(teammates).flatMapToInt(Arrays::stream).distinct().count() == 1) {
            //if (Arrays.stream(opponents).flatMapToInt(Arrays::stream).distinct().count() == 1) {
                return true;
            //} else {
            //    return false;
            //}
        } else {
            return false;
        }
    }

}
