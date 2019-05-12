package com.bigsea;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailTestOne {
    public static void main(String[] args) throws MessagingException {
        String fromEmail = "****@qq.com";
        String[] toEmails = {"****@qq.com", "****@qq.com", "****@qq.com"};
        String username = "****@qq.com";
        String password = "****";

        String title = "测试邮件标题";
        String content = "测试内容";
        EmailTestOne emailTestOne = new EmailTestOne();
        emailTestOne.sendMail(fromEmail, toEmails, username, password,title, content);

    }

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
        message.setText(content);
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect(username, password);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
