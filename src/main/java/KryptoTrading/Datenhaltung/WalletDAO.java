package KryptoTrading.Datenhaltung;

public interface WalletDAO {
    boolean add_wallet(User user, String identifier_name);
    boolean delete_wallet(User user, String identifier_name);
    boolean update(String username, String identifier_name);
}
