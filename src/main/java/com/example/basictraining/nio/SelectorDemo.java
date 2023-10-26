package com.example.basictraining.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author qw
 * @Date 2023/10/26 14:18
 * @Version 1.0
 */
public class SelectorDemo {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssChannel = null;
        try {
             ssChannel = ServerSocketChannel.open();
             ssChannel.configureBlocking(false);
             ssChannel.socket().bind(new InetSocketAddress(8080));

            Selector selector = Selector.open();
            // 将ServerSocketChannel 注册到selector并监听 OP_ACCEPT 事件
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        // 处理连接事件
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        // 将客户端通道注册到 Selector并监听 OP_READ事件
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        // 处理读事件
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int byteRead = client.read(buffer);

                        if (byteRead > 0) {
                            buffer.flip();
                            System.out.println("收到数据：" + new String(buffer.array(),0,byteRead));
                            // 将客户端注册到 Selector 并监听 OP_WRITE事件
                            client.register(selector, SelectionKey.OP_WRITE);
                        } else if (byteRead < 0) {
                            client.close();
                        }
                    } else if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.wrap("hello client!".getBytes());
                        client.write(buffer);
                        // 将客户端注册到 Selector 并监听 OP_READ事件
                        client.register(selector,SelectionKey.OP_READ);
                    }
                    keyIterator.remove();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert ssChannel != null;
            ssChannel.close();
        }
    }
}
