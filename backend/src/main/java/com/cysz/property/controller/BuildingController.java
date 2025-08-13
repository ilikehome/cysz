package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Building;
import com.cysz.property.service.BuildingService;
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
 * 楼栋管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "楼栋管理", description = "楼栋管理相关接口")
@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
@Validated
public class BuildingController {

    private final BuildingService buildingService;

    @Operation(summary = "分页查询楼栋列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getBuildingPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "楼栋编号") @RequestParam(required = false) String number,
            @Parameter(description = "楼栋名称") @RequestParam(required = false) String name,
            @Parameter(description = "楼栋状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "楼栋类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        PageResult<Map<String, Object>> result = buildingService.getBuildingPage(
                pageQuery, number, name, status, type, projectId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getBuildingDetail(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = buildingService.getBuildingDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建楼栋")
    @PostMapping
    public Result<Boolean> createBuilding(
            @Parameter(description = "楼栋信息") @RequestBody @Valid Building building) {
        boolean result = buildingService.createBuilding(building);
        return Result.success(result);
    }

    @Operation(summary = "更新楼栋")
    @PutMapping("/{id}")
    public Result<Boolean> updateBuilding(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id,
            @Parameter(description = "楼栋信息") @RequestBody @Valid Building building) {
        building.setId(id);
        boolean result = buildingService.updateBuilding(building);
        return Result.success(result);
    }

    @Operation(summary = "删除楼栋")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteBuilding(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        boolean result = buildingService.deleteBuilding(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除楼栋")
    @DeleteMapping
    public Result<Boolean> batchDeleteBuildings(
            @Parameter(description = "楼栋ID列表") @RequestBody List<Long> buildingIds) {
        boolean result = buildingService.batchDeleteBuildings(buildingIds);
        return Result.success(result);
    }

    @Operation(summary = "根据项目ID查询楼栋")
    @GetMapping("/by-project/{projectId}")
    public Result<List<Map<String, Object>>> getBuildingsByProjectId(
            @Parameter(description = "项目ID") @PathVariable @NotNull Long projectId) {
        List<Map<String, Object>> result = buildingService.getBuildingsByProjectId(projectId);
        return Result.success(result);
    }

    @Operation(summary = "验证楼栋编号是否存在")
    @GetMapping("/check-number")
    public Result<Boolean> checkNumberExists(
            @Parameter(description = "楼栋编号") @RequestParam String number,
            @Parameter(description = "项目ID") @RequestParam @NotNull Long projectId,
            @Parameter(description = "排除的楼栋ID") @RequestParam(required = false) Long excludeId) {
        boolean result = buildingService.checkNumberExists(number, projectId, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getBuildingStatistics(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        Map<String, Object> result = buildingService.getBuildingStatistics(projectId);
        return Result.success(result);
    }

    @Operation(summary = "根据状态统计楼栋数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getBuildingCountByStatus(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        List<Map<String, Object>> result = buildingService.getBuildingCountByStatus(projectId);
        return Result.success(result);
    }

    @Operation(summary = "根据类型统计楼栋数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> getBuildingCountByType(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        List<Map<String, Object>> result = buildingService.getBuildingCountByType(projectId);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋详细信息（包含楼层和单元统计）")
    @GetMapping("/{id}/detail-with-stats")
    public Result<Map<String, Object>> getBuildingDetailWithStats(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = buildingService.getBuildingDetailWithStats(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋的楼层列表")
    @GetMapping("/{id}/floors")
    public Result<List<Map<String, Object>>> getBuildingFloors(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = buildingService.getBuildingFloors(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋的单元列表")
    @GetMapping("/{id}/units")
    public Result<List<Map<String, Object>>> getBuildingUnits(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = buildingService.getBuildingUnits(id);
        return Result.success(result);
    }

    @Operation(summary = "更新楼栋单元数量和面积统计")
    @PostMapping("/{id}/update-stats")
    public Result<Boolean> updateBuildingStats(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        boolean result = buildingService.updateBuildingStats(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋租赁统计")
    @GetMapping("/{id}/rental-stats")
    public Result<Map<String, Object>> getBuildingRentalStats(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = buildingService.getBuildingRentalStats(id);
        return Result.success(result);
    }

    @Operation(summary = "获取楼栋收益统计")
    @GetMapping("/{id}/revenue-stats")
    public Result<Map<String, Object>> getBuildingRevenueStats(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long id,
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        Map<String, Object> result = buildingService.getBuildingRevenueStats(id, year);
        return Result.success(result);
    }

    @Operation(summary = "导出楼栋数据")
    @GetMapping("/export")
    public Result<String> exportBuildingData(
            @Parameter(description = "楼栋编号") @RequestParam(required = false) String number,
            @Parameter(description = "楼栋名称") @RequestParam(required = false) String name,
            @Parameter(description = "楼栋状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "楼栋类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        String result = buildingService.exportBuildingData(
                number, name, status, type, projectId);
        return Result.success(result);
    }

}