package com.github.binarywang.demo.wx.mp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019-06-04.
 */

@Data
@ConfigurationProperties(prefix = "image.download")
@Service
public class ImageUrl {
    private String url;

}
