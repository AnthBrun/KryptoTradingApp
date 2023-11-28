package KryptoTrading.Fachlogik;

import KryptoTrading.Datenhaltung.Coin_in_Wallet;
import KryptoTrading.Datenhaltung.Coins;
import KryptoTrading.Datenhaltung.Wallet;

public class CoinFunctions {
    public static boolean add_new_Coin(String shortname, String name, double value, double market_capitalization, int total_amount){
        if (Coins.getCoin(shortname) == null){
            return Coins.add(shortname, name, value, market_capitalization, total_amount);
        }
        return false;
    }

    public static boolean remove_Coin(String shortname){
        return Coins.remove(shortname);
    }

    public static boolean add_coins_to_wallet(String shortname, double amount, Wallet wallet){
        if (Coin_in_Wallet.check_coin_in_wallet(wallet, shortname)){
            Coin_in_Wallet.add_coins(wallet, shortname, amount);
        } else {
            Coin_in_Wallet.insert_new_coin(wallet, shortname, amount);
        }
        return true;
    }

    public static boolean remove_coins_from_wallet(String shortname, double amount, Wallet wallet){
        if (Coin_in_Wallet.check_amount(wallet, shortname, amount)){
            return Coin_in_Wallet.remove_coins(wallet, shortname, amount);
        }
        System.out.printf("not enough %s\nactual amount: %s\n",shortname, Coin_in_Wallet.getCoin_amount(wallet, shortname));
        return false;
    }
}
