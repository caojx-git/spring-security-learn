package com.caojx.learn.security.core.validate.code.image;

import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 图形验证码生成实现，注意这里没有@Service注解，为了在ValidateCodeBeanConfig中配合@ConditionalOnMissingBean注解
 *
 * @author caojx
 * @version $Id: ImageValidateCodeGenerator.java,v 1.0 2020/2/22 6:37 下午 caojx
 * @date 2020/2/22 6:37 下午
 */
public class ImageValidateCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ImageCode generate(ServletWebRequest servletWebRequest) {
        //定义图片的长和宽
//        int width = 67;
//        int height = 23;

        // 从请求中读取图片的长和宽，没有取配置中的默认值
        int width = ServletRequestUtils.getIntParameter(servletWebRequest.getRequest(), "width", securityProperties.getCode().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(servletWebRequest.getRequest(), "height", securityProperties.getCode().getImage().getHeight());


        //生成一张图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //获得画笔工具
        Graphics g = image.getGraphics();

        //画一个矩形
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);

        //画干扰线
        g.setColor(new Color(0, 0, 0));
        //设置字体
        g.setFont(new Font("Time New Roman", Font.ITALIC, 20));
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            //(x,y)到(x+x1,y+y1)
            g.drawLine(x, y, x + x1, y + y1);
        }

        //画数据
        String sRand = "";
        // 从配置中读取图片验证码的长度
        int length = securityProperties.getCode().getImage().getLength();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            //每一个字都改变一下颜色
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            //画每一个数据
            g.drawString(rand, 13 * i, 16);
        }

        g.dispose();

        //生成我们自己的验证码数据(图片，验证码，过期时间)
        int expireIn = securityProperties.getCode().getImage().getExpireIn();
        return new ImageCode(image, sRand, expireIn);
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}