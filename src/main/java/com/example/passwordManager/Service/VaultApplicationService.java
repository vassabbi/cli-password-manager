package com.example.passwordManager.Service;

import java.nio.file.Path;
import java.util.List;

import com.example.passwordManager.Model.Entry;

public class VaultApplicationService {

    private final VaultService vaultService;
    private final BackupService backupService;
    private final EntryMetadata entryMetadata;

    public VaultApplicationService(
        VaultService vaultService, 
        BackupService backupService, 
        EntryMetadata entryMetadata
    ) {
        this.vaultService = vaultService;
        this.backupService = backupService;
        this.entryMetadata = entryMetadata;
    }

    //Vault CRUD
    public boolean addEntry(
            String serviceName,
            String username,
            String password,
            String notes,
            String url
    ){
        boolean added = vaultService.addEntry(serviceName, username, password, notes, url);
        if (added) {
            vaultService.saveVault();
        }
        return added;
    }

    public List<Entry> getAllEntries(){
        return vaultService.getAllEntries();
    }

    public Entry getEntryById(int id){
        return vaultService.getEntryById(id);
    }

    public boolean removeEntryById(int id){
        boolean removed = vaultService.removeEntryById(id);

        if (removed){
            vaultService.saveVault();
        }
        return removed;
    }

    public boolean updateEntryField(
        Entry entry,
        String fieldName,
        String newValue
    ){
        boolean updated = entryMetadata.updateFieldValue(entry, fieldName, newValue);
        if (updated){
            vaultService.saveVault();
        }
        return updated;
    }

    public List<Entry> searchByKeyword(String keyword){
        return vaultService.getEntriesByKeyword(keyword);
    }

    public boolean createBackup(){
        return backupService.backup();
    }

    public List<Path> getAvailableBackups(){
        return backupService.getBackups();
    }

    public boolean restoreFromBackup(Path backupPath){
        boolean restored = backupService.restore(backupPath);
        if (restored){
            vaultService.loadVault();
        }
        return restored;
    }

    public Iterable<String> getEditableFieldNames() {
        return entryMetadata.getFieldNames();
    }

    public String getFieldValue(Entry entry, String fieldName) {
        return entryMetadata.getFieldValue(entry, fieldName);
    }

    public void shutdown() {
        vaultService.saveVault();
    }
}
