package com.github.binarywang.demo.wx.mp.domain;

import lombok.*;

/**
 * Created by Administrator on 2019-05-23.
 */
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String nickname;
    private String headImgUrl;
    private String sexDesc;
    private String city;


}
