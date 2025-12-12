package com.example.passwordManager.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.example.passwordManager.Model.Entry;
import com.example.passwordManager.Model.Vault;
import com.example.passwordManager.Repository.VaultRepository;
import com.google.gson.Gson;

public class VaultService {

    private final VaultRepository repo = new VaultRepository();
    private Vault vault;
    private final String userName = "Sasha";
    Gson gson = new Gson();
    private final Map<String, BiConsumer<Entry, String>> fieldSetters = new LinkedHashMap<>();
    private final Map<String, Function<Entry, String>> fieldGetters = new LinkedHashMap<>();

    public VaultService() {
        this.loadVault();

        fieldSetters.put("Service Name", Entry::setServiceName);
        fieldSetters.put("Username", Entry::setUsername);
        fieldSetters.put("Password", Entry::setPassword);
        fieldSetters.put("Notes", Entry::setNotes);
        fieldSetters.put("URL", Entry::setUrl);

        fieldGetters.put("Service Name", Entry::getServiceName);
        fieldGetters.put("Username", Entry::getUsername);
        fieldGetters.put("Password", Entry::getPassword);
        fieldGetters.put("Notes", Entry::getNotes);
        fieldGetters.put("URL", Entry::getUrl);
    }

    public String getField(Entry entry, String fieldName){
        Function<Entry, String> getter = fieldGetters.get(fieldName);
        if (getter != null){
            return getter.apply(entry);
        } else {
            return "";
        }
    }

    public Set<String> getEditableFieldNames(){
        return fieldGetters.keySet();
    }

    public boolean updateField(Entry entry, String fieldName, String value){
        BiConsumer<Entry, String> setter = fieldSetters.get(fieldName);
        if (setter == null) {
            return false;
        } else {
            setter.accept(entry, value);
            return true;
        }
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

    public boolean backup(){
        return repo.backup(userName);
    }

    public List<Path> getBackups(){
        List<Path> backups = repo.getBackups(userName);
        List<Path> backupsFiltered = new ArrayList<>();
        for (Path backup: backups){
            String fileName = backup.getFileName().toString();
            if (fileName.startsWith(userName + "_") && fileName.endsWith(".vault.bak")){
                backupsFiltered.add(backup);
            }
        }
        return backupsFiltered;
    }

    public boolean restore(Path backupPath){
        boolean restoring = repo.restore(userName, backupPath);
        loadVault();
        return restoring;
    }
}
