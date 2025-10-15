package com.example.blooddonor.service;

import com.example.blooddonor.model.Donor;
import com.google.firebase.database.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DonorService {

    private DatabaseReference donorsRef;

    @PostConstruct
    public void init() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            this.donorsRef = database.getReference("donors");
            System.out.println("✅ DonorService initialized successfully with Firebase Realtime Database.");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize DonorService: " + e.getMessage());
        }
    }

    // ----------------- ADD DONOR -----------------
    public CompletableFuture<Donor> addDonor(Donor donor) {
        CompletableFuture<Donor> future = new CompletableFuture<>();

        if (donorsRef == null) {
            future.completeExceptionally(new IllegalStateException("Firebase not initialized"));
            return future;
        }

        DatabaseReference newRef = donorsRef.push();
        donor.setId(newRef.getKey());

        newRef.setValue(donor, (error, ref) -> {
            if (error != null) {
                System.err.println("❌ Error saving donor: " + error.getMessage());
                future.completeExceptionally(error.toException());
            } else {
                System.out.println("✅ Donor registered successfully: " + donor.getName());
                future.complete(donor);
            }
        });

        return future;
    }

    // ----------------- GET ALL DONORS -----------------
    public CompletableFuture<List<Donor>> getAllDonors() {
        CompletableFuture<List<Donor>> future = new CompletableFuture<>();

        if (donorsRef == null) {
            future.completeExceptionally(new IllegalStateException("Firebase not initialized"));
            return future;
        }

        donorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Donor> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Donor donor = child.getValue(Donor.class);
                    if (donor != null) list.add(donor);
                }
                System.out.println("✅ Retrieved " + list.size() + " donors from Firebase.");
                future.complete(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("❌ Failed to fetch donors: " + error.getMessage());
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    // ----------------- FIND DONORS BY FILTER -----------------
    public CompletableFuture<List<Donor>> findDonors(String bloodGroup, Double lat, Double lng, Double radiusKm) {
        return getAllDonors().thenApply(list -> {
            List<Donor> filtered = new ArrayList<>();

            for (Donor d : list) {
                // Filter by blood group
                if (bloodGroup != null && !bloodGroup.isEmpty() &&
                        !bloodGroup.equalsIgnoreCase(d.getBloodGroup())) {
                    continue;
                }

                // Filter by location
                if (lat != null && lng != null && radiusKm != null) {
                    double dist = haversine(lat, lng, d.getLatitude(), d.getLongitude());
                    if (dist > radiusKm) continue;
                }

                filtered.add(d);
            }

            System.out.println("✅ Found " + filtered.size() + " matching donors.");
            return filtered;
        });
    }

    // ----------------- HAVERSINE DISTANCE -----------------
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
