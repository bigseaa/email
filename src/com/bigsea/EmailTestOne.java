package com.bigsea;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Properties;

/**
 * 读取html文件，并以此为模板作为发送邮件的内容
 * @Author yuhai
 * @Date 2019-05-26
 */
public class EmailTestOne {
    public static void main(String[] args) throws MessagingException {
        String fromEmail = "****@qq.com";
        String[] toEmails = {"****@qq.com", "****@qq.com", "****@qq.com"};
        String username = "****@qq.com";
        String password = "****";

        String title = "测试邮件标题";
        EmailTestOne emailTestOne = new EmailTestOne();
        String content = emailTestOne.readTemplate();
        content = content.replace("${time}", "2012-12-12");
        emailTestOne.sendMail(fromEmail, toEmails, username, password,title, content);
    }

    /**
     * 发送邮件
     * @param fromEmail 发件人
     * @param toEmails 收件人
     * @param username 发件人用户名
     * @param password 密码
     * @param title 标题
     * @param content 内容
     * @throws MessagingException
     */
    public void sendMail(String fromEmail, String[] toEmails, String username, String password,
                         String title, String content) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress(fromEmail));
        // 设置收件人邮箱地址
        InternetAddress[] internetAddresses = new InternetAddress[toEmails.length];
        for (int i = 0; i < toEmails.length; i++) {
            internetAddresses[i] = new InternetAddress(toEmails[i]);
        }
        message.setRecipients(Message.RecipientType.TO, internetAddresses);
        //message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));//一个收件人
        // 设置邮件标题
        message.setSubject(title);
        // 设置邮件内容
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(content, "text/html;charset=utf-8");
        multipart.addBodyPart(bodyPart);
        message.setContent(multipart);
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect(username, password);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * 读取模板文件
     * @return 模板文件字符串
     */
    public String readTemplate() {
        StringBuffer sb = new StringBuffer();
        InputStreamReader in = null;
        BufferedReader br = null;
        // 加载模板
        String tamplatePath = this.getClass().getClassLoader().getResource("").getPath();
        tamplatePath = tamplatePath + "/com/bigsea/email_temp.html";
        // 读取模板中的数据
        File file = new File(tamplatePath);
        try {
            in = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(in);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
