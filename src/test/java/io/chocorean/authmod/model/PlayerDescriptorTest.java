package io.chocorean.authmod.model;

import net.minecraft.entity.player.EntityPlayerMP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PlayerDescriptorTest {
    private PlayerDescriptor playerDescriptor;

    @BeforeEach
    void init() {
        this.playerDescriptor = new PlayerDescriptor(
            mock(EntityPlayerMP.class),
            new PlayerPos(null, 1, 0)
        );
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.playerDescriptor);
    }

    @Test
    public void testGetPlayer() {
        assertNotNull(this.playerDescriptor.getPlayer());
    }

    @Test
    public void testGetPosition() {
        assertNotNull(this.playerDescriptor.getPosition());
    }

}

