package api.interfaces;

public interface IAccountController {
    Object register(String username, String password);
    Object login(String username, String password);
    Object signOut(String username);
}
