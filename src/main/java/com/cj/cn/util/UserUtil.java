package com.cj.cn.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.cj.cn.pojo.User;
import com.cj.cn.response.ResultResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class UserUtil {
    private static Properties properties;

    static {
        properties = new Properties();
        InputStream ins = UserUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert ins != null;
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        String driver = properties.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    private static String loginRPC(String url, long mobile, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        //设置POST请求的参数
        StringEntity reqEntity = new StringEntity("mobile=" + mobile + "&password=" + password);
        //设置参数类型
        reqEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(reqEntity);

        CloseableHttpResponse response = null;
        response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
//        System.err.println(EntityUtils.toString(entity));     //该方法只能调用一次
        InputStream ins = entity.getContent();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = ins.read(buff)) != -1) {
            bos.write(buff, 0, len);
        }
        ins.close();
        bos.close();
        String responseStr = new String(bos.toByteArray());
        ResultResponse resultResponse = JsonUtil.strToObj(responseStr, ResultResponse.class);
        String token = (String) resultResponse.getData();
        System.err.println("token = " + token);
        response.close();
        httpClient.close();
        return token;
    }

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
            User user = new User()
                    .setId(13000000000L + i)
                    .setLoginCount(1)
                    .setNickname("user" + i)
                    .setRegisterDate(new Date())
                    .setLastLoginDate(new Date())
                    .setSalt("1a2b3c")
                    .setPassword("123456");
            users.add(user);
        }
        System.out.println("create user");

        //插入数据库
//        Connection conn = getConnection();
//        String sql = "insert into user(login_count, nickname, register_date, salt, password, id, last_login_date)values(?,?,?,?,?,?,?)";
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        for (int i = 0; i < users.size(); i++) {
//            User user = users.get(i);
//            pstmt.setInt(1, user.getLoginCount());
//            pstmt.setString(2, user.getNickname());
//            pstmt.setDate(3, new java.sql.Date(user.getRegisterDate().getTime()));
//            pstmt.setString(4, user.getSalt());
//            pstmt.setString(5, user.getPassword());
//            pstmt.setLong(6, user.getId());
//            pstmt.setDate(7, new java.sql.Date(user.getLastLoginDate().getTime()));
//            pstmt.addBatch();
//        }
//        pstmt.executeBatch();
//        pstmt.close();
//        conn.close();
//        System.out.println("insert to db");

        //登录并生成token
        String urlString = "http://localhost:8080/login/do_login";
        File file = new File("D:\\IDEAproject\\Seckill\\tokens.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String token = loginRPC(urlString, user.getId(), user.getPassword());
            System.err.println("create token : " + user.getId());
            String row = user.getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.err.println("write to file : " + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    public static void main(String[] args) throws Exception {
//        createUser(5000);
        loginRPC("http://localhost:8080/login/do_login", 17367117439l, "123456");
    }
}
