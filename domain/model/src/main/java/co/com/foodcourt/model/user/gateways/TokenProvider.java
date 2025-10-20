package co.com.foodcourt.model.user.gateways;

public interface TokenProvider {
    String generateToken(Long userId,String role,String email);
}
