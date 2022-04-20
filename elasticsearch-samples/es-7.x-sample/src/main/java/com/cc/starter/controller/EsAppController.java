package com.cc.starter.controller;

import com.cc.core.index.AppIndexDTO;
import com.cc.core.search.SearchAppDTO;
import com.cc.core.service.EsAppService;
import com.cc.starter.controller.domain.vo.AddAppReq;
import com.gow.common.CommonCode;
import com.gow.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gow
 * @date 2022/2/28
 */
@RestController
@RequestMapping("api/es")
public class EsAppController {

    @Autowired
    private EsAppService esAppService;

    @PostMapping("add")
    public Result<Iterable<AppIndexDTO>> add(@RequestBody List<AddAppReq> addAppReqList) {
        if (addAppReqList != null && addAppReqList.size() > 0) {
            List<AppIndexDTO> indexDTOS = addAppReqList.stream().map(req -> {
                AppIndexDTO indexDTO = new AppIndexDTO();
                indexDTO.setAppKey(req.getAppKey());
                indexDTO.setName(req.getName());
                indexDTO.setStatus(req.getStatus());
                return indexDTO;
            }).collect(Collectors.toList());
            Iterable<AppIndexDTO> iterable = esAppService.batchInsert(indexDTOS);
            return Result.ok(iterable);
        }
        return Result.failed(CommonCode.UNKNOWN_ERROR, null);
    }

    @PostMapping("search")
    public Result<SearchHits<AppIndexDTO>> search(@RequestBody SearchAppDTO searchAppDTO) {
        return Result.ok(esAppService.searchAppByPage(searchAppDTO));
    }

    @GetMapping("searchByName")
    public Result<List<AppIndexDTO>> searchByName(@RequestParam("name")String name){
        return Result.ok(esAppService.searchByName(name));
    }

    @GetMapping("countByName")
    public Result<Long> countByName(@RequestParam("name")String name){
        return Result.ok(esAppService.countByName(name));
    }
}
