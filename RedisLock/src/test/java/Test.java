public class Test {
    public static void main(String[] args) throws Exception {
        int i=10;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("aaa");
                }
            }
        });
        thread.setDaemon(true);  //设置为守护线程
        thread.start();
        while (true){
            i--;
            if(i==0){
                throw new Exception("出异常");
            }
            Thread.sleep(1000);
        }
    }
}
