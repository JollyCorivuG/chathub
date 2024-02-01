package bupt.edu.jhc.chathub.common.utils.context;

/**
 * @Description: 记录请求上下文
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/16
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> tl = new ThreadLocal<>();

    /**
     * 保存请求信息
     * @param info
     */
    public static void save(RequestInfo info){
        tl.set(info);
    }

    /**
     * 获取请求信息
     * @return 请求信息
     */
    public static RequestInfo get(){
        return tl.get();
    }

    /**
     * 移除请求信息
     */
    public static void remove(){
        tl.remove();
    }
}
