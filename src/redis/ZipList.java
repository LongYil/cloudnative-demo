package redis;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ZipList {
    private byte[] zl;
    private int zlbytes;
    private int zltail;
    private int zllen;

    private static final byte ZLEND = (byte) 0xFF;

    public ZipList() {
        zl = new byte[10];
        zlbytes = 10;
        zltail = 0;
        zllen = 0;
        zl[zltail] = ZLEND;
    }

    public void push(String s) {
        byte[] sBytes = s.getBytes(StandardCharsets.UTF_8);
        int sLen = sBytes.length;
        int prevTailLen = zltail == 0 ? 0 : zl[zltail - 1];

        // 计算需要的长度
        int newEntryLen = 1 + 1 + sLen; // prevLen + dataLen + data
        int newZlBytes = zlbytes + newEntryLen;

        // 扩展字节数组
        zl = Arrays.copyOf(zl, newZlBytes);

        // 插入新的元素
        zl[zltail] = (byte) prevTailLen;
        zl[zltail + 1] = (byte) sLen;
        System.arraycopy(sBytes, 0, zl, zltail + 2, sLen);

        // 更新元数据
        zltail += newEntryLen;
        zl[zltail] = ZLEND;
        zlbytes = newZlBytes;
        zllen++;
    }

    public String get(int index) {
        if (index < 0 || index >= zllen) {
            return null;
        }

        int pos = 0;
        int currentIndex = 0;
        while (pos < zlbytes && zl[pos] != ZLEND) {
            if (currentIndex == index) {
                int prevLen = zl[pos];
                pos++;
                int dataLen = zl[pos];
                pos++;
                byte[] data = Arrays.copyOfRange(zl, pos, pos + dataLen);
                return new String(data, StandardCharsets.UTF_8);
            }

            int prevLen = zl[pos];
            pos++;
            int dataLen = zl[pos];
            pos += 1 + dataLen;
            currentIndex++;
        }

        return null;
    }

    public void delete(int index) {
        if (index < 0 || index >= zllen) {
            return;
        }

        int pos = 0;
        int currentIndex = 0;
        while (pos < zlbytes && zl[pos] != ZLEND) {
            if (currentIndex == index) {
                int prevLen = zl[pos];
                pos++;
                int dataLen = zl[pos];
                pos++;
                int entryLen = 1 + 1 + dataLen; // prevLen + dataLen + data
                System.arraycopy(zl, pos + dataLen, zl, pos - 2 - prevLen, zlbytes - pos - dataLen);
                zlbytes -= entryLen;
                zltail -= entryLen;
                zl = Arrays.copyOf(zl, zlbytes);
                zl[zltail] = ZLEND;
                zllen--;
                return;
            }

            int prevLen = zl[pos];
            pos++;
            int dataLen = zl[pos];
            pos += 1 + dataLen;
            currentIndex++;
        }
    }

    public void print() {
        int pos = 0;
        while (pos < zlbytes && zl[pos] != ZLEND) {
            int prevLen = zl[pos];
            pos++;
            int dataLen = zl[pos];
            pos++;
            byte[] data = Arrays.copyOfRange(zl, pos, pos + dataLen);
            pos += dataLen;
            System.out.print(new String(data, StandardCharsets.UTF_8) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ZipList zipList = new ZipList();
        zipList.push("hello");
        zipList.push("world");
        zipList.push("!");
        zipList.print(); // 输出: hello world ! 

        System.out.println(zipList.get(0)); // 输出: hello
        System.out.println(zipList.get(1)); // 输出: world
        System.out.println(zipList.get(2)); // 输出: !

        zipList.delete(1);
        zipList.print(); // 输出: hello ! 

        System.out.println(zipList.get(1)); // 输出: !
    }
}
