package com.example.passwordManager.Service;

public class AuthService {
    private final VaultVerifier vaultVerifier;

    public AuthService(VaultVerifier vaultVerifier) {
        this.vaultVerifier = vaultVerifier;
    }
    
    public boolean login(String username, char[] password){
        //Vault exctractedVault = vaultVerifier.verify(username, password);
        return vaultVerifier.verify(username, password);
    }
}
