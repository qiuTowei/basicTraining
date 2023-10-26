package com.example.basictraining.nio;

import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author qw
 * @Date 2023/10/26 13:59
 * @Version 1.0
 */
public class ChannelDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile reader = null;
        ClassPathResource resource = new ClassPathResource("test.txt");
        try {
            reader = new RandomAccessFile(resource.getFile().getPath(), "r");
            FileChannel channel = reader.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char)buffer.get());
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
                assert reader != null;
                reader.close();
        }

    }
}
