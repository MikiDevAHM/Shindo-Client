package me.miki.shindo.features.account;

import com.google.gson.*;
import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.multithreading.Multithreading;
import me.miki.shindo.helpers.network.JsonHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class AccountManager {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final ArrayList<Account> accounts = new ArrayList<Account>();

    private String currentAccount;

    public AccountManager() {

        FileManager fileManager = Shindo.getInstance().getFileManager();
        File microsoftDir = new File(fileManager.getExternalDir(), "microsoft");
        File accountFile = new File(fileManager.getShindoDir(), "Account.json");
        File skinDir = new File(fileManager.getCacheDir(), "skin");

        if (!microsoftDir.exists()) {
            fileManager.createDir(microsoftDir);
        }

        if (!accountFile.exists()) {
            fileManager.createFile(accountFile);
        }

        if (!skinDir.exists()) {
            fileManager.createDir(skinDir);
        }

        if (accountFile.length() > 0) {
            load();
        }

        if (getAccountByName(currentAccount) != null) {

            if (getAccountByName(currentAccount).getType().equals(AccountType.MICROSOFT)) {
                Multithreading.runAsync(() -> {
                    SessionChanger.getInstance().setUserMicrosoft(getAccountByName(currentAccount).getEmail(), getAccountByName(currentAccount).getPassword());
                });
            } else {
                mc.setSession(new Session(getAccountByName(currentAccount).getName(), "0", "0", "mojang"));
            }
        }
    }

    public void save() {

        FileManager fileManager = Shindo.getInstance().getFileManager();

        try (FileWriter writer = new FileWriter(new File(fileManager.getShindoDir(), "Account.json"))) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            if (mc.getSession() != null) {

                jsonObject.addProperty("Current Account", currentAccount);

                for (Account acc : accounts) {

                    JsonObject accJsonObject = new JsonObject();
                    accJsonObject.addProperty("Name", acc.getName());

                    if (acc.getType().equals(AccountType.MICROSOFT)) {
                        String email = new String(Base64.getEncoder().encode(acc.getEmail().getBytes(StandardCharsets.UTF_8)));
                        String password = new String(Base64.getEncoder().encode(acc.getPassword().getBytes(StandardCharsets.UTF_8)));

                        accJsonObject.addProperty("Email", email);
                        accJsonObject.addProperty("Password", password);
                    }
                    accJsonObject.addProperty("Account Type", acc.getType().getId());

                    jsonArray.add(accJsonObject);
                }

                jsonObject.add("Accounts", jsonArray);
                gson.toJson(jsonObject, writer);
            }
        } catch (Exception e) {
            ShindoLogger.error("Failed to save account", e);
        }
    }

    public void load() {

        FileManager fileManager = Shindo.getInstance().getFileManager();

        try (FileReader reader = new FileReader(new File(fileManager.getShindoDir(), "Account.json"))) {

            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            if (jsonObject != null && jsonObject.isJsonObject()) {

                JsonArray jsonArray = JsonHelper.getArrayProperty(jsonObject, "Accounts");

                currentAccount = JsonHelper.getStringProperty(jsonObject, "Current Account", "null");

                if (jsonArray != null) {

                    for (JsonElement jsonElement : jsonArray) {

                        JsonObject accJsonObject = gson.fromJson(jsonElement, JsonObject.class);

                        String name = JsonHelper.getStringProperty(accJsonObject, "Name", "null");

                        // Usando o metodo getAccountTypeById para pegar o AccountType correto
                        int accountTypeId = JsonHelper.getIntProperty(accJsonObject, "Account Type", 0);
                        AccountType type = AccountType.getAccountTypeById(accountTypeId);

                        if (type != null) {
                            if (type.equals(AccountType.MICROSOFT)) {
                                String email = new String(Base64.getDecoder().decode(JsonHelper.getStringProperty(accJsonObject, "Email", "null")));
                                String password = new String(Base64.getDecoder().decode(JsonHelper.getStringProperty(accJsonObject, "Password", "null")));
                                accounts.add(new Account(name, email, password, type));
                            } else {
                                accounts.add(new Account(name, null, null, type));
                            }
                        }
                    }
                }

                // Após carregar as contas, tente configurar a conta atual
                if (getAccountByName(currentAccount) != null) {
                    setCurrentAccount(getAccountByName(currentAccount));
                }

            }

        } catch (Exception e) {
            ShindoLogger.error("Failed to load account", e);
        }
    }

    public Account getCurrentAccount() {
        return getAccountByName(currentAccount);
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account.getName();
    }

    public Account getAccountByName(String name) {
        if (name == null || name.isEmpty()) {
            return null; // Retorna null se o nome for inválido (null ou vazio)
        }

        for (Account account : accounts) {
            if (account != null && name.equalsIgnoreCase(account.getName())) {
                return account; // Retorna a conta se o nome for encontrado
            }
        }

        return null; // Retorna null se não encontrar a conta
    }

    public boolean isAccountNameAvailable(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        // Verificando se existe uma conta com nome semelhante (ignorando diferenças de maiúsculas/minúsculas)
        return accounts.stream()
                .noneMatch(acc -> acc != null && acc.getName().equalsIgnoreCase(name));
    }

    public Account getAccountByEmail(String email) {
        if (email == null) {
            return null; // Retorna null se o e-mail for nulo
        }

        for (Account account : accounts) {
            if (account.getEmail() != null && account.getEmail().equalsIgnoreCase(email)) {
                return account; // Retorna a conta se o e-mail for encontrado
            }
        }

        return null; // Retorna null se não encontrar
    }

    public boolean isEmailAvailable(String email) {
        if (email == null || email.isEmpty()) {
            return true; // Retorna true se o email for inválido (para permitir que um novo email seja registrado)
        }

        // Verificando se existe uma conta com o mesmo email (ignorando maiúsculas/minúsculas)
        return accounts.stream()
                .noneMatch(acc -> acc != null && acc.getEmail() != null && acc.getEmail().equalsIgnoreCase(email));
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void addAccounts(Account account) {
        accounts.add(account);
    }
}
