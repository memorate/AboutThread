package anchor.thread.util;

import java.lang.management.ManagementFactory;

/**
 * @author Anchor
 */
public class CommonUtil {
    public static String getThreadPid(){
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

}
