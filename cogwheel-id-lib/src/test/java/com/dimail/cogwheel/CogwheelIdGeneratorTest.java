package com.dimail.cogwheel;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CogwheelIdGeneratorTest {

    @ParameterizedTest
    @MethodSource("provideOrderedStream")
    public void checkNotEquals(Long id1, Long id2) {
        assertNotEquals(id1, id2);
    }

    @ParameterizedTest
    @MethodSource("provideOrderedStream")
    public void checkOrder(Long id1, Long id2) {
        int compareId = id1.compareTo(id2);
        if (compareId > 0) {
            compareId = 1;
        } else if (compareId < 0) {
            compareId = -1;
        }

        String base1 = Text32.text32(id1);
        String base2 = Text32.text32(id2);
        int compareBase = base1.compareTo(base2);
        if (compareBase > 0) {
            compareBase = 1;
        } else if (compareBase < 0) {
            compareBase = -1;
        }
        assertEquals(compareId, compareBase);
    }

    private static Stream<Arguments> provideOrderedStream() {
        CogwheelIdGenerator cogwheelIdGenerator = CogwheelIdGenerator.randomOf(UUID.randomUUID().toString());
        return IntStream.range(0, 10000).mapToObj(i -> getIds(cogwheelIdGenerator));
    }

    private static Arguments getIds(CogwheelIdGenerator cogwheelIdGenerator) {
        return Arguments.arguments(
                cogwheelIdGenerator.uniqueLong(),
                cogwheelIdGenerator.uniqueLong()
        );
    }

}
