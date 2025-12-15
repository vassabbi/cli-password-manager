package com.example.passwordManager.Service;

import java.util.List;

import com.example.passwordManager.Model.Entry;
import com.example.passwordManager.Model.Vault;
import com.example.passwordManager.Repository.VaultRepository;
import com.google.gson.Gson;

public class VaultService {

    private final VaultRepository repo;
    private Vault vault;
    private final String userName;
    Gson gson = new Gson();

    public VaultService(VaultRepository repo, String username) {
        this.repo = repo;
        this.userName = username;
        this.loadVault();
    }

    public void saveVault() {
        String jsonVault = gson.toJson(vault);
        byte[] data = jsonVault.getBytes();
        repo.save(userName, data);
    }

    public final boolean loadVault() {
        byte[] data = repo.load(userName);
        if (data != null) {
            String jsonVault = new String(data);
            this.vault = gson.fromJson(jsonVault, Vault.class);
            if (this.vault == null) {
                this.vault = new Vault();
            }
            return true;
        } else {
            vault = new Vault();
            return false;
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

    public Entry getEntryById(int id) {
        return vault.getEntries()
                .stream()
                .filter(en -> en.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Entry> getEntriesByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return vault.getEntries()
                .stream()
                .filter(en
                        -> en.getServiceName().toLowerCase().contains(lowerKeyword)
                || en.getUrl().toLowerCase().contains(lowerKeyword)
                || en.getUsername().toLowerCase().contains(lowerKeyword)
                || en.getNotes().toLowerCase().contains(lowerKeyword)).toList();
    }

    public boolean removeEntryById(int id) {
        return vault.getEntries()
                .removeIf(en -> en.getId() == id);
    }
}
