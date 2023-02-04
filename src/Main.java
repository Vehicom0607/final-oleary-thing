import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

        System.out.println("round_list = " + round_list);

        List<List<int[]>> bracket_list = new ArrayList<>();
        bracket_list = getBracket(round_list, player_count);

        System.out.println("bracket_list = " + bracket_list.size());

        System.out.println(checkBracketValidity(bracket_list.get(0)));



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
        Iterator<int[]> pairing_iterator = CombinatoricsUtils.combinationsIterator(rounds.get(0).size(), players - 1);
        List<List<int[]>> comb = new ArrayList<>();
        while (pairing_iterator.hasNext()) {
            final int[] combination = pairing_iterator.next();
            List<int[]> temp_bracket = new ArrayList<>();
            for (int i = 0; i < rounds.size(); i++) {
                temp_bracket.add(rounds.get(i).get(combination[i]));
            }
            comb.add(temp_bracket);
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
        for (int i =  0; i < bracket.get(0).length; i++) {
            for (int k = 0; k < len; k++) {
                teammates[k][k]++;
                opponents[k][k] += 2;
            }
            for (int[] round: bracket) {
                for (int j = 0; j < round.length; j++) {
                    if (j % 4 == 0) {
                        teammates[j][round[j+1]]++;
                        opponents[j][round[j+2]]++;
                        opponents[j][round[j+3]]++;
                    }
                    if (j % 4 == 1) {
                        teammates[j][round[j-1]]++;
                        opponents[j][round[j+1]]++;
                        opponents[j][round[j+2]]++;
                    }
                    if (j % 4 == 2) {
                        teammates[j][round[j+1]]++;
                        opponents[j][round[j-1]]++;
                        opponents[j][round[j-2]]++;
                    }
                    if (j % 4 == 3) {
                        teammates[j][round[j - 1]]++;
                        opponents[j][round[j - 2]]++;
                        opponents[j][round[j - 3]]++;
                    }
                }
            }

        }

        return false;
    }

}
