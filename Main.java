import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {
    // 穴が空いた時の文字列
    public static final String LINE = "[0, 0, 0, 0, 0]";
    // 斜めチェック用のMap
    public static Map<Integer, List<Integer>> crossNums = new HashMap<Integer, List<Integer>>();
    // 抽選番号の上限
    public static final int MAX_NUM = 75;
    public static Random rand = new Random();

    public static void main(String[] args) {
        // ビンゴカード作成
        int[][] bingo = makeBingoCard();
        // 斜めチェック用のMap作成
        crossNums = makeCrossNums();
        // 抽選回数用のカウント
        int count = 0;
        // 1~75までのList
        List<Integer> list = makeRandomNumber(1, MAX_NUM, 75);
        // ビンゴスタート
        bingoGame: for (int i = 0; i < list.size(); i++) {
            // 抽選番号
            int num = list.get(i);
            // 1~15までなら一列目だけ確認する
            int column = (num - 1) / 15;
            bingoCheck: for (int row = 0; row < 5; row++) {
                // 穴が空いている場所はチェックしない
                if (bingo[row][column] > 0) {
                    // 抽選番号と一致しているか
                    if (bingo[row][column] == num) {
                        // 一致していたところを0にする
                        bingo[row][column] = 0;
                        // 一致していたマスの縦横斜めでビンゴしているかチェックする
                        if (checkBingo(bingo, row, column)) {
                            System.out.println(row);
                            System.out.println(column);
                            // ビンゴしていれば抽選回数をセットする
                            count = i;
                            // bingoGameのfor文を抜ける
                            break bingoGame;
                        }
                        // 一致していたマス以降はチェック不要なのでbingoCheckのfor文を抜ける
                        break bingoCheck;
                    }
                }
            }
        }
        // 抽選回数を出力
        System.out.println("抽選回数： " + count);
        // ビンゴカードを一行ずつ出力
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(bingo[i]));
        }
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
        // 返却用のList
        List<Integer> result = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        // 全ての数字使う場合はシャッフルする
        if (end == count) {
            Collections.shuffle(list);
            return list;
        } else {
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
     * 斜めチェック用のマスの組み合わせを作成する
     * 
     * @return crossNums
     */
    public static Map<Integer, List<Integer>> makeCrossNums() {
        // 0: [0,4]
        // 1: [1,3]
        // 2: [2]
        // 3: [1,3]
        // 4: [0,4]
        // 上記のMapを作る
        Map<Integer, List<Integer>> crossNums = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(i);
            list.add(4 - i);
            crossNums.put(i, list);
            crossNums.put(4 - i, list);
        }
        return crossNums;
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
        return LINE.equals(Arrays.toString(bingo[row]));
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
        return LINE.equals(Arrays.toString(currentNums));
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
        if (!isTargetRowColumn(row, column)) {
            return false;
        }
        int[] rightBottom = new int[5];
        int[] leftBottom = new int[5];
        for (int i = 0; i < 5; i++) {
            rightBottom[i] = bingo[i][i];
            leftBottom[i] = bingo[4 - i][i];
        }
        return LINE.equals(Arrays.toString(rightBottom)) || LINE.equals(Arrays.toString(leftBottom));
    }

    /**
     * 空いたマスが斜めに関連しているかどうか
     * 
     * @param row    空いたマスの行番号
     * @param column 空いたマスの列番号
     * @return 空いたマスが斜めに関連しているかどうか
     */
    public static boolean isTargetRowColumn(int row, int column) {
        return crossNums.get(row).contains(column);
    }
}

