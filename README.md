# Crypto Trading Platform Kryptonia v0.9

This software is a prototype of a crypto trading platform where user can register an account, manage wallets and transfer crypto coins.

## Usage
Only start the program by executing the Kryptonia_start.bat. The batch file will execute the regular jar with additional arguments of JavaFX.
Executing the jar file itself will raise an error because of missing JavaFX files. Make sure you have a vpn connection.
The bat script will also open a terminal which contains more information about the layers.

### User Types
In this project are two kinds of logins: admin and user.  
By using the **admin layout**, you are allowed to select, add, delete and update every table.  
By using the **user layout**, you are only allowed to organize your wallets and transfer coins.  

**Note:** User data are written below.

## Information
- Hashes SHA256
password = hash of clear password
address = hash of username
wallet_id = hash of address + random UUID (universally unique identifier)

### Password
The 'password_hash' is salted with "fuckjava" before

### User Data [username, password]
- [admin, admin]
- [test, test]
- [user, hallo123]

## Possibly required
jdk 18.0.2  
javafx sdk 20.0.2

Written with Intellij
&copy; by Philipp Mikos & Anthony Brunner



