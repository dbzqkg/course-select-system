//package com.lzh.common.constants;
//
//public class RedisConstants {
//    // 课程相关
//    public static final String COURSE_STOCK_KEY = "course:stock:";
//    public static final String COURSE_IDX_ALL = "course:idx:all";
//
//    // 学生相关
//    public static final String STUDENT_SCHEDULE_BIT = "student:schedule:";
//    public static final String STUDENT_TOKEN_LOCK = "lock:refresh:";
//
//}
package com.lzh.common.constants;

/**
 * Redis 常量类
 * 规范：前缀:业务:标识
 */
public class RedisConstants {

    // ======================== 1. 学生/用户相关 ========================

    /** 学生个人基本信息 (Hash: id, name, major...) */
    public static final String STUDENT_INFO_KEY = "student:info:";
    /** 学生选课时间位图 (String/Bit: 1024bits/128bytes) */
    public static final String STUDENT_SCHEDULE_BIT = "student:schedule:";
    /** 学生已选课程集合 (Set: 存储 classId，用于幂等校验) */
    public static final String STUDENT_CLASSES_SET = "student:classes:";

    // ======================== 2. 课程/教学班相关 ========================

    /** 教学班(SKU)实时库存 (String: 用于 Redis 预减库存) */
    public static final String CLASS_STOCK_KEY = "class:stock:";
    /** 教学班(SKU)详细信息 (Hash: 冗余存储学分、老师、地点，用于前端快速渲染) */
    public static final String CLASS_INFO_KEY = "class:info:";
    /** 课程(SPU)基础属性缓存 (Hash: 课程大纲、类型等) */
    public static final String COURSE_INFO_KEY = "course:info:";
    /** 所有开放选课的教学班索引 (ZSet/Set: 用于分页或展示全部列表) */
    public static final String CLASS_IDX_ALL = "class:idx:all";
    /** 时间位图*/
public static final String CLASS_SCHEDULE_BIT_MAP = "class:schedule:";


    // ======================== 3. 分布式锁/同步相关 ========================

    /** Token 续期锁 (防并发刷新) */
    public static final String STUDENT_TOKEN_LOCK = "lock:refresh:";
    /** 选课逻辑分布式锁 (兜底使用) */
    public static final String COURSE_SELECT_LOCK = "lock:select:";
    /** 缓存预热状态锁 (防止重复加载) */
    public static final String CACHE_WARM_LOCK = "lock:warm:";

    // ======================== 4. 过期时间 (TTL - 秒) ========================

    /** 默认 30 分钟过期 (如：验证码、临时状态) */
    public static final String TTL_DEFAULT = "1800";
    /** 1 小时过期 (如：频繁访问的信息) */
    public static final Long TTL_ONE_HOUR = 3600L;
    /** 12 小时过期 (建议位图、用户信息设置较长 TTL) */
    public static final Long TTL_HALF_DAY = 43200L;
    /** 7 天过期 (对于不常变动的数据) */
    public static final Long TTL_ONE_WEEK = 604800L;

}