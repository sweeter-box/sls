package link.s.sls.utils;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public final class IpUtils {

  public  static final String REGION = "内网IP|内网IP";
  private static final String UNKNOWN = "unknown";

    /**
     * 获取ip地址
     */
    public static String getIp(ServerHttpRequest request) {
        HttpHeaders httpHeaders = request.getHeaders();
        String ip = httpHeaders.getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getHostString();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if  (localhost.equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getGeoInfo(String ip) {
        DbSearcher searcher = null;
        try {
            String path = "ip2region/ip2region.db";
            String name = "ip2region.db";
            DbConfig config = new DbConfig();
            ClassLoader classLoader = IpUtils.class.getClassLoader();
            URL resource = classLoader.getResource(name);
            searcher = new DbSearcher(config, resource.getFile());
            Method method;
            method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock;
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String address = dataBlock.getRegion().replace("0|","");
            char symbol = '|';
            if(address.charAt(address.length()-1) == symbol){
                address = address.substring(0,address.length() - 1);
            }
            return address.equals(REGION)?"内网IP":address;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(searcher!=null){
                try {
                    searcher.close();
                } catch (IOException ignored) {
                }
            }

        }
        return "";
    }
}
