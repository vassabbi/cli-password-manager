package com.example.passwordManager.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
