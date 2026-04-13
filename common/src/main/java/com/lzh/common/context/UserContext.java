package com.lzh.common.context;

/**
 * 修改后的 UserContext
 * 包含身份信息和该次请求需要的“热数据”
 */
public record UserContext(
        Long id,
        String stuId,
        String name,
        byte[] scheduleBitmap // 从 Redis 实时拉取的位图
) {
}