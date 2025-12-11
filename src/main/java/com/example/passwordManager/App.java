package com.example.passwordManager;

import com.example.passwordManager.Controller.VaultController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VaultController vc = new VaultController();
        vc.startCLICycle();
    }
}
