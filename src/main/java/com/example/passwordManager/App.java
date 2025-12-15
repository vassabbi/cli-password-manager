package com.example.passwordManager;

import com.example.passwordManager.Controller.VaultController;
import com.example.passwordManager.Repository.VaultRepository;
import com.example.passwordManager.Service.BackupService;
import com.example.passwordManager.Service.EntryMetadata;
import com.example.passwordManager.Service.VaultApplicationService;
import com.example.passwordManager.Service.VaultService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VaultRepository vr = new VaultRepository();
        String userName = "Sasha";
        VaultService vs = new VaultService(vr, userName);
        BackupService bs = new BackupService(vr, userName);
        EntryMetadata em = new EntryMetadata();
        VaultApplicationService vas = new VaultApplicationService(vs, bs, em);
        VaultController vc = new VaultController(vas);
        vc.startCLICycle();
    }
}
