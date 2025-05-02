package com.serviciosyave.entities;

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

    // Paso 1: Información Personal
    private String fullName;
    private int age;
    private LocalDate birthdate;
    private String idNumber;
    private String gender;
    private String city;

    // Paso 2: Información Profesional
    private String profession;
    private int yearsOfExperience;
    private String skillsDescription;

    @ManyToOne
    private User user;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Modality> modalities;

    // Opcional: Crear un enum
    public enum Modality {
        PRESENCIAL, ONLINE
    }

    // Paso 3: Verificación
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
    @Lob
    @Column(name = "university_title_name", columnDefinition = "LONGBLOB")
    private byte[] universityTitleName;

    @ElementCollection
    @Lob
    @Column(name = "certifications_names", columnDefinition = "LONGBLOB")
    @CollectionTable(name = "seller_certifications_names", joinColumns = @JoinColumn(name = "seller_id"))
    private List<byte[]> certificationsNames;

    // Paso 4: Galería
    @ElementCollection
    @Lob
    @Column(name = "gallery_images_names", columnDefinition = "LONGBLOB")
    @CollectionTable(name = "seller_gallery_images_names", joinColumns = @JoinColumn(name = "seller_id"))
    private List<byte[]> galleryImagesNames;

    // Campos adicionales
    private LocalDateTime createdAt;

    public Seller() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<Modality> getModalities() {
        return modalities;
    }

    public void setModalities(List<Modality> modalities) {
        this.modalities = modalities;
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
}

