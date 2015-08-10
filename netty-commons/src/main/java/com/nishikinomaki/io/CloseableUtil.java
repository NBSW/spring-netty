package com.nishikinomaki.io;

import com.nishikinomaki.log.Log;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * io关闭工具
 *
 * @author Jax.Gong
 * @date:2015年5月11日
 */
public class CloseableUtil {

    static Logger logger = Log.getLogger();

    public static void close(Closeable close) {
        if (close != null) {
            try {
                close.close();
            } catch (IOException e) {
                logger.error("io close error", e);
            }
        }
    }
}
