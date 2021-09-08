```java
 public final void acquire(int arg) {
        //尝试获取同步state(置1)，成功则结束
     	if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);//将当前线程以独享模式创建节点
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {//如果当前tail不为空
            node.prev = pred;//将当前节点的前区指向tail
            if (compareAndSetTail(pred, node)) {//cas设置当前节点为新的tail
                pred.next = node;//tail的next指向当前节点
                return node;
            }
        }
        enq(node);
        return node;
    }

//自旋的方式将节点加入同步队列(保证成功加入同步队列)
private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { //如果当前尾节点为空
                if (compareAndSetHead(new Node()))//实例化一个head节点
                    tail = head;//尾节点指向头节点
            } else {
                node.prev = t;//当前节点前驱指向尾节点
                if (compareAndSetTail(t, node)) {//cas设置当前节点为新的tail
                    t.next = node;
                    return t;
                }
            }
        }
    }

final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();//当前节点的前驱
                if (p == head && tryAcquire(arg)) {//前驱节点为head节点，则可以尝试获取state
                    setHead(node);//将当前节点设置为head节点(节点信息清空，只保留后继信息)
                    p.next = null; //提醒GC回收原head节点
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;//被中断唤醒，则设置标志位
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            //前驱节点已经设置了SIGNAL(已经告知前驱线程释放锁后unpark当前节点)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {//前驱节点被取消了，更新前驱
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);//一直跳过当前前驱直到一个有效前驱
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);//挂起
        return Thread.interrupted();//判断是被中断还是被唤醒
    }
```

