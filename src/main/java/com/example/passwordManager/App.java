package com.example.passwordManager;

import java.util.Scanner;

import com.example.passwordManager.Controller.AuthController;
import com.example.passwordManager.Controller.VaultController;
import com.example.passwordManager.Model.UserSession;
import com.example.passwordManager.Repository.VaultRepository;
import com.example.passwordManager.Service.AuthService;
import com.example.passwordManager.Service.BackupService;
import com.example.passwordManager.Service.EntryMetadata;
import com.example.passwordManager.Service.VaultApplicationService;
import com.example.passwordManager.Service.VaultCodec;
import com.example.passwordManager.Service.VaultService;
import com.example.passwordManager.Service.VaultVerifier;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VaultCodec vaultCodec = new VaultCodec();
        Scanner scanner = new Scanner(System.in);
        VaultRepository vr = new VaultRepository();
        VaultVerifier vv = new VaultVerifier(vr, vaultCodec);
        AuthService as = new AuthService(vv);
        AuthController ac = new AuthController(as);
        while (true){
            UserSession session = null;
            while (session == null){
                session = ac.login();
            }
            VaultService vs = new VaultService(vr, vaultCodec, session.getUsername(), session.getPassword());
            vs.loadVault();
            BackupService bs = new BackupService(vr, session.getUsername());
            EntryMetadata em = new EntryMetadata();
            VaultApplicationService vas = new VaultApplicationService(vs, bs, em);
            VaultController vc = new VaultController(vas);
            vc.startCLICycle();
            System.out.println("Do you want to switch user? (y/n)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }
        //String userName = "Sasha";
        
    }
}
