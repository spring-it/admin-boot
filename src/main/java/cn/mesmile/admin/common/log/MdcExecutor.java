package cn.mesmile.admin.common.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.concurrent.Executor;

/**
 * @author zb
 * @Description 解决线程异步场景下RequestId的打印问题
 */
@Slf4j
public class MdcExecutor implements Executor {

    private Executor executor;

    public MdcExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable command) {
        final String requestId = MDC.get("REQUEST_ID");
        executor.execute(() -> {
            MDC.put("REQUEST_ID", requestId);
            try {
                command.run();
            } finally {
                MDC.remove("REQUEST_ID");
            }
        });
    }
}
