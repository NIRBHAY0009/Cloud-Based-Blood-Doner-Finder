package com.example.blooddonor.controller;

import com.example.blooddonor.model.Donor;
import com.example.blooddonor.service.DonorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*") // Allow access from any frontend
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    // 🩸 Register a new donor
    @PostMapping
    public ResponseEntity<?> createDonor(@RequestBody Donor donor) {
        try {
            Donor saved = donorService.addDonor(donor).get();
            System.out.println("✅ Donor created successfully: " + saved.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error registering donor: " + e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Unexpected error: " + ex.getMessage());
        }
    }

    // 🔍 Search donors by filters (bloodGroup, location, radius)
    @GetMapping
    public ResponseEntity<?> searchDonors(
            @RequestParam(name = "bloodGroup", required = false) String bloodGroup,
            @RequestParam(name = "lat", required = false) Double lat,
            @RequestParam(name = "lng", required = false) Double lng,
            @RequestParam(name = "radiusKm", required = false) Double radiusKm
    ) {
        try {
            List<Donor> donors = donorService.findDonors(bloodGroup, lat, lng, radiusKm).get();

            if (donors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("⚠️ No donors found for the given criteria.");
            }

            return ResponseEntity.ok(donors);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error fetching donors: " + e.getMessage());
        }
    }

    // 📋 Get all donors (unfiltered)
    @GetMapping("/all")
    public ResponseEntity<?> getAllDonors() {
        try {
            List<Donor> donors = donorService.getAllDonors().get();

            if (donors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("⚠️ No donors found in the database.");
            }

            return ResponseEntity.ok(donors);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error retrieving donors: " + e.getMessage());
        }
    }
}
