package com.example.passwordManager.Service;

import java.util.List;

import com.example.passwordManager.Model.Entry;
import com.example.passwordManager.Model.Vault;
import com.example.passwordManager.Repository.VaultRepository;
import com.google.gson.Gson;

public class VaultService {

    private VaultRepository repo = new VaultRepository();
    private Vault vault;
    private final String userName = "Sasha";
    Gson gson = new Gson();

    public VaultService() {
        this.loadVault();
    }

    public void saveVault() {
        String jsonVault = gson.toJson(vault);
        byte[] data = jsonVault.getBytes();
        repo.save(userName, data);
    }

    private void loadVault() {
        byte[] data = repo.load(userName);
        if (data != null) {
            String jsonVault = new String(data);
            this.vault = gson.fromJson(jsonVault, Vault.class);
            if (this.vault == null) {
                this.vault = new Vault();
            }
        } else {
            vault = new Vault();
        }
    }

    private int getNextId() {
        return vault.getEntries()
                .stream()
                .mapToInt(Entry::getId)
                .max()
                .orElse(0) + 1;
    }

    public boolean addEntry(String serviceName, String username, String password, String notes, String url) {
        Entry entry = new Entry(
                this.getNextId(),
                serviceName,
                username,
                password,
                notes,
                url
        );
        if (this.vault.getEntries()
                .stream()
                .filter(en -> (en.getServiceName().equals(serviceName)) && en.getUsername().equals(username))
                .count() > 0) {
            return false;
        }

        this.vault.addEntry(entry);
        return true;
    }

    public List<Entry> getAllEntries() {
        return vault.getEntries();
    }
}
