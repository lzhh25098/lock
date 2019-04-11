package cn.enjoy;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

public class ZKDistrbuteLock extends ZKAbstractLock {
    private CountDownLatch countDownLatch = null;
    boolean tryLock() {
        try {
            zkClient.createEphemeral(PATH);
            return true;

        }catch (Exception e){
             new RuntimeException("获取锁失败!");
            return false;
        }
    }

    void waitLock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {

            }

            public void handleDataDeleted(String s) throws Exception {
                if (countDownLatch != null){
                    countDownLatch.countDown();
                }

            }
        };
        //注册事件
        zkClient.subscribeDataChanges(PATH,iZkDataListener);
        if(zkClient.exists(PATH)){
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(PATH,iZkDataListener);

    }


    public void unlock() {
     if (zkClient != null){
         zkClient.delete(PATH);
         zkClient.close();
         System.out.println("释放锁资源...");
     }
    }
}
