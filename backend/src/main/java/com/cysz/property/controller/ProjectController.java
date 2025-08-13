package com.cysz.property.controller;

import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.dto.ProjectDTO;
import com.cysz.property.dto.ProjectQueryDTO;
import com.cysz.property.service.ProjectService;
import com.cysz.property.vo.ProjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目管理控制器
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Validated
@Tag(name = "项目管理", description = "项目管理相关接口")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 分页查询项目列表
     */
    @GetMapping
    @Operation(summary = "分页查询项目列表", description = "根据条件分页查询项目列表")
    public Result<PageResult<ProjectVO>> getProjectPage(ProjectQueryDTO query) {
        PageResult<ProjectVO> result = projectService.getProjectPage(query);
        return Result.success(result);
    }

    /**
     * 根据ID查询项目详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询项目详情", description = "根据ID查询项目详情")
    public Result<ProjectVO> getProjectById(
            @Parameter(description = "项目ID", required = true)
            @PathVariable @NotNull(message = "项目ID不能为空") Long id) {
        ProjectVO result = projectService.getProjectById(id);
        return Result.success(result);
    }

    /**
     * 查询所有项目列表（用于下拉选择）
     */
    @GetMapping("/list")
    @Operation(summary = "查询项目列表", description = "查询所有项目列表，用于下拉选择")
    public Result<List<ProjectVO>> getProjectList() {
        List<ProjectVO> result = projectService.getProjectList();
        return Result.success(result);
    }

    /**
     * 创建项目
     */
    @PostMapping
    @Operation(summary = "创建项目", description = "创建新的项目")
    public Result<Long> createProject(
            @Parameter(description = "项目信息", required = true)
            @RequestBody @Valid ProjectDTO projectDTO) {
        Long result = projectService.createProject(projectDTO);
        return Result.success(result);
    }

    /**
     * 更新项目
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新项目", description = "根据ID更新项目信息")
    public Result<Boolean> updateProject(
            @Parameter(description = "项目ID", required = true)
            @PathVariable @NotNull(message = "项目ID不能为空") Long id,
            @Parameter(description = "项目信息", required = true)
            @RequestBody @Valid ProjectDTO projectDTO) {
        Boolean result = projectService.updateProject(id, projectDTO);
        return Result.success(result);
    }

    /**
     * 删除项目
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目", description = "根据ID删除项目")
    public Result<Boolean> deleteProject(
            @Parameter(description = "项目ID", required = true)
            @PathVariable @NotNull(message = "项目ID不能为空") Long id) {
        Boolean result = projectService.deleteProject(id);
        return Result.success(result);
    }

    /**
     * 批量删除项目
     */
    @DeleteMapping
    @Operation(summary = "批量删除项目", description = "根据ID列表批量删除项目")
    public Result<Boolean> deleteProjects(
            @Parameter(description = "项目ID列表", required = true)
            @RequestBody @NotEmpty(message = "项目ID列表不能为空") List<Long> ids) {
        Boolean result = projectService.deleteProjects(ids);
        return Result.success(result);
    }

    /**
     * 检查项目编码是否存在
     */
    @GetMapping("/check-code")
    @Operation(summary = "检查项目编码", description = "检查项目编码是否已存在")
    public Result<Boolean> checkCodeExists(
            @Parameter(description = "项目编码", required = true)
            @RequestParam String code,
            @Parameter(description = "排除的项目ID")
            @RequestParam(required = false) Long excludeId) {
        Boolean result = projectService.checkCodeExists(code, excludeId);
        return Result.success(result);
    }

    /**
     * 检查项目名称是否存在
     */
    @GetMapping("/check-name")
    @Operation(summary = "检查项目名称", description = "检查项目名称是否已存在")
    public Result<Boolean> checkNameExists(
            @Parameter(description = "项目名称", required = true)
            @RequestParam String name,
            @Parameter(description = "排除的项目ID")
            @RequestParam(required = false) Long excludeId) {
        Boolean result = projectService.checkNameExists(name, excludeId);
        return Result.success(result);
    }
}