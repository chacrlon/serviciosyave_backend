package com.serviciosyave.entities;

import com.serviciosyave.Enum.Estatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(name = "dni_front_name", columnDefinition = "LONGBLOB")
    private byte[] dniFrontName;
    @Lob
    @Column(name = "dni_back_name", columnDefinition = "LONGBLOB")
    private byte[] dniBackName;
    @Lob
    @Column(name = "selfie_name", columnDefinition = "LONGBLOB")
    private byte[] selfieName;
    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;


    // Paso 2: Información Profesional
    @Lob
    @Column(name = "university_title_name", columnDefinition = "LONGBLOB")
    private byte[] universityTitleName;

    @ElementCollection
    @Lob
    @Column(name = "certifications_names", columnDefinition = "LONGBLOB")
    @CollectionTable(name = "seller_certifications_names", joinColumns = @JoinColumn(name = "seller_id"))
    private List<byte[]> certificationsNames;

    @ElementCollection
    @Lob
    @Column(name = "gallery_images_names", columnDefinition = "LONGBLOB")
    @CollectionTable(name = "seller_gallery_images_names", joinColumns = @JoinColumn(name = "seller_id"))
    private List<byte[]> galleryImagesNames;

    private String profession;
    private int yearsOfExperience;
    private String skillsDescription;

    // Relación con User (lado inverso)
    @Column(name = "user_id", nullable = true) // Columna en la tabla 'sellers' que referencia a 'users.id'
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "seller_subcategories",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private List<Subcategory> subcategories;

    // Postularse en las subcategorias
    @ManyToMany
    @JoinTable(
            name = "seller_selected_subcategories",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private List<Subcategory> selectedSubcategories;

    //La ubicacion del profesional
    private String serviceArea;
    private double latitude;
    private double longitude;
    private double coverageRadius; // En kilómetros


    // Esto es por debajo
    private LocalDateTime createdAt;

    //PENDIENTE, APROBADO, RECHAZADO
    private Estatus status;


    public Seller() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getSkillsDescription() {
        return skillsDescription;
    }

    public void setSkillsDescription(String skillsDescription) {
        this.skillsDescription = skillsDescription;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getCoverageRadius() {
        return coverageRadius;
    }

    public void setCoverageRadius(double coverageRadius) {
        this.coverageRadius = coverageRadius;
    }

    public Estatus getStatus() {
        return status;
    }

    public void setStatus(Estatus status) {
        this.status = status;
    }
}

