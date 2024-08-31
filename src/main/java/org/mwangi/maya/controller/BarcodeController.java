package org.mwangi.maya.controller;

import org.mwangi.maya.services.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController

public class BarcodeController {
    @Autowired
    private ParcelService parcelService;
    @Value("${autohotkey.script.path}")
    private String scriptPath;
    @PostMapping("/script/start")
    public ResponseEntity<?> startScript() {
        try {
            String autoHotKeypath="C:\\Program Files\\AutoHotkey\\v2\\AutoHotkey.exe";
            Runtime.getRuntime().exec( autoHotKeypath+ " " + scriptPath);
            return ResponseEntity.ok("Script started successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to start script: " + e.getMessage());
        }
    }
    @PostMapping("/parcels/update-status")
    @ResponseBody
    public void updateParcelStatus(@RequestBody Map<String, String> payload) {
        String barcode = payload.get("barcode");
        //System.out.println(barcode);
        try {
            //System.out.println("Wamenicall");
            parcelService.updateParcelStatus(barcode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
