# coding:utf-8
import random

lotteryNumber = random.sample(range(1, 76), k=75)


def main():
    bingoCard = makeBingoCard()
    for row in bingoCard:
        print(row)
    startBingo(bingoCard, 0, 0)


def makeBingoCard():
    list = [random.sample(range(i * 15 + 1, i * 15 + 16), k=5)
            for i in range(5)]
    bingoCard = [[row[i] for row in list] for i in range(5)]
    bingoCard[2][2] = 0
    return bingoCard


def startBingo(bingoCard, row, index):
    num = lotteryNumber[index]
    column = (num - 1) // 15
    if (bingoCard[row][column] == num):
        bingoCard[row][column] = 0
        if (checkBingo(bingoCard, row, column)):
            print('抽選回数：' + str(index+1))
            for row in bingoCard:
                print(row)
            return
        else:
            startBingo(bingoCard, 0, index+1)
    else:
        if (row == 4):
            startBingo(bingoCard, 0, index + 1)
        else:
            startBingo(bingoCard, row+1, index)


def checkBingo(bingoCard, row, column):
    return checkRow(bingoCard, row) or checkColumn(bingoCard, column) or checkCross(bingoCard, row, column)


def checkRow(bingoCard, row):
    return all([i == 0 for i in bingoCard[row]])


def checkColumn(bingoCard, column):
    return all([bingoCard[row][column] == 0 for row in range(5)])


def checkCross(bingoCard, row, column):
    if (row == column or row == 4 - column):
        return all([bingoCard[i][i] == 0 for i in range(5)]) or all([bingoCard[i][4 - i] == 0 for i in range(5)])
    else:
        return False


if __name__ == '__main__':
    main()
