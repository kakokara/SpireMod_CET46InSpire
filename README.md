# 别忘了四六级 CET46 In Spire
这个模组想法来自Noita的同名模组. [Noita创意工坊-别忘了四六级](https://steamcommunity.com/sharedfiles/filedetails/?id=3404422065&searchtext=%E5%88%AB%E5%BF%98%E4%BA%86).

**添加系列遗物**: 每当你打出一张卡牌, 会触发一个指定词库范围内的小单词测验, 你的测验得分会影响你**所有**攻击和格挡的获得倍率. 测验得分在每次测验后更新, 或是在回合结束或战斗结束时复位.  
**添加新事件**: 在第0层的涅奥房间结束时强制进入事件, 此事件会提醒你距离下一次CET还有多久, 同时允许你选择遗物的词库范围(也可以选择不拿遗物, 那么就和正常游戏没有区别).  

## 词库来源
原始词库来自新东方的pdf版本: [新东方-2024最新英语四六级词汇完整版](https://cet4-6.xdf.cn/202409/13925649.html).  
原始词库通过Python解析得到`UIString`格式的json文件, 由于解析脚本处理部分情况并不理想, 因此词库其实存在很多问题.  
PS: Python解析用的是`pdfplumber`和`re`, 很明显后者并不靠谱.

## 备注
CET计划时间是通过硬编码写进去的, 因此可能每半年需要更新一次考试时间.  
另外, 目前只有四级词库.  
本地化只做了中文, 不过预计会添加英文版本模组.  

## Issues & PRs
**我们需要词库!** 老实说我自认为这个mod扩展性挺好的, 要什么CET6, CJT都可以很快实现, 但是词库这个没办法.  
欢迎任何建议. 