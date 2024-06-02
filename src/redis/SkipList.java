package redis;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// 跳表节点类
class SkipListNode {
    int value;
    SkipListNode[] forwards;
    static Integer num = 0;
    public SkipListNode(int value, int level) {
        num++;
        this.value = value;
        this.forwards = new SkipListNode[level + 1];
    }
}

// 跳表类
public class SkipList {
    private static final int MAX_LEVEL = 6; // 最大层数
    private int levelCount = 1; // 跳表当前层数
    private SkipListNode head = new SkipListNode(0, MAX_LEVEL); // 跳表头节点
    private Random random = new Random(); // 随机数生成器

    // 查找节点
    public SkipListNode find(int value) {
        SkipListNode p = head;
        System.out.println("查找："+value);
        int searchNum = 1;
        // 从最上层开始查找
        for (int i = levelCount - 1; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].value < value) {
                System.out.println("第"+ i +"层:" + p.forwards[i].value);
                System.out.println("当前查找次数:" + (searchNum++) );
                p = p.forwards[i];
            }
        }
        // 如果找到节点值等于value，则返回节点，否则返回null
        if (p.forwards[0] != null && p.forwards[0].value == value) {
            return p.forwards[0];
        } else {
            return null;
        }
    }

    // 插入节点
    public void insert(int value) {
        int level = randomLevel(); // 确定插入节点的层数
        SkipListNode newNode = new SkipListNode(value, level);

        SkipListNode[] update = new SkipListNode[level + 1];
        SkipListNode p = head;

        // 从最上层开始查找，记录每层需要更新的节点
        for (int i = level; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].value < value) {
                p = p.forwards[i];
            }
            update[i] = p;
        }

        // 将新节点插入各层链表中
        for (int i = 0; i <= level; i++) {
            newNode.forwards[i] = update[i].forwards[i];
            update[i].forwards[i] = newNode;
        }

        // 更新跳表层数
        if (level > levelCount) {
            levelCount = level;
        }
    }

    // 删除节点
    public void delete(int value) {
        SkipListNode[] update = new SkipListNode[levelCount];
        SkipListNode p = head;

        // 从最上层开始查找，记录每层需要更新的节点
        for (int i = levelCount - 1; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].value < value) {
                p = p.forwards[i];
            }
            update[i] = p;
        }

        // 如果存在需要删除的节点，则删除节点并更新各层链表
        if (p.forwards[0] != null && p.forwards[0].value == value) {
            for (int i = levelCount - 1; i >= 0; i--) {
                if (update[i].forwards[i] != null && update[i].forwards[i].value == value) {
                    update[i].forwards[i] = update[i].forwards[i].forwards[i];
                }
            }
        }

        // 更新跳表层数
        while (levelCount > 1 && head.forwards[levelCount] == null) {
            levelCount--;
        }
    }

    // 随机生成插入节点的层数
    private int randomLevel() {
        int level = 0;
        while (random.nextDouble() < 0.5 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    // 打印跳表
    public void printSkipList() {
        for (int i = levelCount - 1; i >= 0; i--) {
            SkipListNode p = head;
            System.out.print("Level " + i + ": ");
            while (p.forwards[i] != null) {
                System.out.print(p.forwards[i].value + " ");
                p = p.forwards[i];
            }
            System.out.println();
        }
    }

    // 测试主函数
    public static void main(String[] args) {
        SkipList skipList = new SkipList();

        for (int i = 0; i < 10; i++) {
            skipList.insert(new Random().nextInt(100) );
        }

        skipList.printSkipList();

        System.out.println("节点个数:" + SkipListNode.num);
        int sNum = 243;
        System.out.println("Search for "+ sNum + (skipList.find(sNum) != null ? " Found" : "Not found"));
        sNum = 370;
        System.out.println("Search for " + sNum+ (skipList.find(370) != null ? " Found" : "Not found"));

        skipList.delete(68);
        skipList.delete(41);

        System.out.println("After deletion:");
        skipList.printSkipList();
    }
}
