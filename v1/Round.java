import java.util.ArrayList;

public class Round {
    private int[] player_list;
    public Round(int[] players) {
        player_list = players;
    }

    public boolean checkValidity() {
        // Make sure only one of each number
        for (int i = 0; i < player_list.length; i++) {
            int counter = 0;
            for (int j = 0; j < player_list.length; j++ ) {
                if (player_list[j] == player_list[i]) {
                    counter++;
                }
            }
            if (counter != 1) {
                return false;
            }
        }
        return true;
    }

    public boolean hasSimilarity(Round other_player_list) {
        for (int j = 0; j < other_player_list.getLength()/2; j++) {
            for (int i = 0; i < player_list.length / 2; i++) {
                if ((player_list[i*2] == other_player_list.getPlayerList()[j*2] && player_list[i*2 + 1] == other_player_list.getPlayerList()[j*2 + 1]) || (player_list[i*2] == other_player_list.getPlayerList()[j*2 + 1] && player_list[i*2 + 1] == other_player_list.getPlayerList()[j*2])) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getLength() {
        return player_list.length;
    }

    public int[] getPlayerList() {
        return player_list;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < player_list.length; i++) {
            result += player_list[i];
        }
        return result;
    }
}
