package co.com.foodcourt.model.rol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RolTest {

    @Test
    void shouldReturnEnumFromString() {
        Rol role = Rol.valueOf("ADMIN");
        assertEquals(Rol.ADMIN, role);
    }
}
