package com.sunxuhao.server;

import com.sunxuhao.crypto.Crypto;
import com.sunxuhao.crypto.CryptoBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            ByteBuf buf=(ByteBuf)msg;
            byte[] bytes=new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            System.out.println("接受明文："+new String(bytes));
            String res= new CryptoBuilder()
                    .append(Crypto.MD5)
                    .getInstance().encrypt(bytes);
            System.out.println("加密结果："+res);
            ByteBuf resBuf= Unpooled.copiedBuffer(res.getBytes());
            ctx.write(resBuf);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
