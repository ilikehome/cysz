package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Unit;
import com.cysz.property.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 单元管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "单元管理", description = "单元管理相关接口")
@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
@Validated
public class UnitController {

    private final UnitService unitService;

    @Operation(summary = "分页查询单元列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getUnitPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "单元编号") @RequestParam(required = false) String number,
            @Parameter(description = "单元名称") @RequestParam(required = false) String name,
            @Parameter(description = "单元状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "单元类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "朝向") @RequestParam(required = false) Integer orientation,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        PageResult<Map<String, Object>> result = unitService.getUnitPage(
                pageQuery, number, name, status, type, orientation, floorId, buildingId, projectId);
        return Result.success(result);
    }

    @Operation(summary = "获取单元详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUnitDetail(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = unitService.getUnitDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建单元")
    @PostMapping
    public Result<Boolean> createUnit(
            @Parameter(description = "单元信息") @RequestBody @Valid Unit unit) {
        boolean result = unitService.createUnit(unit);
        return Result.success(result);
    }

    @Operation(summary = "更新单元")
    @PutMapping("/{id}")
    public Result<Boolean> updateUnit(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id,
            @Parameter(description = "单元信息") @RequestBody @Valid Unit unit) {
        unit.setId(id);
        boolean result = unitService.updateUnit(unit);
        return Result.success(result);
    }

