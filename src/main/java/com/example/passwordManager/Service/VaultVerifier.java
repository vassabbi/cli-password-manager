package com.example.passwordManager.Service;

import java.security.GeneralSecurityException;

import com.example.passwordManager.Repository.VaultRepository;

public class VaultVerifier {
    private final VaultRepository repo;
    private final VaultCodec codec;

    public VaultVerifier(VaultRepository repo, VaultCodec codec){
        this.repo = repo;
        this.codec = codec;
    }

    public boolean verify(String username, char[] password){
        byte[] encrypted = repo.load(username);
        if (encrypted == null) {
            return false;
        }

        try {
            codec.decode(encrypted, password);
            return true;
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

}
