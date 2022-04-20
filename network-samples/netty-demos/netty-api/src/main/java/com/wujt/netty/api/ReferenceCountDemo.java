package com.wujt.netty.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.IllegalReferenceCountException;

/**
 * @author gow
 * @date 2022/1/28
 */
public class ReferenceCountDemo {
    public static void main(String[] args) {
        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        testRelease(allocator);
        testIncrReference(allocator);
        testDuplicate(allocator);

        ByteBuf byteBuf = allocator.directBuffer(10);
        byteBuf.writeBytes(new byte[]{0,1});
        ByteBuf duplicate = byteBuf.duplicate();
        int bytes = duplicate.readableBytes();
        ByteBuf buffer = allocator.buffer(bytes);
        buffer.writeBytes(duplicate);
        System.out.printf("duplicate refCnt=%d",duplicate.refCnt());
        buffer.release();
        assert buffer.refCnt()==0;
    }

    /**
     * ByteBuf.duplicate(), ByteBuf.slice() and ByteBuf.order(ByteOrder) create a derived buffer
     * which shares the memory region of the parent buffer. A derived buffer does not have its own reference count,
     * but shares the reference count of the parent buffer.
     * @param allocator
     */
    private static void testDuplicate(PooledByteBufAllocator allocator) {
        ByteBuf parent = allocator.directBuffer();
        ByteBuf derived = parent.duplicate();

// Creating a derived buffer does not increase the reference count.
        assert parent.refCnt() == 1;
        assert derived.refCnt() == 1;
        derived.release();
        assert derived.refCnt() == 0;
        assert parent.refCnt() == 0;
    }

    private static void testIncrReference(PooledByteBufAllocator allocator) {
        ByteBuf buf = allocator.directBuffer();
        assert buf.refCnt() == 1;

        ByteBuf retain = buf.retain();
        assert buf.refCnt() == 2;

        boolean destroyed = buf.release();
        assert !destroyed;
        assert buf.refCnt() == 1;
        assert retain.refCnt() == 1;
        retain.release();
        assert retain.refCnt() == 0;
    }

    private static void testRelease(PooledByteBufAllocator allocator) {
        // init
        ByteBuf buf = allocator.directBuffer();
        assert buf.refCnt() == 1;
        // release() returns true only if the reference count becomes 0.
        boolean destroyed = buf.release();
        assert destroyed;
        assert buf.refCnt() == 0;
        assert buf.refCnt() == 0;
        try {
            buf.writeLong(0xdeadbeef);
            throw new Error("should not reach here");
        } catch (IllegalReferenceCountException e) {
            System.out.println("IllegalReferenceCountException occur");
            // Expected
        }
    }
}
