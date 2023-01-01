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

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < player_list.length; i++) {
            result += player_list[i];
        }
        return result;
    }
}
