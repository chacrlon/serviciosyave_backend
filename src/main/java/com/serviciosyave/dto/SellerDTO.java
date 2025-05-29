package com.serviciosyave.dto;

import com.serviciosyave.Enum.Estatus;

import java.time.LocalDateTime;
import java.util.List;

public class SellerDTO {
    private Long id;
    private String fullName;
    private byte[] dniFrontName;
    private byte[] dniBackName;
    private byte[] selfieName;
    private byte[] profilePicture;
    private byte[] universityTitleName;
    private List<byte[]> certificationsNames;
    private List<byte[]> galleryImagesNames;
    private String profession;
    private Integer yearsOfExperience;
    private String skillsDescription;
    private Long userId;
    private List<Long> selectedSubcategories;
    private String serviceArea;
    private Double latitude;
    private Double longitude;
    private Double coverageRadius;
    private LocalDateTime createdAt;
    private Estatus status;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public byte[] getDniFrontName() { return dniFrontName; }
    public void setDniFrontName(byte[] dniFrontName) { this.dniFrontName = dniFrontName; }

    public byte[] getDniBackName() { return dniBackName; }
    public void setDniBackName(byte[] dniBackName) { this.dniBackName = dniBackName; }

    public byte[] getSelfieName() { return selfieName; }
    public void setSelfieName(byte[] selfieName) { this.selfieName = selfieName; }

    public byte[] getProfilePicture() { return profilePicture; }
    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }

    public byte[] getUniversityTitleName() { return universityTitleName; }
    public void setUniversityTitleName(byte[] universityTitleName) { this.universityTitleName = universityTitleName; }

    public List<byte[]> getCertificationsNames() { return certificationsNames; }
    public void setCertificationsNames(List<byte[]> certificationsNames) { this.certificationsNames = certificationsNames; }

    public List<byte[]> getGalleryImagesNames() { return galleryImagesNames; }
    public void setGalleryImagesNames(List<byte[]> galleryImagesNames) { this.galleryImagesNames = galleryImagesNames; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public String getSkillsDescription() { return skillsDescription; }
    public void setSkillsDescription(String skillsDescription) { this.skillsDescription = skillsDescription; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getSelectedSubcategories() {
        return selectedSubcategories;
    }

    public void setSelectedSubcategories(List<Long> selectedSubcategories) {
        this.selectedSubcategories = selectedSubcategories;
    }

    public String getServiceArea() { return serviceArea; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getCoverageRadius() { return coverageRadius; }
    public void setCoverageRadius(Double coverageRadius) { this.coverageRadius = coverageRadius; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Estatus getStatus() { return status; }
    public void setStatus(Estatus status) { this.status = status; }
}