package eu.okaeri.commonstest;

import eu.okaeri.commons.indexedset.IndexedLinkedHashSet;
import eu.okaeri.commons.indexedset.IndexedSet;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestIndexedSet {

    @Test
    public void test_indexedset_of() {

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        IndexedSet<Player, UUID> set = IndexedSet.of(Player::getId, player1, player2);

        assertEquals(IndexedLinkedHashSet.class, set.getClass());
        assertTrue(set.size() == 2);
        assertEquals(set.get(player1.getId()), player1);
        assertEquals(set.get(player2.getId()), player2);
    }

    @Test
    public void test_indexedset_builder_default() {

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        IndexedSet<Player, UUID> set = IndexedSet.builder(Player.class, UUID.class)
                .keyFunction(Player::getId)
                .add(player1)
                .add(player2)
                .build();

        assertEquals(IndexedLinkedHashSet.class, set.getClass());
        assertTrue(set.size() == 2);
        assertEquals(set.get(player1.getId()), player1);
        assertEquals(set.get(player2.getId()), player2);
    }

    @Test
    public void test_indexedset_builder_hashset() {

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        IndexedSet<Player, UUID> set = IndexedSet.builder(Player.class, UUID.class)
                .keyFunction(Player::getId)
                .add(player1)
                .add(player2)
                .build();

        assertEquals(IndexedLinkedHashSet.class, set.getClass());
        assertTrue(set.size() == 2);
        assertEquals(set.get(player1.getId()), player1);
        assertEquals(set.get(player2.getId()), player2);
    }

    @Data
    class Player {
        private final UUID id = UUID.randomUUID();
        private final String name;
    }
}
