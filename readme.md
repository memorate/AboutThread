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
**4.线程池的五中状态** 
a.RUNNING，可以接受新任务，以及对已添加的任务进行处理。线程池一旦被创建，就处于此状态;  
b.SHUTDOWN，不接受新任务，但能处理已添加的任务。调用 shutdown() 后由 RUNNING ——> SHUTDOWN;  
c.STOP，不接受新任务，不处理已添加的任务，并且会中断正在处理任务的线程。调用 shutdownNow() 后由 RUNNING/SHUTDOWN ——> STOP;  
d.TIDYING，所有任务已终止，且 worker 数量为 0，当状态变为 TIDYING 会执行 terminated()。SHUTDOWN/STOP ——> TIDYING;  
e.TERMINATED，线程池完全终止。执行过 terminated() 后由 TIDYING ——> TERMINATED;  
**5.阻塞队列**  
a.ArrayBlockingQueue，基于数组结构的有界阻塞队列，按照先进先出进行排序，new时指定大小;  
b.LinkedBlockingQueue，基于单链表结构的有界阻塞队列，按照先进先出进行排序，默认大小为Integer.MAX_VALUE。吞吐量高于ArrayBlockingQueue;  
c.LinkedBlockingDeque，基于双链表结构的有界阻塞队列，按照先进先出进行排序，默认大小为Integer.MAX_VALUE;  
d.LinkedTransferQueue，基于单链表结构的无界阻塞队列，按照先进先出进行排序。生产者会一直阻塞，直到所添加到队列的元素被某个消费者消费;  
d.SynchronousQueue，不储存元素，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直阻塞。吞吐量高于LinkedBlockingQueue;  
e.PriorityQueue，基于数组结构的有界阻塞队列，进入队列的元素按照优先级进行排序;  
f.DelayQueue，基于PriorityQueue的延时队列，队列中的元素只有当timeout时间过之后才能取出;  
**6.线程池四种拒绝策略**  
a.CallerRunsPolicy，由调用线程池的线程自己去执行此任务  
b.AbortPolicy，默认使用此策略。抛弃任务，并抛出 RejectedExecutionException 异常  
c.DiscardPolicy，抛弃任务，不抛出异常  
d.DiscardOldestPolicy，抛弃最老的任务(队列最头部，poll())，不抛出异常  