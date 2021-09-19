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

    /**
     * Convert ipv4 address to Long Number by https://mkyong.com/java/java-convert-ip-address-to-decimal-number/
     * @param ipv4
     * @return
     */
    public static Long ipv4ToLong(String ipv4) {
        String[] ipv4InArray = ipv4.split("\\.");
        long result = 0;
        for (int i = 0; i < ipv4InArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipv4InArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
    }

    public static long ipv4ToLong2(String ipv4) {
        long result = 0;
        String[] ipAddressInArray = ipv4.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            // left shifting 24,16,8,0 and bitwise OR
            // 1. 192 << 24
            // 1. 168 << 16
            // 1. 1 << 8
            // 1. 2 << 0
            result |= ip << (i * 8);

        }

        return result;
    }

    /**
     *  Convert ipv4 Long Number address to ipv4 String
     * @param ipv4
     * @return
     */
    public static String longToIpv4(long ipv4) {
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0,Long.toString(ipv4 & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ipv4 = ipv4 >> 8;
        }
        return result.toString();
    }

    public static String longToIpv42(long ip) {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            // 1. 2
            // 2. 1
            // 3. 168
            // 4. 192
            sb.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                sb.insert(0, '.');
            }
            // 1. 192.168.1.2
            // 2. 192.168.1
            // 3. 192.168
            // 4. 192
            ip = ip >> 8;
        }
        return sb.toString();
    }

}
