import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    // 穴が空いた時の配列
    public static final int[] nums = { 0, 0, 0, 0, 0 };
    // 抽選番号の上限
    public static final int MAX_NUM = 75;
    public static Random rand = new Random();
    public static List<Integer> list = new ArrayList<Integer>();

    public static void main(String[] args) {
        // ビンゴカード作成
        int[][] bingo = makeBingoCard();
        // 1~75までのList
        list = makeRandomNumber(1, MAX_NUM, MAX_NUM);
        startBingo(bingo, 0);
    }

    /**
     * ビンゴを開始する
     * 
     * @param bingo
     * @param index
     * @return
     */
    private static Object startBingo(int[][] bingo, int index) {
        int num = list.get(index);
        int column = (num - 1) / 15;
        for (int row = 0; row < 5; row++) {
            if (bingo[row][column] == num) {
                bingo[row][column] = 0;
                if (checkBingo(bingo, row, column)) {
                    System.out.println(row);
                    System.out.println(column);
                    int count = index + 1;
                    // 抽選回数を出力
                    System.out.println("抽選回数： " + count);
                    // ビンゴカードを一行ずつ出力
                    for (int i = 0; i < 5; i++) {
                        System.out.println(Arrays.toString(bingo[i]));
                    }
                    return null;
                } else {
                    return startBingo(bingo, index + 1);
                }
            }
        }
        return startBingo(bingo, index + 1);
    }

    /**
     * ビンゴカードを作成する
     * 
     * @return int[][] 5x5の二次元配列
     */
    public static int[][] makeBingoCard() {
        // 5x5の二次元配列を作成
        int[][] bingo = new int[5][5];
        for (int i = 0; i < 5; i++) {
            // 1列目は1~15、2列目は16~30までのランダムな数字を入れる
            List<Integer> list = makeRandomNumber(i * 15 + 1, i * 15 + 15, 5);
            for (int j = 0; j < 5; j++) {
                bingo[j][i] = list.get(j);
            }
        }
        // 真ん中は0にする
        bingo[2][2] = 0;
        // 作成したビンゴカードを一行ずつ出力する
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(bingo[i]));
        }
        return bingo;
    }

    /**
     * 任意の数字範囲でランダムなListを作成する
     * 
     * @param start 開始番号
     * @param end   終了番号
     * @return list ランダムなList
     */
    public static List<Integer> makeRandomNumber(int start, int end, int count) {
        // 数字範囲のList
        List<Integer> list = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        // 全ての数字使う場合はシャッフルする
        if (end == count) {
            Collections.shuffle(list);
            return list;
        } else {
            // 返却用のList
            List<Integer> result = new ArrayList<Integer>();
            for (int j = 0; j < count; j++) {
                // listの長さを取得
                int remainingCount = list.size();
                // ランダムなインデックス番号を取得
                int index = rand.nextInt(remainingCount);
                // 返却用のlistに追加
                result.add(list.get(index));
                // listの最後のインデックス番号
                int lastIndex = remainingCount - 1;
                // listの最後の要素
                int lastElement = list.remove(lastIndex);
                // listの最後のインデックス番号ではないとき
                if (index < lastIndex) {
                    // resultに追加した要素をlistの最後の要素で上書きする
                    list.set(index, lastElement);
                }
            }
            return result;
        }
    }

    /**
     * 縦横斜めのどれかでビンゴしているかどうか
     * 
     * @param bingo  ビンゴカード
     * @param row    空いたマスの行番号
     * @param column 空いたマスの列番号
     * @return ビンゴしているかどうか
     */
    public static boolean checkBingo(int[][] bingo, int row, int column) {
        return checkRow(bingo, row) || checkColumn(bingo, column) || checkCross(bingo, row, column);
    }

    /**
     * 空いたマスの行がビンゴしているかどうか
     * 
     * @param bingo ビンゴカード
     * @param row   空いたマスの行番号
     * @return ビンゴしているかどうか
     */
    public static boolean checkRow(int[][] bingo, int row) {
        return Arrays.equals(nums, bingo[row]);
    }

    /**
     * 空いたマスの列がビンゴしているかどうか
     * 
     * @param bingo  ビンゴカード
     * @param column 空いたマスの列番号
     * @return ビンゴしているかどうか
     */
    public static boolean checkColumn(int[][] bingo, int column) {
        int[] currentNums = new int[5];
        for (int i = 0; i < 5; i++) {
            currentNums[i] = bingo[i][column];
        }
        return Arrays.equals(nums, currentNums);
    }

    /**
     * 空いたマスの斜めがビンゴしているかどうか
     * 
     * @param bingo  ビンゴカード
     * @param row    空いたマスの行番号
     * @param column 空いたマスの列番号
     * @return ビンゴしているかどうか
     */
    public static boolean checkCross(int[][] bingo, int row, int column) {
        // 空いたマスが斜めに関連していなければfalseを返す
        if (row == column || row == 4 - column) {
            int[] cross = new int[5];
            for (int i = 0; i < 5; i++) {
                int j = row == column ? i : 4 - i;
                cross[i] = bingo[j][i];
            }
            return Arrays.equals(nums, cross);
        } else {
            return false;
        }
    }
}
