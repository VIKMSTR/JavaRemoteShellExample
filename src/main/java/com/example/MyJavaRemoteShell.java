package com.example;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

//http://www.jcraft.com/jsch/examples/Exec.java.html
public class MyJavaRemoteShell {

    void connectRemotelyToDevice() throws JSchException, IOException {
        JSch jsch=new JSch();

        String host=null;
        String passwd = "123";
        String user=host.substring(0, host.indexOf('@'));
        host=host.substring(host.indexOf('@')+1);

        Session session=jsch.getSession(user, host, 22);
        session.setPassword(passwd);
      /*
      String xhost="127.0.0.1";
      int xport=0;
      String display=JOptionPane.showInputDialog("Enter display name",
                                                 xhost+":"+xport);
      xhost=display.substring(0, display.indexOf(':'));
      xport=Integer.parseInt(display.substring(display.indexOf(':')+1));
      session.setX11Host(xhost);
      session.setX11Port(xport+6000);
      */

        // username and password will be given via UserInfo interface.
//
//        session.setUserInfo(new UserInfo() {
//            @Override
//            public String getPassphrase() {
//                return null;
//            }
//
//            @Override
//            public String getPassword() {
//                return null;
//            }
//
//            @Override
//            public boolean promptPassword(String message) {
//                return false;
//            }
//
//            @Override
//            public boolean promptPassphrase(String message) {
//                return false;
//            }
//
//            @Override
//            public boolean promptYesNo(String message) {
//                return false;
//            }
//
//            @Override
//            public void showMessage(String message) {
//
//            }
//        });
        session.connect();

        String command= "ps -ef";

        Channel channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        // X Forwarding
        // channel.setXForwarding(true);

        //channel.setInputStream(System.in);
        channel.setInputStream(null);

        //channel.setOutputStream(System.out);

        //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
        //((ChannelExec)channel).setErrStream(fos);
        ((ChannelExec)channel).setErrStream(System.err);

        InputStream in=channel.getInputStream();

        channel.connect();

        byte[] tmp=new byte[1024];
        while(true){
            while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                System.out.print(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
                if(in.available()>0) continue;
                System.out.println("exit-status: "+channel.getExitStatus());
                break;
            }
            try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();
        session.disconnect();
    }

    }

