package javaMail;

/*
 * Copyright (c) 1996-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * sendfile will create a multipart message with the second
 * block of the message being the given file.<p>
 *
 * This demonstrates how to use the FileDataSource to send
 * a file via mail.<p>
 *
 * usage: <code>java sendfile <i>to from smtp file true|false</i></code>
 * where <i>to</i> and <i>from</i> are the destination and
 * origin email addresses, respectively, and <i>smtp</i>
 * is the hostname of the machine that has smtp server
 * running.  <i>file</i> is the file to send. The next parameter
 * either turns on or turns off debugging during sending.
 *
 * @author	Christopher Cotton
 */
public class SendFile {

    public static void main(String[] args) {
		
		String to = "yangnn_os@sari.ac.cn";
		String from = "OA_Feedback@sari.ac.cn";
		String host = "smtp.cstnet.cn";
		String filename = "D:\\workSpaceZKY\\emoe\\rosa-utils\\results\\tables\\simple_table.pdf";
		boolean debug = true;
		String msgText1 = "Sending a file.\n";
		String subject = "Sending a file";
		
		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		
		Session session = Session.getInstance(props);
		session.setDebug(debug);
		
		try {
		    // create a message
		    MimeMessage msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(from));
		    InternetAddress[] address = {new InternetAddress(to)};
		    msg.setRecipients(Message.RecipientType.TO, address);
		    msg.setSubject(subject);
	
		    // create and fill the first message part
		    MimeBodyPart mbp1 = new MimeBodyPart();
		    mbp1.setText(msgText1);
	
		    // create the second message part
		    MimeBodyPart mbp2 = new MimeBodyPart();
	
		    // attach the file to the message
		    mbp2.attachFile(filename);
	
		    /*
		     * Use the following approach instead of the above line if
		     * you want to control the MIME type of the attached file.
		     * Normally you should never need to do this.
		     *
		    FileDataSource fds = new FileDataSource(filename) {
			public String getContentType() {
			    return "application/octet-stream";
			}
		    };
		    mbp2.setDataHandler(new DataHandler(fds));
		    mbp2.setFileName(fds.getName());
		     */
	
		    // create the Multipart and add its parts to it
		    Multipart mp = new MimeMultipart();
		    mp.addBodyPart(mbp1);
		    mp.addBodyPart(mbp2);
	
		    // add the Multipart to the message
		    msg.setContent(mp);
	
		    // set the Date: header
		    msg.setSentDate(new Date());
	
		    /*
		     * If you want to control the Content-Transfer-Encoding
		     * of the attached file, do the following.  Normally you
		     * should never need to do this.
		     *
		    msg.saveChanges();
		    mbp2.setHeader("Content-Transfer-Encoding", "base64");
		     */
	
		    // send the message
		    Transport ts = session.getTransport();
		   //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
		    ts.connect("smtp.cstnet.cn", "OA_Feedback@sari.ac.cn", "sari112233");
		    
		    ts.sendMessage(msg, msg.getAllRecipients());
		    ts.close();
		    
		} catch (MessagingException mex) {
		    mex.printStackTrace();
		    Exception ex = null;
		    if ((ex = mex.getNextException()) != null) {
		    	ex.printStackTrace();
		    }
		} catch (IOException ioex) {
		    ioex.printStackTrace();
		}
    }
}
