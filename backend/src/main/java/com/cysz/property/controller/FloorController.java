package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Floor;
import com.cysz.property.service.FloorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 楼层管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "楼层管理", description = "楼层管理相关接口")
@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
@Validated
public class FloorController {

    private final FloorService floorService;

    @Operation(summary = "分页查询楼层列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getFloorPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "楼层编号") @RequestParam(required = false) String number,
            @Parameter(description = "楼层名称") @RequestParam(required = false) String name,
            @Parameter(description = "楼层状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "楼层类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId) {
        PageResult<Map<String, Object>> result = floorService.getFloorPage(
                pageQuery, number, name, status, type, buildingId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getFloorDetail(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = floorService.getFloorDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建楼层")
    @PostMapping
    public Result<Boolean> createFloor(
            @Parameter(description = "楼层信息") @RequestBody @Valid Floor floor) {
        boolean result = floorService.createFloor(floor);
        return Result.success(result);
    }

    @Operation(summary = "更新楼层")
    @PutMapping("/{id}")
    public Result<Boolean> updateFloor(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id,
            @Parameter(description = "楼层信息") @RequestBody @Valid Floor floor) {
        floor.setId(id);
        boolean result = floorService.updateFloor(floor);
        return Result.success(result);
    }

    @Operation(summary = "删除楼层")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteFloor(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id) {
        boolean result = floorService.deleteFloor(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除楼层")
    @DeleteMapping
    public Result<Boolean> batchDeleteFloors(
            @Parameter(description = "楼层ID列表") @RequestBody List<Long> floorIds) {
        boolean result = floorService.batchDeleteFloors(floorIds);
        return Result.success(result);
    }

    @Operation(summary = "根据楼栋ID查询楼层")
    @GetMapping("/by-building/{buildingId}")
    public Result<List<Map<String, Object>>> getFloorsByBuildingId(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long buildingId) {
        List<Map<String, Object>> result = floorService.getFloorsByBuildingId(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "验证楼层编号是否存在")
    @GetMapping("/check-number")
    public Result<Boolean> checkNumberExists(
            @Parameter(description = "楼层编号") @RequestParam String number,
            @Parameter(description = "楼栋ID") @RequestParam @NotNull Long buildingId,
            @Parameter(description = "排除的楼层ID") @RequestParam(required = false) Long excludeId) {
        boolean result = floorService.checkNumberExists(number, buildingId, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "验证楼层序号是否存在")
    @GetMapping("/check-sequence")
    public Result<Boolean> checkSequenceExists(
            @Parameter(description = "楼层序号") @RequestParam Integer sequence,
            @Parameter(description = "楼栋ID") @RequestParam @NotNull Long buildingId,
            @Parameter(description = "排除的楼层ID") @RequestParam(required = false) Long excludeId) {
        boolean result = floorService.checkSequenceExists(sequence, buildingId, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getFloorStatistics(
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId) {
        Map<String, Object> result = floorService.getFloorStatistics(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "根据状态统计楼层数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getFloorCountByStatus(
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId) {
        List<Map<String, Object>> result = floorService.getFloorCountByStatus(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "根据类型统计楼层数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> getFloorCountByType(
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId) {
        List<Map<String, Object>> result = floorService.getFloorCountByType(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层详细信息（包含单元统计）")
    @GetMapping("/{id}/detail-with-stats")
    public Result<Map<String, Object>> getFloorDetailWithStats(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = floorService.getFloorDetailWithStats(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层的单元列表")
    @GetMapping("/{id}/units")
    public Result<List<Map<String, Object>>> getFloorUnits(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = floorService.getFloorUnits(id);
        return Result.success(result);
    }

    @Operation(summary = "更新楼层单元数量和面积统计")
    @PostMapping("/{id}/update-stats")
    public Result<Boolean> updateFloorStats(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id) {
        boolean result = floorService.updateFloorStats(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层租赁统计")
    @GetMapping("/{id}/rental-stats")
    public Result<Map<String, Object>> getFloorRentalStats(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = floorService.getFloorRentalStats(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层收益统计")
    @GetMapping("/{id}/revenue-stats")
    public Result<Map<String, Object>> getFloorRevenueStats(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long id,
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        Map<String, Object> result = floorService.getFloorRevenueStats(id, year);
        return Result.success(result);
    }

    @Operation(summary = "批量创建楼层")
    @PostMapping("/batch-create")
    public Result<Map<String, Object>> batchCreateFloors(
            @Parameter(description = "楼栋ID") @RequestParam @NotNull Long buildingId,
            @Parameter(description = "开始楼层") @RequestParam Integer startFloor,
            @Parameter(description = "结束楼层") @RequestParam Integer endFloor,
            @Parameter(description = "楼层模板") @RequestBody @Valid Floor floorTemplate) {
        Map<String, Object> result = floorService.batchCreateFloors(buildingId, startFloor, endFloor, floorTemplate);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层最大序号")
    @GetMapping("/max-sequence")
    public Result<Integer> getMaxSequence(
            @Parameter(description = "楼栋ID") @RequestParam @NotNull Long buildingId) {
        Integer result = floorService.getMaxSequence(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼层最小序号")
    @GetMapping("/min-sequence")
    public Result<Integer> getMinSequence(
            @Parameter(description = "楼栋ID") @RequestParam @NotNull Long buildingId) {
        Integer result = floorService.getMinSequence(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "导出楼层数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportFloorData(
            @Parameter(description = "楼层编号") @RequestParam(required = false) String number,
            @Parameter(description = "楼层名称") @RequestParam(required = false) String name,
            @Parameter(description = "楼层状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "楼层类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId) {
        List<Map<String, Object>> result = floorService.exportFloorData(
                number, name, status, type, buildingId);
        return Result.success(result);
    }

}