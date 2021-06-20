# 短链系统
> 简单的实现一个短链系统

<p align="center">
  <img src="./doc/logo.svg" alt="logo" width="70%" />
</p>

##  环境
- java 1.8
- springboot 2.5.0
- netty
- webflux
- redis
- mysql/oracle

## URL短链介绍

#### 各大互联网公司的短链(从短信中取得)

- 京东
    ```text
    3.cn/-1f2fQae
    ```
- 淘宝
    ```text
    https://s.tb.cn/C.10Oafv
    hm.tb.cn/x.Vi3bx5
    ```

- 哔哩哔哩
    ```text
    https://b23.tv/lVkf3F
    ```

- 百度
    ```text
    https://dwz.cn/NQhVSUM2
    ```
- 哈罗单车
    ```text
    https://h.c3x.me/1086mE
    
    ```

- 网易云
    ```text
    http://163.lu/cwGFX2
    ```
- 中国联通
    ```text
    https://u.10010.cn/qA8MI
    ```
- 中国电信
    ```text
    http://suo.im/5IGDLX
    ```
- 小米
    ```text
    https://s.mi.cn/_xUsz6
    ```
  
- 观察结论
   - 域名越短越好，京东3.cn无疑是最优秀的
   - 短链中带有.-_特殊字符
   - 短信中网址使用http协议时，可以省略http://的协议头部分，缩短字符
   
#### 优点
- 美观、满足字数限制（如短信营销）
- 字符少节约短信推广成本
- 易于传播分享，避免复制时丢失链接字符
- 二维码简化,降低二维码复杂程度，减少二维码像素，提升识别速度及成功率
- 便于分析,所有访问都通过短链系统重定向


## 设计中注意事项
- 避免和短链系统自身的url冲突
- 长链与短链映射关系不可逆
- 服务高可用，可动态水平扩展

## 知识点
- 域名越短越好
- 301 是永久重定向，302 是临时重定向


## 设计简述
- 设计灵感来源于知乎问题 [短 URL 系统是怎么设计的？](https://www.zhihu.com/question/29270034/answer/46446911)下@iammutex 的回答
- 1.使用发号策略，设计一个发号器，给每个长链发一个十进制编号
- 2.对发号器进行分组，可以把多个发号器分为一组，也可以一个发号器为一组，每个发号器组分配一个十进制id
- 3.发号器组id的62编码+"."+编号的62进制编码组成一个短链，如：1c.AbC1,看起来非常像阿里的短链是不是？:smile:



## 如何使用
- 体验地址：


## License
