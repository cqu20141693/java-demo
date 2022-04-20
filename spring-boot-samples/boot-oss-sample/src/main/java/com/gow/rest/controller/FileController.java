package com.gow.rest.controller;

import com.gow.common.Result;
import com.gow.rest.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author wujt  2021/5/19
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private OSSService ossService;

    /**
     * 上传文件
     *
     * @param multipartRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result uploadFiles(MultipartRequest multipartRequest,
                              @RequestParam("fileName")
                              @NotBlank(message = "fileName 不能为空") String fileName) {
        MultipartFile file = multipartRequest.getFile("file");
        if (!file.getOriginalFilename().endsWith(".json")) {
            throw new RuntimeException("模型文件只支持 .json");
        }
        return Result.ok(fileName);
    }

    /**
     * 上传证件 file
     *
     * @param userKey
     * @param request
     * @return com.gow.common.Result
     * @date 2021/5/24 16:35
     */
    @PostMapping("uploads/images/license")
    public Result uploadLicense(@RequestParam("userKey") String userKey, MultipartHttpServletRequest request) {
        return ossService.uploadLicense(userKey, request.getFile("file"));
    }

    /**
     * 上传图片 base64
     *
     * @param source
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "uploads/images", method = RequestMethod.POST)
    public Result uploadImages(@RequestParam("userKey") String userKey, @RequestBody Map<String, String> source) throws Exception {

        return ossService.uploadUserAvatarBase64(userKey, source);

    }
}
