package com.abiao.wallet;


/**
 * 钱包的抽象接口
 * */
public interface Wallet {

    boolean getMoney(int Money);

    int remain();

    int total();

    int spend();
}
