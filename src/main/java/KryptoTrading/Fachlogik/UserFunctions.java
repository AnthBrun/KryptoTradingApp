package KryptoTrading.Fachlogik;

import KryptoTrading.Datenhaltung.User;
import KryptoTrading.GUI.model.Globals;
import KryptoTrading.GUI.model.exceptions.*;

import java.sql.SQLException;

public class UserFunctions {
    public static boolean login(String username, String password) {
        String hashed_password = HashGeneratorSHA256.hashString("fuckjava" + password);
        String adresse = HashGeneratorSHA256.hashString(username);
        return User.login(adresse, hashed_password);

    }

    public static boolean register(String username, String password, String repeat_password) throws PasswordsNotFitException, PasswordToShortException, TooLongUsernameException, TooLongPasswordException, NoUsernameException, SQLException {
        if (!password.equals(repeat_password)) {
            throw new PasswordsNotFitException();
        }

        if (password.length() < Globals.MIN_PASSWORD_LENGTH) {
            throw new PasswordToShortException();
        }

        if (username.length() > Globals.MAX_USERNAME_LENGTH) {
            throw new TooLongUsernameException();
        }

        if (password.length() > Globals.MIN_PASSWORD_LENGTH) {
            throw new TooLongPasswordException();
        }

        if (username.isEmpty()) {
            throw new NoUsernameException();
        }
        String hashed_password = HashGeneratorSHA256.hashString("fuckjava" + password);
        String adresse = HashGeneratorSHA256.hashString(username);
        return User.register(username, adresse, hashed_password);
    }
}