package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            String httpRequest= getRequest(input);
            System.out.println("Request recieved:"+httpRequest);
            
            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.1 200 OK").append("\n");
            if(httpRequest.indexOf("keep-alive")!=-1){
            	response.append("connection: keep-alive").append("\n");	
            	System.out.println("esss-1122");
            }            
            response.append("content-type: text/html");
            
            response.append("\n\n");
            response.append("<font size=\"5\" face=\"verdana\" color=\"green\">");
            response.append("<b> [Java Http Server Response]</b>:");
            response.append("</font>");
            response.append(this.serverText);
            
            response.append("<font size=\"3\" color=\"blue\">");
            response.append("<br/><br/><b>[Request Recieved]</b>:").append(httpRequest);
            response.append("\nTimestamp:").append(time);
            response.append("</font>");
            output.write(response.toString().getBytes());
            output.close();
            input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getRequest(InputStream inputStream){
    	byte[] buffer = new byte[1024];
    	int read;
    	try {
			while((read= inputStream.read(buffer))!=-1){
				String request = new String(buffer,0,read);
				return request;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
}