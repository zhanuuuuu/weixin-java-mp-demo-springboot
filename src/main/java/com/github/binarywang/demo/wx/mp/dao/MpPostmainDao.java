package com.github.binarywang.demo.wx.mp.dao;


import com.github.binarywang.demo.wx.mp.domain.VipEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MpPostmainDao {

    /**
     * 根据会员卡号查询客户信息
     * @param vipNo 会员卡号(唯一)
     * @return 客户信息
     */
    VipEntity queryByVipNo(@Param("vipNo") String vipNo);

    /**
     * 验证用户信息， 如果用户信息正确，那么返回一个用户对象， 否则为空
     * @param vipNo
     * @param password
     * @return
     */
    VipEntity validateUser(@Param("vipNo") String vipNo, @Param("password") String password);


    VipEntity queryByWxCardIDAndCode(@Param("cardID") String cardID, @Param("wxCode") String wxCode);

    /**
     * 根据用户的微信号获取用户信息
     * @param openID 微信号
     * @return 由于一个客户可能会有多个会员卡, 所以应该返回的是一个数组
     */
    List<VipEntity> queryByOpenID(@Param("openID") String openID);

    /**
     * 老会员绑定
     * @param vipNo 要绑定用户的会员卡号(在微信里称为code)
     * @param openID 微信号
     * @return 返回影响的行数, 如果行数为1, 那么说明绑定成功, 否则视为失败
     */
    int bindOpenID(@Param("vipNo") String vipNo, @Param("openID") String openID);

    int insertUserCodeWithOpenid(@Param("userCode") String userCode, @Param("cardID") String cardID, @Param("openID") String openID);

    int isBindOpenId(@Param("vipNo") String vipNo);

    int cardBindAction(@Param("vipNo") String vipNo, @Param("openID") String openID, @Param("cardID") String cardID, @Param("code") String code, @Param("phone") String phone);

}
