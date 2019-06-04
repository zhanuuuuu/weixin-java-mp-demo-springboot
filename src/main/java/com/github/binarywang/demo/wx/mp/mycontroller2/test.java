package com.github.binarywang.demo.wx.mp.mycontroller2;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Administrator on 2019-05-29.
 */
public class test {

    public static void main(String[] args) {
        String Signature= DigestUtils.sha1Hex("1559118074IpK_1T69hDhZkLQTlwsAX0duH9HnYUQl89-_Tl9KWtWrfW2gfeiTp8RdfZCgO2pHm2Z9XjMjW3rvKaPMm-YI4ARC89r1mzChpU2r4ZpgOfAwrdq-3NSJ7VWaC2L0lEgWaE");


        Signature= DigestUtils.sha1Hex("1559120607k875uYuhhwlKr1aYIpK_1T69hDhZkLQTlwsAX0duH9HnYUQl89-_Tl9KWtWrfW2gfeiTp8RdfZCgO2pHm2Z9XjMjW3rvKaPMm-YI4ApgOfAwrdq-3NSJ7VWaC2L0lEgWaE");
        System.out.println(Signature);
    }
}
