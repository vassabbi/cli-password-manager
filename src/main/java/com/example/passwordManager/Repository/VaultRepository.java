package com.example.passwordManager.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    
    public byte[] load(String username){
        Path filePath = dataDir.resolve(username + ".vault");
        try{
            byte[] data = Files.readAllBytes(filePath);
            return data;
        } catch (IOException e){
            return null;
        }   
    }

    public byte[] load(Path path){
        try{
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (IOException e){
            return null;
        }   
    }

    public boolean save(String username, byte[] data){
        Path filePath = dataDir.resolve(username + ".vault");
        try{
            Files.write(filePath, data);
            return true;
        } catch (IOException e){
            System.out.println("Failed to write to file");
            return false;
        }
    }

    public boolean copy(Path source, Path target, boolean overwrite){
        try {
            if (overwrite){
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(source, target);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<Path> listFiles(){
        try {
            return Files.list(dataDir)
                .filter(Files::isRegularFile)
                .toList();
        } catch (IOException e){
            return null;
        }
    }

    public Path getVaultPath(String username){
        return dataDir.resolve(username + ".vault");
    }

    public Path createBackupPath(String username, String timestamp){
        return dataDir.resolve(username + "_" + timestamp + ".vault.bak");
    }
}
