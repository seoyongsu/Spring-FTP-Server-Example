package org.example.ftp.handler;

import org.apache.ftpserver.ftplet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Interface
 * {@link org.apache.ftpserver.ftplet.Ftplet}
 */
@Component
public class FtpLetHandler extends DefaultFtplet {
    @Override
    public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
        System.out.println("****** onConnect ******");
        return super.onConnect(session);
    }

    @Override
    public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException {
        return FtpletResult.DISCONNECT;
    }

    @Override
    public FtpletResult onLogin(FtpSession session, FtpRequest request) throws FtpException, IOException {
        System.out.println("******  On Login!!!! ******");

        return super.onLogin(session, request);
    }

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        System.out.println("-- onUploadStart -- ThreadID : "+ Thread.currentThread().getId() + " //  sessionID : " +session.getSessionId());
        return super.onUploadStart(session, request);
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        FileSystemView fileSystemView = session.getFileSystemView();
        FtpFile file = fileSystemView.getFile(request.getArgument());
        System.out.println(" file Info  : " + file);
        System.out.println("-- onUploadEnd -- ThreadID : "+ Thread.currentThread().getId() + " //  sessionID : " +session.getSessionId());
        return super.onUploadEnd(session, request);
    }
}
