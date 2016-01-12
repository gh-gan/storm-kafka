package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hna.dao.MysqlDao;

public class FlumeTest {
	private static Logger log = LoggerFactory.getLogger(FlumeTest.class);
	
	//String s = "{"website":"JDBJPRD000","time_local":"03/Dec/2015:00:01:36 +0800","remote_addr":"60.11.11.163","remote_port":"51613","request":"/b2c-flight/searchflight.action","request_body":"\x03\x05\x1E\x07#\x0E\x07\x12J%8\x2293#%>'Q\x18\x05\x104\x1E\x03\x0EJ9<0Q\x18\x05\x104\x1E\x03\x0E(\x1AJQ\x13\x04\x034\x1E\x03\x0E(\x1AJQ\x13\x04\x034\x1E\x03\x0EJ?6<Q\x11\x1B\x1E\x10\x1F\x033\x16\x03\x12JEGFBZFEZGEQ\x05\x12\x03\x02\x05\x193\x16\x03\x12JEGFBZFEZGN","response_time":"2015-12-03 00:01:36", "request_time":"20151203000136","status":"200","body_bytes_sent":"54053","http_referer":"http://www.jdair.net/index.jsp;jsessionid=0558B234B7075FE5B670D0294AB4D8F5.d2", "http_user_agent":"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)","headers":"","jsession":"0558B234B7075FE5B670D0294AB4D8F5.d2","http_cookie":"IESESSION=alive; JSESSIONID=0558B234B7075FE5B670D0294AB4D8F5.d2; tirpTypeCookie=RETURN; orgCityCookie=NKG; dstCityCookie=HAK; flightDateCookie=2015-12-02; returnDateCookie=2015-12-09","resp_body":"" }";
	@Test
	public void send() throws Exception{
		BufferedReader	buff = new BufferedReader(new FileReader(new File("H:/access_nginx.log")));
		try {
			
			Socket socket  = new Socket("data01", 5140);
			BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String line = null;
			int i = 0;
			while((line=buff.readLine())!=null){
				bufw.write(line);
				bufw.newLine();
				i++;
				if(i>=100){
					break;
				}
			}
			bufw.flush();
			
			bufw.close();
//			socket.shutdownOutput();
			socket.close();
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String args[]){
		
		ScheduledExecutorService newSes = Executors.newScheduledThreadPool(1);
		newSes.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				log.info("111111111111111");
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
	
	@Test
	public void prop(){
		InputStream in = FlumeTest.class.getResourceAsStream("/conf.properties");
		Properties prop = new Properties();
		try{
			prop.load(in);
			log.info(prop.getProperty("jdbcUrl"));
		}catch(Exception e){
			
		}
	}
	
}
