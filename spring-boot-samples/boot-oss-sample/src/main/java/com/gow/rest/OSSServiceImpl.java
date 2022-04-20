package com.gow.rest;

import static com.gow.oss.util.FileUploadUtils.checkBase64;
import com.gow.common.Result;
import com.gow.oss.util.FileUploadUtils;
import com.gow.oss.util.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wujt  2021/5/21
 */
@Service
public class OSSServiceImpl implements OSSService {
    private Integer avatarWidth = 150;

    private Integer avatarHeight = 150;

    @Autowired
    private FileUploadUtils fileUploadUtils;

    @Override
    public Result uploadUserAvatarBase64(String userKey, Map<String, String> source) throws IOException {
        checkBase64(source);
        String avatar =
                fileUploadUtils.uploadUserAvatarBase64(userKey, source.get("base64Str"), avatarWidth, avatarHeight);
        return Result.ok(avatar);
    }

    @Override
    public Result uploadLicense(String userKey, MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("营业照不能为空");
        }
        HashMap<String, String> result = new HashMap<>(2);
        try (InputStream stream = file.getInputStream()) {
            String fileType = FileUtils.getImageType(file.getContentType());
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = new StringBuilder()
                    .append(dateTime.getYear()).append(dateTime.getMonthValue()).append(dateTime.getDayOfMonth())
                    .append(dateTime.getHour()).append(dateTime.getMinute()).append(dateTime.getSecond()).toString();
            String filePath = new StringBuilder()
                    .append(File.separator)
                    .append(userKey)
                    .append(File.separator)
                    .append("license")
                    .append(File.separator)
                    .append(fileName)
                    .append(".")
                    .append(fileType).toString();
            filePath = FileUtils.filePathConvertUri(filePath);
            ByteArrayOutputStream outputStream =
                    fileUploadUtils.handleImage(avatarWidth, avatarHeight, fileType, filePath.substring(1), stream);
            result.put("url", filePath);

            result.put("source", Base64.getEncoder().encodeToString(outputStream.toByteArray()));

            return Result.ok(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片流获取失败");
        }
    }
}