    @Operation(summary = "删除单元")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUnit(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id) {
        boolean result = unitService.deleteUnit(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除单元")
    @DeleteMapping
    public Result<Boolean> batchDeleteUnits(
            @Parameter(description = "单元ID列表") @RequestBody List<Long> unitIds) {
        boolean result = unitService.batchDeleteUnits(unitIds);
        return Result.success(result);
    }

    @Operation(summary = "根据楼层ID查询单元")
    @GetMapping("/by-floor/{floorId}")
    public Result<List<Map<String, Object>>> getUnitsByFloorId(
            @Parameter(description = "楼层ID") @PathVariable @NotNull Long floorId) {
        List<Map<String, Object>> result = unitService.getUnitsByFloorId(floorId);
        return Result.success(result);
    }

    @Operation(summary = "根据楼栋ID查询单元")
    @GetMapping("/by-building/{buildingId}")
    public Result<List<Map<String, Object>>> getUnitsByBuildingId(
            @Parameter(description = "楼栋ID") @PathVariable @NotNull Long buildingId) {
        List<Map<String, Object>> result = unitService.getUnitsByBuildingId(buildingId);
        return Result.success(result);
    }

    @Operation(summary = "根据项目ID查询单元")
    @GetMapping("/by-project/{projectId}")
    public Result<List<Map<String, Object>>> getUnitsByProjectId(
            @Parameter(description = "项目ID") @PathVariable @NotNull Long projectId) {
        List<Map<String, Object>> result = unitService.getUnitsByProjectId(projectId);
        return Result.success(result);
    }

    @Operation(summary = "验证单元编号是否存在")
    @GetMapping("/check-number")
    public Result<Boolean> checkNumberExists(
            @Parameter(description = "单元编号") @RequestParam String number,
            @Parameter(description = "楼层ID") @RequestParam @NotNull Long floorId,
            @Parameter(description = "排除的单元ID") @RequestParam(required = false) Long excludeId) {
        boolean result = unitService.checkNumberExists(number, floorId, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取单元统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getUnitStatistics(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId) {
        Map<String, Object> result = unitService.getUnitStatistics(projectId, buildingId, floorId);
        return Result.success(result);
    }

    @Operation(summary = "根据状态统计单元数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getUnitCountByStatus(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId) {
        List<Map<String, Object>> result = unitService.getUnitCountByStatus(projectId, buildingId, floorId);
        return Result.success(result);
    }

    @Operation(summary = "根据类型统计单元数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> getUnitCountByType(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId) {
        List<Map<String, Object>> result = unitService.getUnitCountByType(projectId, buildingId, floorId);
        return Result.success(result);
    }

    @Operation(summary = "根据朝向统计单元数量")
    @GetMapping("/statistics/orientation")
    public Result<List<Map<String, Object>>> getUnitCountByOrientation(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId) {
        List<Map<String, Object>> result = unitService.getUnitCountByOrientation(projectId, buildingId, floorId);
        return Result.success(result);
    }

    @Operation(summary = "获取单元详细信息（包含合同信息）")
    @GetMapping("/{id}/detail-with-contract")
    public Result<Map<String, Object>> getUnitDetailWithContract(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = unitService.getUnitDetailWithContract(id);
        return Result.success(result);
    }

    @Operation(summary = "获取单元合同列表")
    @GetMapping("/{id}/contracts")
    public Result<List<Map<String, Object>>> getUnitContracts(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = unitService.getUnitContracts(id);
        return Result.success(result);
    }

    @Operation(summary = "获取单元当前合同")
    @GetMapping("/{id}/current-contract")
    public Result<Map<String, Object>> getUnitCurrentContract(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = unitService.getUnitCurrentContract(id);
        return Result.success(result);
    }

    @Operation(summary = "获取可租赁单元列表")
    @GetMapping("/available")
    public Result<List<Map<String, Object>>> getAvailableUnits(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId,
            @Parameter(description = "单元类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "最小面积") @RequestParam(required = false) BigDecimal minArea,
            @Parameter(description = "最大面积") @RequestParam(required = false) BigDecimal maxArea,
            @Parameter(description = "最小租金") @RequestParam(required = false) BigDecimal minRent,
            @Parameter(description = "最大租金") @RequestParam(required = false) BigDecimal maxRent) {
        List<Map<String, Object>> result = unitService.getAvailableUnits(
                projectId, buildingId, floorId, type, minArea, maxArea, minRent, maxRent);
        return Result.success(result);
    }

    @Operation(summary = "获取单元租赁历史")
    @GetMapping("/{id}/rental-history")
    public Result<List<Map<String, Object>>> getUnitRentalHistory(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = unitService.getUnitRentalHistory(id);
        return Result.success(result);
    }

    @Operation(summary = "获取单元收益统计")
    @GetMapping("/{id}/revenue-stats")
    public Result<Map<String, Object>> getUnitRevenueStats(
            @Parameter(description = "单元ID") @PathVariable @NotNull Long id,
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        Map<String, Object> result = unitService.getUnitRevenueStats(id, year);
        return Result.success(result);
    }

    @Operation(summary = "批量创建单元")
    @PostMapping("/batch-create")
    public Result<Map<String, Object>> batchCreateUnits(
            @Parameter(description = "楼层ID") @RequestParam @NotNull Long floorId,
            @Parameter(description = "单元数量") @RequestParam Integer count,
            @Parameter(description = "单元模板") @RequestBody @Valid Unit unitTemplate) {
        Map<String, Object> result = unitService.batchCreateUnits(floorId, count, unitTemplate);
        return Result.success(result);
    }

    @Operation(summary = "批量更新单元状态")
    @PostMapping("/batch-update-status")
    public Result<Boolean> batchUpdateUnitStatus(
            @Parameter(description = "单元ID列表") @RequestBody List<Long> unitIds,
            @Parameter(description = "状态") @RequestParam Integer status) {
        boolean result = unitService.batchUpdateUnitStatus(unitIds, status);
        return Result.success(result);
    }

    @Operation(summary = "批量更新单元租金")
    @PostMapping("/batch-update-rent")
    public Result<Boolean> batchUpdateUnitRent(
            @Parameter(description = "单元ID列表") @RequestBody List<Long> unitIds,
            @Parameter(description = "租金") @RequestParam BigDecimal rent) {
        boolean result = unitService.batchUpdateUnitRent(unitIds, rent);
        return Result.success(result);
    }

    @Operation(summary = "获取单元出租率统计")
    @GetMapping("/occupancy-rate")
    public Result<BigDecimal> getUnitOccupancyRate(
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId) {
        BigDecimal result = unitService.getUnitOccupancyRate(projectId, buildingId, floorId);
        return Result.success(result);
    }

    @Operation(summary = "导出单元数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportUnitData(
            @Parameter(description = "单元编号") @RequestParam(required = false) String number,
            @Parameter(description = "单元名称") @RequestParam(required = false) String name,
            @Parameter(description = "单元状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "单元类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "朝向") @RequestParam(required = false) Integer orientation,
            @Parameter(description = "楼层ID") @RequestParam(required = false) Long floorId,
            @Parameter(description = "楼栋ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        List<Map<String, Object>> result = unitService.exportUnitData(
                number, name, status, type, orientation, floorId, buildingId, projectId);
        return Result.success(result);
    }

}