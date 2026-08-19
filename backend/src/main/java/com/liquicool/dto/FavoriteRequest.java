package com.liquicool.dto;

import com.liquicool.enums.FavoriteTargetType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FavoriteRequest {

    @NotNull(message = "收藏类型不能为空")
    private FavoriteTargetType targetType;

    @NotNull(message = "收藏目标ID不能为空")
    private Long targetId;
}
