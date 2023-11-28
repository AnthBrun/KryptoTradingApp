package KryptoTrading.GUI.model;

import KryptoTrading.GUI.model.exceptions.*;
import KryptoTrading.GUI.view.LoginLayout;
import KryptoTrading.GUI.view.Main;
import KryptoTrading.GUI.model.exceptions.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
public class Util {
	private static final String CRYPTOGRAPHIC_ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
	
	
	public static void createNewAccount(String username, CharSequence password, CharSequence passwordRepeat) throws AccountExistsException, PasswordsNotFitException, PasswordToShortException, NoUsernameException, TooLongUsernameException, TooLongPasswordException {
		//Checking
		File root = new File(Globals.DATA_PATH);
		root.mkdir();
		boolean found = false;
		File files[] = root.listFiles();
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			if (currentFile.isDirectory() && currentFile.getName().equals(username + Globals.USER_DIRECTORY_SUFFIX)) {
				found = true;
				break;
			}
		}	
		if (!found) {
			if(!passwordsFit(password, passwordRepeat)) {
				throw new PasswordsNotFitException();
			}
			if (password.length() < Globals.MIN_PASSWORD_LENGTH) {
				throw new PasswordToShortException();
			}
			
			if (password.length() > Globals.MAX_PASSWORD_LENGTH) {
				throw new TooLongPasswordException();
			}
			
			if (username.length() <= 0) {
				throw new NoUsernameException();
			}
			if (username.length() > Globals.MAX_USERNAME_LENGTH) {
				throw new TooLongUsernameException();
			}
			
			
		}
		else {
			throw new AccountExistsException();
		}
		
		
		
		
		//Creating
		File userDir = new File(Globals.DATA_PATH + "/" + username + Globals.USER_DIRECTORY_SUFFIX);
		userDir.mkdir();
		
		File loginData = new File(Globals.DATA_PATH + "/" + username + Globals.USER_DIRECTORY_SUFFIX + "/" + username + Globals.LOGIN_DATA_SUFFIX);
		try(FileWriter fw = new FileWriter(loginData)) {
			fw.write(username + Globals.SEPARATOR + (new String(CharSequenceToCharArray(password)).hashCode()) + Globals.SEPARATOR);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	public static void login(String username, CharSequence password) throws FileNotFoundException, IOException, DataCorruptedException, PasswordsNotFitException {
		Util.saveConfig(Main.config);
		File root = new File(Globals.DATA_PATH);
		root.mkdir();
		
		String realUsername = "";
		
		String filename = Globals.DATA_PATH + "/" + username + Globals.USER_DIRECTORY_SUFFIX + "/" + username + Globals.LOGIN_DATA_SUFFIX;
		File loginData = new File(filename);
		
		try(FileReader fr = new FileReader(loginData)) {
			char[] buf = new char[Globals.MAX_BUFFER_LENGTH];
			int len = fr.read(buf);
			int passOffset = -1;
			for (int i = 0; i < len; i++) {
				if (buf[i] == Globals.SEPARATOR) {
					realUsername = String.valueOf(buf, 0, i);
					passOffset = i+1;
					break;
				}
			}
			
			CharSequence realPassword = CharBuffer.wrap(buf, passOffset, len-(passOffset+1));
			
			if (!realUsername.equals(username)) throw new DataCorruptedException();
			if (!passwordsFit(""+new String(CharSequenceToCharArray(password)).hashCode(), realPassword)) throw new PasswordsNotFitException();
			
		}
		
		
	}
	
	
	public static boolean isAdmin(String username) {
		if(username.equals(Globals.ADMIN_NAME)) {
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void logout() {
		Main.changeScene(new LoginLayout());
	}
	
	
	
	
	
	
	
	
	
	
	private static boolean passwordsFit(CharSequence password1, CharSequence password2) {
		if (password1.length() != password2.length()) return false;
		for (int i = 0; i < password1.length(); i++) {
			if (password1.charAt(i) != password2.charAt(i)) return false;
		}
		return true;
	}
	
	
	
	
	
	public static void saveConfig(ConfigBean config) throws FileNotFoundException, IOException {
		File sFile = new File(Globals.CONFIG_PATH);
		try (FileOutputStream fos = new FileOutputStream(sFile)) {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(config);
		}
	}
	
	
	public static ConfigBean loadConfig() throws FileNotFoundException, IOException, ClassNotFoundException {
		File sFile = new File(Globals.CONFIG_PATH);
		ConfigBean config;
		try(FileInputStream fis = new FileInputStream(sFile)) {
			ObjectInputStream ois = new ObjectInputStream(fis);
			config = (ConfigBean)ois.readObject();
		}
		return config;
	}
	
	
	
	
	
	
	public static void saveEntrys(String username, ArrayList<EntryBean> entrys) throws FileNotFoundException, IOException {
		File entryFile = new File(Globals.DATA_PATH + "/" + username + Globals.USER_DIRECTORY_SUFFIX + "/" + username + Globals.ENTRY_DATA_SUFFIG);
		
		try(FileOutputStream fis = new FileOutputStream(entryFile)) {
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.writeObject(entrys);
			
		}
	}
	
	
	
	
	public static ArrayList<EntryBean> loadEntrys(String username) throws FileNotFoundException, IOException, ClassNotFoundException {
		File entryFile = new File(Globals.DATA_PATH + "/" + username + Globals.USER_DIRECTORY_SUFFIX + "/" + username + Globals.ENTRY_DATA_SUFFIG);
		ArrayList<EntryBean> entrys;
		try(FileInputStream fis = new FileInputStream(entryFile)) {
			ObjectInputStream ois = new ObjectInputStream(fis);
			entrys = (ArrayList<EntryBean>)ois.readObject();
		}
		
		return entrys;
	}
	
	
	
	public static char[] CharSequenceToCharArray(CharSequence seq) {
		char[] result = new char[seq.length()];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = seq.charAt(i);
		}
		return result;
	}
	
	
	
	
	public static char[] generatePassword(char[] alphabet, int len) {
		char[] result = new char[len];
		for (int i = 0; i < len; i++) {
			result[i] = alphabet[(int)(Math.random() * alphabet.length)];
		}
		return result;
	}
	
	
	
	

    public static char[] encrypt(char[] data, char[] encryptionKey) throws Exception {
        SecretKeySpec key = generateKey(encryptionKey);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(new String(data).getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes).toCharArray();
    }

    public static char[] decrypt(char[] encryptedData, char[] encryptionKey) throws Exception {
        SecretKeySpec key = generateKey(encryptionKey);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(new String(encryptedData));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8).toCharArray();
    }

    private static SecretKeySpec generateKey(char[] encryptionKey) throws Exception {
        byte[] keyBytes = new byte[16]; // AES verwendet einen 128-Bit-Schl�ssel (16 Byte)
        byte[] passwordBytes = new String(encryptionKey).getBytes(StandardCharsets.UTF_8);
        System.arraycopy(passwordBytes, 0, keyBytes, 0, Math.min(keyBytes.length, passwordBytes.length));
        return new SecretKeySpec(keyBytes, CRYPTOGRAPHIC_ALGORITHM);
    }
	
	

}
