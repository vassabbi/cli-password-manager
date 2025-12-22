package com.example.passwordManager.Service;

import java.security.GeneralSecurityException;

import com.example.passwordManager.Model.AuthResult;
import com.example.passwordManager.Model.Vault;
import com.example.passwordManager.Repository.VaultRepository;

public class AuthService {
    private final VaultRepository repo;
    private final VaultCodec codec;

    public AuthService(VaultRepository repo, VaultCodec codec) {
        this.repo = repo;
        this.codec = codec;
    }
    
    public AuthResult login(String username, char[] password){
        byte[] encrypted = repo.load(username);
        if (encrypted == null) {
            return AuthResult.VAULT_NOT_FOUND;
        }
        try {
            codec.decode(encrypted, password);
            return AuthResult.SUCCESS;
        } catch (GeneralSecurityException e) {
            return AuthResult.INVALID_PASSWORD_OR_CORRUPTED;
        }
    }

    public boolean register(String username, char[] password){
        if (repo.load(username) != null){
            return false;
        }

        Vault vault = new Vault();
        try {
            byte[] encrypted = codec.encode(vault, password);
            repo.save(username, encrypted);
            return true;
        } catch (GeneralSecurityException ex) {
            return false;
        }
    }
}
