package io.chocorean.authmod.datasource;

import io.chocorean.authmod.PlayerFactory;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.model.IPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileDataSourceStrategyTest {

    private File dataFile;
    private IPlayer player;
    private IDataSourceStrategy dataSource;

    @BeforeEach
    void init() {
        this.dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
        if(this.dataFile.exists()) {
            this.dataFile.delete();
        }
    }

    private boolean registerPlayer() throws RegistrationException {
        this.dataSource = new FileDataSourceStrategy(this.dataFile);
        this.player = PlayerFactory.create();
        return dataSource.add(this.player);
    }

    @Test
    public void testConstructor() {
        new FileDataSourceStrategy(this.dataFile);
        assertTrue(this.dataFile.exists(), "The strategy should create a CSV file automatically");
    }

    @Test
    public void testAdd() throws AuthmodException {
        boolean added = this.registerPlayer();
        assertTrue(added, "The player should be registered");
    }

    @Test
    public void testAddDoublon() throws AuthmodException {
        boolean added = this.registerPlayer();
        assertTrue(added, "The player should be registered");
        assertThrows(PlayerAlreadyExistException.class, () -> {
            this.registerPlayer();
        });
    }

    @Test
    public void testFindByEmail() throws RegistrationException {
        this.registerPlayer();
            assertNotNull(dataSource.find(this.player.getEmail(), null), "The player should exist and be found");
    }

    @Test
    public void testFindByUsername() throws RegistrationException {
        this.registerPlayer();
        assertNotNull(dataSource.find(null, player.getUsername()), "The player should exist and be found");
    }

    @Test
    public void testFindNotExist() throws RegistrationException {
        this.registerPlayer();
        assertNull(dataSource.find("test@test.fr", "test"), "The player should not exist");
    }

    @Test
    public void testFindByUsernameOrEmail() throws RegistrationException {
        this.registerPlayer();
        assertNotNull(dataSource.find(player.getEmail(), player.getUsername()), "The player should exist and be found");
    }

    @Test
    public void testFindNullParams() throws AuthmodException {
        this.registerPlayer();
        assertNull(dataSource.find(null, null), "It should return null");
    }

}