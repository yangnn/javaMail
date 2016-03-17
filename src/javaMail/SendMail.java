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
		String to = "yangnn_os@sari.ac.cn";//�ռ���
		String from = "OA_Feedback@sari.ac.cn";//������
		String password = "sari112233";//��������������
		String host = "smtp.cstnet.cn";//����������
		boolean debug = true;//�Ƿ���Ҫdebug
		
		String filename = "D:\\books\\javamail.txt";
		
		//���÷����ʼ��Ļ�������
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.debug", debug);
		
		//���������ʼ�������������������Ϣ��Session����
		//Session�ඨ������Ӧ�ó�������Ļ�����Ϣ��
		//Session���������Щ�ʼ���Ϣ���� �����ʼ��շ���Transport��Store�����Լ��ͻ��˴���Message����ʱ�ṩ��Ϣ֧��
		Session session = Session.getInstance(props);
		
		try {
			//���������ʼ����ݵ�Message����
			//message���󣺴����ͽ����ʼ��ĺ���API������ʵ���������һ������ʼ�
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("java mail ����");
			msg.setSentDate(new Date());
			
			//����Transport�������ӷ�����
			//Transport�Ƿ����ʼ��ĺ���API�࣬����ʵ����������� ʵ��ĳ���ʼ�����Э����ʼ����Ͷ�������SMTPЭ��
			Transport ts = session.getTransport(); 
			ts.connect("smtp.cstnet.cn", "OA_Feedback@sari.ac.cn", "sari112233");
			
			//����Message
			//���ͼ��ʼ�
			setTextContent(msg);
			msg.saveChanges();
			ts.sendMessage(msg, msg.getAllRecipients());
			
			//������Ӹ������ʼ�
			setFileAsAttachment(msg,filename);
			msg.saveChanges();
			ts.sendMessage(msg, address);
			//�ر�����
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
	
	//���ͼ򵥵ģ�ֻ�����ı����ʼ�
	public static void setTextContent(Message msg) throws MessagingException{
		String content = "���ͼ򵥵ģ�ֻ�����ı����ʼ�";
		msg.setContent(content, "text/html;charset=UTF-8");
	}
	
	//������Ӹ������ʼ�
	public static void setFileAsAttachment(Message msg,String filename) throws MessagingException, IOException{
		//MimeBodyPart��ʾ�ʼ���һ��MIME��Ϣ
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText("���Է�����Ӹ������ʼ�");
		
		MimeBodyPart mbp2 = new MimeBodyPart();
		mbp2.attachFile(filename);
		
		//Multipart���ʾһ���ɶ��MIME��Ϣ��ϳɵ����MIME��Ϣ
		//����Multipart����BodyParts��ӵ�Multipart
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		//��Multipart��Ϊ��Ϣ������
		msg.setContent(mp);
	}
}
