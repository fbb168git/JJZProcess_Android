package com.fbb.jjzprocess.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {
//    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static String AndroidAngent = "Mozilla/5.0 (Linux; Android 6.0.1; MI 5s Plus Build/MXB48T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36";
    private static String PCAngent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";

    public static String sendGet(String url, String param) {
        return sendGet(url, param, "");
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String charset) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
//            	LogUtil.screenLog(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            if (charset == null || charset.equalsIgnoreCase("")) {
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), charset));
            }

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("[EXCEPTION] http get ", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                logger.error("[EXCEPTION] http get ", e);
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("[EXCEPTION] http post ", e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
//                logger.error("[EXCEPTION] http post ", ex);
            }
        }
        return result;
    }

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("[EXCEPTION] https trustAllHosts ", e);
        }
    }

    /**
     * 发送https请求(忽略证书)
     *
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendHtpps(String url, String params) {
        return sendHtpps(url, params, null, true);
    }

    public static String sendHtpps(String url, String params, String referer) {
        return sendHtpps(url, params, referer, true);
    }

    public static String sendHtpps(String url, String param, String referer, boolean useMobile) {
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn;
        try {
            trustAllHosts();
            URL realUrl = new URL(url);
            //通过请求地址判断请求类型(http或者是https)
            if (realUrl.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", useMobile ? AndroidAngent : PCAngent);
            conn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
            if (referer != null && !"".equalsIgnoreCase(referer)) {
                conn.setRequestProperty("Referer", referer);
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("responseCode:" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("[EXCEPTION] https ", e);
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
//                logger.error("[EXCEPTION] https ", ex);
            }
        }
        return result;
    }

    public static String sendHtpps2(String url, String param, String referer) {
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn;
        try {
            trustAllHosts();
            URL realUrl = new URL(url);
            //通过请求地址判断请求类型(http或者是https)
            if (realUrl.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 设置通用的请求属性
//            conn.setRequestProperty("Host", "enterbj.zhongchebaolian.com");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Pragma", "no-cache");
//            conn.setRequestProperty("Cache-Control", "no-cache");
//            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
//            conn.setRequestProperty("Origin", "https://enterbj.zhongchebaolian.com");
//            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            //TODO *
            conn.setRequestProperty("User-Agent", AndroidAngent);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            //TODO *
            conn.setRequestProperty("Referer", referer);
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//            conn.setRequestProperty("Accept-Language", "zh-CN,en-US;q=0.8");
//            conn.setRequestProperty("Cookie", "JSESSIONID=8C89A5E61468FF70C270BB38A94249EF; UM_distinctid=15f0a2370ea99-0fa8354e2f4bf5-7b353118-43113-15f0a2370ec11; CNZZDATA1260761932=356835291-1509984145-https%253A%252F%252Fenterbj.zhongchebaolian.com%252F%7C1514287695");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode:" + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("[EXCEPTION] https ", e);
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
//                logger.error("[EXCEPTION] https ", ex);
            }
        }
        return result;
    }


}
