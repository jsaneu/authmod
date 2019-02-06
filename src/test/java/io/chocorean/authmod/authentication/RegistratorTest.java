package io.chocorean.authmod.authentication;

import io.chocorean.authmod.authentication.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.model.IPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class RegistratorTest {

    private Registrator registrator;
    private RegistrationPayload payload;
    private IDataSourceStrategy dataSource;
    private File dataFile;

    @BeforeEach
    void init() throws IOException, AuthmodException {
        this.dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
        if(this.dataFile.exists()) {
            this.dataFile.delete();
        }
        this.dataSource = new FileDataSourceStrategy(this.dataFile);
        this.dataFile.createNewFile();
        this.payload = new RegistrationPayload();
        payload.setEmail("test@test.test");
        payload.setUsername("mcdostone");
        payload.setPassword("rootroot");
        this.registrator = new Registrator(this.dataSource);
    }

    @Test
    public void testDefaultConstructor() {
        Registrator registrator = new Registrator();
        assertTrue(registrator.getDataSourceStrategy().getClass().equals(FileDataSourceStrategy.class), "Default data source strategy should be FileDataSourceStrategy");
    }

    @Test
    public void testConstructor() throws SQLException {
        Registrator registrator = new Registrator(new DatabaseSourceStrategy(null));
        assertTrue(registrator.getDataSourceStrategy().getClass().equals(DatabaseSourceStrategy.class), "Data source strategy should be DatatabaseSourceStrategy");
    }

    @Test
    public void testRegister() throws RegistrationException {
        boolean registered = this.registrator.register(this.payload);
        assertTrue(registered, "Player should be logged");
    }

    @Test
    public void testHashedPassword() throws RegistrationException {
        this.registrator.register(this.payload);
        IPlayer player = this.dataSource.find(this.payload.getEmail(), null);
        assertNotEquals(player.getPassword(), this.payload.getPassword(), "Passwords should be hashed");
    }

    @Test
    public void testRegisterInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> {
            this.registrator.register(this.payload.setEmail("wrong"));
        });
    }

    @Test
    public void testRegisterPlayerAlreadyExist() {
        assertThrows(PlayerAlreadyExistException.class, () -> {
            this.registrator.register(this.payload.setEmail("root@root.root"));
        });
    }

    @Test
    public void testRegisterUnauthorizedHostedDomain() {
        assertThrows(UnauthorizedHostedDomainException.class, () -> {
            this.registrator.register(this.payload.setEmail("root@root.fr"));
        });
    }

    @Test
    public void testLoginNullParams() throws RegistrationException {
        boolean registered = this.registrator.register(null);
        assertFalse(registered, "Can't register the player, no payload provided");
    }

}