**1.run()与start()的区别？**  
调用run()等于调用一个普通方法，还是由当前线程来执行。start()会启动一个新的线程去运行run()。  
调用start()之后并不会立即执行run()，而是等合适的时候(获得CPU时间片后)去执行。  
**2.volatile**  
a.每次会去主内存中读取共享变量的最新值，并将其刷新到本地内存中  
b.线程修改了本地内存中变量的值后，会立即将其刷新到主内存中  
**3.synchronized**  
a.修饰示例方法。对当前实例加锁  
b.修饰静态方法。对类对象加锁  
c.修饰代码块。对指定对象(实例或类对象)加锁  
synchronized默认已经实现volatile的功能，它是互斥锁、可重入锁、不可中断锁、非公平锁。  