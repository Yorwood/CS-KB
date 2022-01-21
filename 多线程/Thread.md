##### Thread

- 创建

  - Thread

    ```java
    public class M1 extends Thread{
        public void run(){
            //重写
        }
        public static void main(String[] args){
            M1 m = new M1();
            m.start();//启动线程
        }
    
    ```

  - Runnable

    ```java
    public class M2 implement Runnable{
        public void run(){
            //重写
        }
        public static void main(String[] args){
            Thread t = new Thread(new M2());
            t.start();//启动线程
        }
    }
    ```

    使用了面向接口，将任务与线程进行分离，有利于解耦;

  - Callable

    ```java
    public class M3 implement Callable<String>{
        public String call(){
            //重写
        }
        public static void main(String[] args){
            M3 m = new M3();
            FutureTask<String> task = new FutureTask<>(m);
            Thread t = new Thread(task);
            t.start();//启动线程
        }
    }
    ```

    call方法具有返回值；

- 生命周期

  生命周期：new -> runnable -> waiting -> blocked -> terminated；

  <img src="C:\Users\18160\Desktop\YW\JAVA\KB\CS-KB\多线程\thread-states.png" style="zoom:50%;" />

- 单例模式

  - 饿汉实现

    ```java
    public class MyObj{
        public static MyObj myObj =  new MyObj();
       	private MyObj(){}
        public MyObj getInstance(){
            return myObj;
        }
    }
    ```

    

  - 懒汉实现

    ```java
    public class MyObj{
        public volatile static MyObj myObj;
       	private MyObj(){}
        public MyObj getInstance(){
            if(myObj != null){
                return myObj;
            }
            else{
                synchronized(MyObj.class){
                    if(myObj == null){//双检查
                        myObj = new MyObj();//volatile可见性、禁止重排
                    }
                }
            }
            return myObj;
        }
    }
    ```

- 交替打印

  ```java
  public class M2 implement Runnable{
      private int num  = 1;
      public void run(){
          while(true){
              synchronized(this){
                  notify();
                  if(number <= 100){
                      System.out.println(Thread.currentThread().getName()+": " + num);
                      num++;
                      try{
                          wait();
                      }catch(InterruptExecption e){
                          e.printStackTrace();
                      }
                  }
                  else{
                      break;
                  }
              }
          }
      }
      public static void main(String[] args){
          M2 m = new M2();
          Thread t1 = new Thread(m);
          Thread t2 = new Thread(m);
          t1.start();//启动线程
          t2.start();//启动线程
      }
  }
  ```

  
