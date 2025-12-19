package com.example.passwordManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new ApplicationRunner().run();
        /*VaultCodec vaultCodec = new VaultCodec();
        Scanner scanner = new Scanner(System.in);
        VaultRepository vr = new VaultRepository();
        VaultVerifier vv = new VaultVerifier(vr, vaultCodec);
        AuthService as = new AuthService(vv);
        AuthController ac = new AuthController(as, scanner);
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
            VaultController vc = new VaultController(vas, scanner);
            vc.startCLICycle();
            System.out.println("Do you want to switch user? (y/n)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }
        //String userName = "Sasha";
     */   
    }
}
