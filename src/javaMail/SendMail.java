package javaMail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	public static void main(String args[]){
		String to = "yangnn_os@sari.ac.cn";//收件人
		String from = "XXX@sari.ac.cn";//发件人
		String password = "xxx";//发件人邮箱密码
		String host = "smtp.cstnet.cn";//服务器主机
		boolean debug = true;//是否需要debug
		
		String filename = "D:\\books\\javamail.txt";
		
		//配置发送邮件的环境属性
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.debug", debug);
		
		//创建包含邮件服务器的网络连接信息的Session对象
		//Session类定义整个应用程序所需的环境信息。
		//Session对象根据这些邮件信息构建 用于邮件收发的Transport和Store对象，以及客户端创建Message对象时提供信息支持
		Session session = Session.getInstance(props);
		
		try {
			//创建代表邮件内容的Message对象
			//message对象：创建和解析邮件的核心API，它的实例对象代表一封电子邮件
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("java mail 测试");
			msg.setSentDate(new Date());
			
			//创建Transport对象，链接服务器
			//Transport是发送邮件的核心API类，它的实例对象代表了 实现某个邮件发送协议的邮件发送对象，例如SMTP协议
			Transport ts = session.getTransport(); 
			ts.connect("smtp.cstnet.cn", "OA_Feedback@sari.ac.cn", "sari112233");
			
			//发送Message
			//发送简单邮件
			setTextContent(msg);
			msg.saveChanges();
			ts.sendMessage(msg, msg.getAllRecipients());
			
			//发送添加附件的邮件
			setFileAsAttachment(msg,filename);
			msg.saveChanges();
			ts.sendMessage(msg, address);
			//关闭连接
			ts.close();
		} catch (MessagingException mex) {
			 // Prints all nested (chained) exceptions as well
            mex.printStackTrace();
            // How to access nested exceptions
            while (mex.getNextException() != null) {
                // Get next exception in chain
                Exception ex = mex.getNextException();
                ex.printStackTrace();
                if (!(ex instanceof MessagingException)) break;
                else mex = (MessagingException)ex;
            }
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//发送简单的，只包含文本的邮件
	public static void setTextContent(Message msg) throws MessagingException{
		String content = "发送简单的，只包含文本的邮件";
		msg.setContent(content, "text/html;charset=UTF-8");
	}
	
	//发送添加附件的邮件
	public static void setFileAsAttachment(Message msg,String filename) throws MessagingException, IOException{
		//MimeBodyPart表示邮件的一个MIME信息
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText("测试发送添加附件的邮件");
		
		MimeBodyPart mbp2 = new MimeBodyPart();
		mbp2.attachFile(filename);
		
		//Multipart类表示一个由多个MIME消息组合成的组合MIME消息
		//创建Multipart，将BodyParts添加到Multipart
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		//将Multipart作为消息的内容
		msg.setContent(mp);
	}
}
