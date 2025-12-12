package com.example.passwordManager.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VaultRepository {

    private final Path dataDir = Paths.get("data");

    public VaultRepository() {
        try {
            if (!Files.exists(dataDir)){
                Files.createDirectories(dataDir);
            }
        }
        catch (IOException e){
            throw new RuntimeException("Cannot create data directory", e);
        }
    }
    
    public byte[] load(String userName){
        Path filePath = dataDir.resolve(userName + ".vault");
        try{
            byte[] data = Files.readAllBytes(filePath);
            return data;
        } catch (IOException e){
            return null;
        }   
    }

    public boolean save(String userName, byte[] data){
        Path filePath = dataDir.resolve(userName + ".vault");
        try{
            Files.write(filePath, data);
            return true;
        } catch (IOException e){
            System.out.println("Failed to write to file");
            return false;
        }
    }

    public boolean backup(String userName){
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path originalPath = dataDir.resolve(userName + ".vault");
        Path backupPath = dataDir.resolve(userName + "_" + timestamp + ".vault.bak");
        try {
            Files.copy(originalPath, backupPath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<Path> getBackups(String username){
        try {
            return Files.list(dataDir)
                .filter(Files::isRegularFile)
                .toList();
        } catch (IOException e){
            return null;
        }
    }

    public boolean restore(String userName, Path backupPath){
        Path originalPath = dataDir.resolve(userName + ".vault");
        try {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
