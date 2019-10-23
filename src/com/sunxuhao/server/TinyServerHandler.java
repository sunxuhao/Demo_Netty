package com.sunxuhao.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TinyServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private int length = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        try {
            HttpRequest request = (HttpRequest) msg;
            byte[] content = getContent(request.uri());
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));
            HttpHeaders headers = response.headers();
            this.length=response.content().readableBytes();
            headers.set("Server", "Tiny Web Server");
            headers.set("Content-Type", getContentType(request.uri()));
            headers.setInt("Content-Length", length);
            if (HttpUtil.isKeepAlive(request)) {
                headers.set("Connection", "keep-live");
                ctx.write(response);
            } else {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception e) {
        }
    }

    void show(){
        Runtime rt=Runtime.getRuntime();
        System.out.println("free memory: "+rt.freeMemory()/1024);
        System.out.println("total memory: "+rt.totalMemory()/1024);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        if(length>1024*1024*256){
            System.out.println("before GC");
            show();
            System.gc();
            System.out.println("after GC");
            show();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public byte[] getContent(String uri) {
        File file = new File(new File("").getAbsolutePath() + uri);
        if (!file.exists()) {
            return "".getBytes();
        }
        FileInputStream fis = null;
        byte[] content = null;
        try {
            fis = new FileInputStream(file);
            content = new byte[fis.available()];
            fis.read(content);
        } catch (IOException e) {
            e.printStackTrace();
            content = new byte[0];
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return content;
        }


    }

    public String getContentType(String uri) {
        int begin = uri.lastIndexOf(".");
        String type = "text/plain";
        if (begin < 0) {
            return type;
        }
        String suffix = uri.substring(begin + 1);
        switch (suffix) {
            case "html":
                type = "text/html";
                break;
            case "css":
                type = "text/css";
                break;
            case "js":
                type = "application/x-javascript";
                break;
            case "jpg":
                type = "image/jpeg";
                break;
            case "jpeg":
                type = "image/jpeg";
                break;
            case "png":
                type = "image/png";
                break;
            case "gif":
                type = "image/gif";
                break;
            case "ico":
                type = "image/vnd.microsoft.icon";
                break;
            case "mp4":
                type = "audio/mp4";
                break;
        }
        return type;
    }
}
