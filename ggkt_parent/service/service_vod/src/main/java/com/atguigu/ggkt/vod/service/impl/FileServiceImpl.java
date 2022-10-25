package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.vod.service.FileService;
import com.atguigu.ggkt.vod.utils.ConstantPropertiesUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    //文件上传腾讯云COS
    @Override
    public String upload(MultipartFile file) {
        //固定代码,复制即可***********
        String secretId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String secretKey = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(ConstantPropertiesUtil.END_POINT);
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        //生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;


        //在文件名称前面添加uuid值(不带'-'),防止名称相同覆盖
        String key = UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename();
        //对上传文件分组，根据当前日期  /2022/11/11
        String date = new DateTime().toString("yyyy/MM/dd");
        key = date + "/" + key;
        try {
            //这部分也是参考腾讯云文档写的
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                    key,
                    inputStream,
                    objectMetadata
            );
            // 高级接口会返回一个异步结果Upload
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            //返回上传文件路径(没有直接返回,我们观察后手动拼接即可)
            String url = "https://" + bucketName + "." + "cos" + "." + ConstantPropertiesUtil.END_POINT + ".myqcloud.com" + "/" + key;
            return url;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
