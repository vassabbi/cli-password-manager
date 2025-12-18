package com.example.passwordManager.Service;

import java.security.GeneralSecurityException;

import com.example.passwordManager.Model.Vault;
import com.google.gson.Gson;

public class VaultCodec {
    private final Gson gson = new Gson();

    public Vault decode(byte[] data, char[] password) throws GeneralSecurityException {
        byte[] decrypted = CryptoService.decrypt(data, password);
        return gson.fromJson(new String(decrypted), Vault.class);
    }

    public byte[] encode(Vault vault, char[] password) throws GeneralSecurityException {
        String json = gson.toJson(vault);
        return CryptoService.encrypt(json.getBytes(), password);
    }
}