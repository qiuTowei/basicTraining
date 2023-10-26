package com.example.basictraining.nio;

import java.nio.*;

/**
 * @Author qw
 * @Date 2023/10/26 13:22
 * @Version 1.0
 */
public class BufferDemo {
        /**
         * Buffer 中四个成员变量的含义
         * 1.capacity：容量，Buffer可以存储的最大数据量，Buffer创建时设置且不可改变；
         * 2.limit：Buffer中可以读/写数据的边界。
         *  写模式：limit代表最多能写入的数据，一般等于capacity；
         *  读模式：limit等于Buffer中实际写入的数据大小。
         * 3.position：位置，下一个可以被读写的数据的位置（索引）。从写操作模式到读操作模式切换的时候（flip），
         * position都会归零，这样就可以从头读写。
         * 4.mark：标记，buffer允许将位置直接定位到该标记处，这是一个可选属性。
         * 四者关系：0<= mark <= position <= limit <= capacity
         */
    public static void main(String[] args) {
        // 分配一个容量为8的CharBuffer，Buffer默认写模式
        CharBuffer buffer = CharBuffer.allocate(8);
        System.out.println("初始状态");
        printState(buffer);
        // 写入
        buffer.put('a').put('b').put('c');
        System.out.println("写入三个字符后的状态：");
        printState(buffer);
        // 调用flip，准备读取
        buffer.flip();
        System.out.println("调用 flip方法后的状态：");
        printState(buffer);

        while (buffer.hasRemaining()) {
            // 读取
            System.out.println(buffer.get());
        }
        // 清空缓冲区
        buffer.clear();
        System.out.println("调用 clear方法后的状态：");
        printState(buffer);

    }
    static void printState(Buffer buffer) {
        System.out.print("capacity:" + buffer.capacity());
        System.out.print(", limit:" + buffer.limit());
        System.out.print(", position:" + buffer.position());
        System.out.println(", mark 开始读取字符:" + buffer.mark());
    }
}
