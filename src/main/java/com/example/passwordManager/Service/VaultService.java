package com.example.passwordManager.Service;

import java.security.GeneralSecurityException;
import java.util.List;

import com.example.passwordManager.Model.Entry;
import com.example.passwordManager.Model.Vault;
import com.example.passwordManager.Repository.VaultRepository;

public class VaultService {

    private final VaultRepository repo;
    private final VaultCodec codec;
    private Vault vault;
    private final String username;
    private final char[] password;

    public VaultService(VaultRepository repo, VaultCodec codec, String username, char[] password) {
        this.repo = repo;
        this.codec = codec;
        this.username = username;
        this.password = password;
    }

    public void saveVault() {
        try {
            byte[] encrypted = codec.encode(vault, password);
            repo.save(username, encrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to save vault", e);
        }
    }

    public final boolean loadVault() {
        byte[] encrypted = repo.load(username);
        if (encrypted == null) {
            vault = new Vault();
            return false;
        }

        try {
            vault = codec.decode(encrypted, password);
            return true;
        } catch (GeneralSecurityException e) {
            vault = null;
            //throw new RuntimeException("Vault corrupted or wrong password", e);
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
