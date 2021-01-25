package anchor.thread.util;

import java.lang.management.ManagementFactory;

/**
 * @author Anchor
 */
public class CommonUtil {

    private CommonUtil() {
        throw new IllegalStateException("Util class can not be constructed!");
    }

    /**
     * 获取当前线程的 pid
     * @return pid
     */
    public static String getThreadPid(){
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
