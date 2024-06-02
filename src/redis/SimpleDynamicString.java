package redis;

public class SimpleDynamicString {
    private char[] buf;
    private int len;
    private int alloc;

    public SimpleDynamicString(String initialStr) {
        this.len = initialStr.length();
        this.alloc = this.len + 16; // 预分配一些额外空间
        this.buf = new char[this.alloc];
        System.arraycopy(initialStr.toCharArray(), 0, this.buf, 0, this.len);
    }

    public int length() {
        return this.len;
    }

    public String toString() {
        return new String(this.buf, 0, this.len);
    }

    public void append(String str) {
        int newLen = this.len + str.length();
        if (newLen > this.alloc) {
            // 扩展空间，扩展为新长度的两倍，避免频繁扩展
            this.alloc = newLen * 2;
            char[] newBuf = new char[this.alloc];
            System.arraycopy(this.buf, 0, newBuf, 0, this.len);
            this.buf = newBuf;
        }
        System.arraycopy(str.toCharArray(), 0, this.buf, this.len, str.length());
        this.len = newLen;
    }

    public void clear() {
        this.len = 0;
    }

    public void removeLastNChars(int n) {
        if (n >= this.len) {
            this.len = 0;
        } else {
            this.len -= n;
        }
    }

    public static void main(String[] args) {
        SimpleDynamicString sds = new SimpleDynamicString("Hello");
        System.out.println("Initial string: " + sds.toString());
        sds.append(", world!");
        System.out.println("After append: " + sds.toString());
        sds.removeLastNChars(7);
        System.out.println("After removing last 7 characters: " + sds.toString());
        sds.clear();
        System.out.println("After clear: " + sds.toString());
    }
}
