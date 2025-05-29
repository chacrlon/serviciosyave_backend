package com.serviciosyave.entities;

import com.serviciosyave.Enum.Estatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Paso 1: Verificación
    private String fullName;

    @Lob
    @Column(name = "dni_front_name", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] dniFrontName;

    @Lob
    @Column(name = "dni_back_name", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] dniBackName;

    @Lob
    @Column(name = "selfie_name", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] selfieName;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] profilePicture;

    // Paso 2: Información Profesional
    @Lob
    @Column(name = "university_title_name", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] universityTitleName;

    @ElementCollection
    @Lob
    @Column(name = "certifications_names", columnDefinition = "LONGBLOB", nullable = true)
    @CollectionTable(name = "seller_certifications_names", joinColumns = @JoinColumn(name = "seller_id"))
    private List<byte[]> certificationsNames = new ArrayList<>();

    @ElementCollection
    @Lob
    @Column(name = "gallery_images_names", columnDefinition = "LONGBLOB", nullable = true)
    @CollectionTable(name = "seller_gallery_images_names", joinColumns = @JoinColumn(name = "seller_id"))
    private List<byte[]> galleryImagesNames = new ArrayList<>();

    private String profession;
    private Integer yearsOfExperience; // Cambiado de int a Integer
    private String skillsDescription;

    // Relación con User (lado inverso)
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "seller_subcategories",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private List<Subcategory> subcategories = new ArrayList<>();

    // Postularse en las subcategorias
    @ManyToMany
    @JoinTable(
            name = "seller_selected_subcategories",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private List<Subcategory> selectedSubcategories = new ArrayList<>();

    private String serviceArea;
    private Double latitude;   // Cambiado de double a Double
    private Double longitude;  // Cambiado de double a Double
    private Double coverageRadius; // Cambiado de double a Double

    private LocalDateTime createdAt;

    //PENDIENTE, APROBADO, RECHAZADO
    private Estatus status;



    public Seller() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte[] getDniFrontName() {
        return dniFrontName;
    }

    public void setDniFrontName(byte[] dniFrontName) {
        this.dniFrontName = dniFrontName;
    }

    public byte[] getDniBackName() {
        return dniBackName;
    }

    public void setDniBackName(byte[] dniBackName) {
        this.dniBackName = dniBackName;
    }

    public byte[] getSelfieName() {
        return selfieName;
    }

    public void setSelfieName(byte[] selfieName) {
        this.selfieName = selfieName;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getUniversityTitleName() {
        return universityTitleName;
    }

    public void setUniversityTitleName(byte[] universityTitleName) {
        this.universityTitleName = universityTitleName;
    }

    public List<byte[]> getCertificationsNames() {
        return certificationsNames;
    }

    public void setCertificationsNames(List<byte[]> certificationsNames) {
        this.certificationsNames = certificationsNames;
    }

    public List<byte[]> getGalleryImagesNames() {
        return galleryImagesNames;
    }

    public void setGalleryImagesNames(List<byte[]> galleryImagesNames) {
        this.galleryImagesNames = galleryImagesNames;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getSkillsDescription() {
        return skillsDescription;
    }

    public void setSkillsDescription(String skillsDescription) {
        this.skillsDescription = skillsDescription;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Subcategory> getSelectedSubcategories() {
        return selectedSubcategories;
    }

    public void setSelectedSubcategories(List<Subcategory> selectedSubcategories) {
        this.selectedSubcategories = selectedSubcategories;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getCoverageRadius() {
        return coverageRadius;
    }

    public void setCoverageRadius(Double coverageRadius) {
        this.coverageRadius = coverageRadius;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Estatus getStatus() {
        return status;
    }

    public void setStatus(Estatus status) {
        this.status = status;
    }
}

