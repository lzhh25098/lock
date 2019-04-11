package cn.enjoy;

import org.I0Itec.zkclient.ZkClient;

public abstract class ZKAbstractLock implements Lock {
    //zk连接地址
    private static final String CONN = "127.0.0.1:2181";
    //连接zk
    protected ZkClient zkClient = new ZkClient(CONN);
    //路径
    protected static final String PATH = "/lock";
    protected static final String PATH2 = "/lock2";

    public void getLock(){
        if (tryLock()){
            System.out.println("####正在获取锁资源####");
        }else {
            //等待
            waitLock();
            //重新获取锁资源
            getLock();
        }
    }
     abstract boolean tryLock();

     abstract void waitLock();

}
