package com.agile.controller;

import com.agile.constant.Constants;
import com.agile.dto.AssetDto;
import com.agile.dto.ResponseDto;
import com.agile.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AssetController {
    @Autowired
    AssetService assetService;
    @PostMapping("/upload")
    public ResponseDto uploadAsset(@ModelAttribute AssetDto assetDto){
        ResponseDto responseDto= new ResponseDto();
        try{
            log.info("");
            responseDto= assetService.uploadAsset(assetDto);
        }catch (Exception e){
            log.info("Error in uploadAsset() : {}",e.getMessage());
             e.printStackTrace();
           responseDto.setStatusCode(Constants.FAILED_CODE);
           responseDto.setMessage(e.getMessage());
           responseDto.setStatus(Constants.FAILED);
        }
        log.info("EXITING uploadAsset ()");
        return responseDto;
    }
}
