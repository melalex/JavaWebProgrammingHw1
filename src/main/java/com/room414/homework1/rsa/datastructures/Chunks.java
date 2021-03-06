package com.room414.homework1.rsa.datastructures;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Data structure to store byte array in byteChunks
 *
 * Created by alexander on 02/02/17.
 */
public class Chunks {
    private List<byte[]> byteChunks;

    private Chunks() {

    }

    public byte[] getChunk(int index) {
        byte[] chunk = byteChunks.get(index);
        return Arrays.copyOf(chunk, chunk.length);
    }

    public byte getByte(int chunkNumber, int byteNumber) {
        return byteChunks.get(chunkNumber)[byteNumber];
    }

    public int getByteCount() {
        return byteChunks.stream().mapToInt(b -> b.length).reduce(0, (a, b) -> a + b);
    }

    public static Chunks createChunks(byte[] source, int chunkSize) {
        return createChunks(source, chunkSize, b -> b);
    }

    /**
     * Create's new Chunks object
     *
     * @param source byte array that should be divide on byteChunks
     * @param chunkSize max size of each chunk
     * @param function function that should be called on all byteChunks
     * @return new Chunks object
     * @throws IllegalArgumentException if chunkSize less than or equal to 0 ({@code chunkSize <= 0})
     */
    public static Chunks createChunks(byte[] source,
                                      int chunkSize,
                                      Function<? super byte[], ? extends byte[]> function) {
        if (chunkSize <= 0)
            throw new IllegalArgumentException("chunkSize must be greater then 0. Got " + chunkSize);

        Chunks newChunk = new Chunks();
        int sourceLength = source.length;
        int chunksCount = (source.length + chunkSize - 1) / chunkSize;

        newChunk.byteChunks = IntStream.
                range(0, chunksCount).
                mapToObj(n -> {
                    byte[] result;

                    if (sourceLength > (n + 1) * chunkSize) {
                        result = Arrays.copyOfRange(source, n * chunkSize, (n + 1) * chunkSize);
                    } else {
                        result = Arrays.copyOfRange(source, n * chunkSize, sourceLength);
                    }

                    return function.apply(result);
                }).
                collect(Collectors.toList());

        return newChunk;
    }

    /**
     * Apply function to all byteChunks
     *
     * @param function function that should be called on all byteChunks
     * @return new Chunks object that store functions result
     */
    public Chunks applyFunction(Function<? super byte[], ? extends byte[]> function) {
        Chunks result = new Chunks();
        LinkedList<byte[]> newChunks = new LinkedList<>();
        result.byteChunks = newChunks;

        byteChunks.forEach(b -> newChunks.addLast(function.apply(b)));

        return result;
    }

    public byte[] asByteArray() {
        byte[] result = new byte[getByteCount()];
        int i = 0;

        for (byte[] chunk : byteChunks) {
            for (byte b : chunk) {
                result[i++] = b;
            }
        }

        return result;
    }
}
