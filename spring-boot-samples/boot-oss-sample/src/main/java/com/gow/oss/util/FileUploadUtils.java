package com.gow.oss.util;

import com.gow.exception.CommonException;
import com.gow.oss.OSSFacade;
import com.gow.oss.model.OSSCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujt  2021/5/21
 */
@Component
public class FileUploadUtils {

    @Autowired
    private OSSFacade ossFacade;

    /**
     * 上传用户头像
     *
     * @param userKey
     * @param imgBase64Str
     * @param thumbnailWidth
     * @param thumbnailHeight
     * @return java.lang.String 保存头像路径
     * @date 2021/5/21 16:04
     */
    public String uploadUserAvatarBase64(String userKey, String imgBase64Str, Integer thumbnailWidth,
                                         Integer thumbnailHeight) throws IOException {
        String fileType = "." + imgBase64Str.substring(0, imgBase64Str.indexOf("base64,") + 7)
                .replaceAll("data:image/(.+);base64,", "$1").toLowerCase();
        String fileName = getFileName();
        String filePath = new StringBuilder()
                .append(File.separator)
                .append(userKey)
                .append(File.separator)
                .append("avatar")
                .append(File.separator)
                .append(fileName)
                .append(fileType).toString();

        filePath = FileUtils.filePathConvertUri(filePath);
        uploadBase64Image(imgBase64Str, thumbnailWidth, thumbnailHeight, fileType.substring(1), filePath.substring(1));

        return filePath;
    }

    /**
     * 上传base64 图片
     *
     * @param imgBase64Str
     * @param thumbnailWidth
     * @param thumbnailHeight
     * @param fileType
     * @param filePath
     * @throws IOException
     */
    private void uploadBase64Image(String imgBase64Str, Integer thumbnailWidth, Integer thumbnailHeight,
                                   String fileType, String filePath) throws IOException {
        String base64ImgData = imgBase64Str.substring(imgBase64Str.indexOf("base64,") + 7);
        Base64.Decoder decoder = Base64.getDecoder();
        //Base64解码
        byte[] b = decoder.decode(base64ImgData);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);

        handleImage(thumbnailWidth, thumbnailHeight, fileType, filePath, byteArrayInputStream);
    }

    /**
     * 处理图片流
     *
     * @param thumbnailWidth
     * @param thumbnailHeight
     * @param fileType
     * @param filePath
     * @param stream
     * @return java.io.ByteArrayOutputStream
     * @date 2021/5/21 16:09
     */
    public ByteArrayOutputStream handleImage(Integer thumbnailWidth, Integer thumbnailHeight, String fileType,
                                             String filePath, InputStream stream) {
        Thumbnails.Builder builder;
        try (InputStream inputStream = stream) {
            //图片压缩
            Thumbnails.Builder fileBuilder = Thumbnails.of(inputStream).scale(1);
            BufferedImage src = fileBuilder.asBufferedImage();
            Integer aHeight = thumbnailHeight;
            Integer aWidth = thumbnailWidth;
            if (src.getHeight() < thumbnailHeight) {
                aHeight = src.getHeight();
            }
            if (src.getWidth() < thumbnailWidth) {
                aWidth = src.getWidth();
            }
            builder = Thumbnails.of(src).size(aWidth, aHeight).keepAspectRatio(true).outputQuality(0.8)
                    .outputFormat(fileType);
        } catch (Exception e) {
            throw new RuntimeException("图片处理异常");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            builder.toOutputStream(byteArrayOutputStream);
            try (ByteArrayInputStream inStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
                upload(filePath, inStream);
                return byteArrayOutputStream;
            }
        } catch (IOException e) {
            throw new RuntimeException("图片处理异常");
        }
    }

    /**
     * 将图片持久化
     *
     * @param filePath
     * @param inStream
     * @date 2021/5/21 16:07
     */
    private void upload(String filePath, InputStream inStream) throws IOException {
        ossFacade.putObject(filePath, inStream);

    }

    public String getFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        return new StringBuilder()
                .append(dateTime.getYear()).append(dateTime.getMonthValue()).append(dateTime.getDayOfMonth())
                .append(dateTime.getHour()).append(dateTime.getMinute()).append(dateTime.getSecond()).toString();
    }

    public static void checkBase64(Map<String, String> source) {
        if (source == null) {
            throw new CommonException(OSSCode.PICTURE_NULL_ERROR.code(), OSSCode.PICTURE_NULL_ERROR.message());
        }
        String base64Str = source.get("base64Str");
        if (base64Str == null || base64Str.isEmpty()) {
            throw new CommonException(OSSCode.PICTURE_FORMAT_ERROR.code(), OSSCode.PICTURE_FORMAT_ERROR.message());
        }
    }
}
