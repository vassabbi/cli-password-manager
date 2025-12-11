package com.example.passwordManager.Model;
import java.util.ArrayList;
import java.util.List;

public class Vault {
    private List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return entries;
    }
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
    public void addEntry(Entry entry) {
        this.entries.add(entry);
    }
}
