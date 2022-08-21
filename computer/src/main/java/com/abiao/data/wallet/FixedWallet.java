package com.abiao.data.wallet;

/**
 * 固定钱包
 * */
public class FixedWallet implements Wallet {

    public boolean getMoney(int Money) {
        return false;
    }

    public int remain() {
        return 0;
    }

    public int total() {
        return 0;
    }

    public int spend() {
        return 0;
    }
}
