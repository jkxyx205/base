package com.base.mail;

import java.io.File;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailTest {
	@Test
	public void testSend() throws MessagingException {
		 JavaMailSenderImpl senderImpl = new JavaMailSenderImpl(); 
		    
		    //设定mail server 
		    senderImpl.setHost("smtp.163.com"); 
		    
		    //建立邮件消息,发送简单邮件和html邮件的区别 
		    MimeMessage mailMessage = senderImpl.createMimeMessage(); 
		    MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage); 
		             
		    //设置收件人，寄件人 
		    messageHelper.setTo("154894898@qq.com"); 
		    messageHelper.setFrom("jkxyx205@163.com"); 
		    messageHelper.setSubject("测试HTML邮件！"); 
		    //true 表示启动HTML格式的邮件 
		    messageHelper.setText("<html><head></head><body><h1>hello!!spring html Mail</h1></body></html>",true); 
		    senderImpl.setUsername("jkxyx205@163.com") ; // 根据自己的情况,设置username
		    senderImpl.setPassword("123456xx") ; // 根据自己的情况, 设置password
		    Properties prop = new Properties() ;
		    prop.put("mail.smtp.auth", "true") ; // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		    prop.put("mail.smtp.timeout", "25000") ; 
		    senderImpl.setJavaMailProperties(prop); 
		    //发送邮件 
		    senderImpl.send(mailMessage); 
		    
		    System.out.println("邮件发送成功.."); 
		 } 
	
	@Test
	public void testsax() throws DocumentException {
		SAXReader reader = new SAXReader();
		long a = System.currentTimeMillis();
        Document document = reader.read(new File("f:\\ScheduleJob.hbm.xml"));
        long b = System.currentTimeMillis();
        
        System.out.println(b-a + "......................");
	}
}
