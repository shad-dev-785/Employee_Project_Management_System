package com.agile.service;

import com.agile.constant.Constants;
import com.agile.dto.AssetDto;
import com.agile.dto.ResponseDto;
import com.agile.model.Asset;
import com.agile.repository.AssetRepository;
import com.agile.storage.StorageService;
import com.agile.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    StorageService storageService;

    public ResponseDto uploadAsset(AssetDto assetDto) {
        ResponseDto responseDto= new ResponseDto();
        Utility utility= new Utility();
    try{
        String filename =generateDateTime() + Utility.getFileExtension(assetDto.getDocument().getOriginalFilename());
        storageService.store(assetDto.getDocument(), filename);
        Asset asset= new Asset();
        asset.setDescription(assetDto.getDescription());
        asset.setName(assetDto.getName());
        asset.setLink(assetDto.getLink());
        asset.setDocument(filename);
        asset.setUploadTime(LocalDateTime.now());
        assetRepository.save(asset);
        responseDto.setMessage("Asset uploaded Successfully");
    }catch (Exception e){
        log.info("Error in uploading Assets in uploadAsset :{}", e.getMessage());
        e.printStackTrace();
        responseDto.setStatus(Constants.FAILED);
        responseDto.setStatusCode(Constants.FAILED_CODE);
        responseDto.setMessage(e.getMessage());
    }
     log.info("EXITING uploadAsset()");
    return responseDto;
    }

    String generateDateTime() {
//        this is for generating date-time
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (int) (Math.random() * 1000);
    }
}
